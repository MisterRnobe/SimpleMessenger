package server.database;

import common.objects.*;
import common.objects.requests.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static server.database.ObjectsBuilder.buildMessage;
import static server.database.ObjectsBuilder.buildUser;
import static server.database.ObjectsBuilder.buildDialogInfo;

public class MySQLExtractor implements DatabaseExtractor {
    private DatabaseConnector connector;

    public MySQLExtractor(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public boolean verifyUser(UserPasswordData userPasswordData) throws SQLException {
        return connector.select("SELECT COUNT(*) FROM users WHERE login = ? AND password = ?;",
                resultSet ->
        {
            if (resultSet.next())
                return resultSet.getInt(1) != 0;
            throw new SQLException();
        }, userPasswordData.getLogin(), hash256(userPasswordData.getPassword()));
    }

    @Override
    public void addUser(RegistrationData registrationData) throws SQLException {
       connector.insert("users", CustomMap.create()
                .add("login", registrationData.getLogin())
                .add("password", hash256(registrationData.getPassword()))
                .add("name", registrationData.getName())
                .add("email", registrationData.getEmail())
                .add("info", registrationData.getInfo())
                .add("has_avatar", registrationData.getAvatar() != null? "1":"0"));
        connector.insert("users", CustomMap.create()
                .add("login", registrationData.getLogin())
                .add("last_online", Long.toString(System.currentTimeMillis()))
                .add("online", "0"));
    }

    @Override
    public int createGroup(String creator, String title, List<String> partners) throws SQLException {
        int dialogId = connector.insert("dialogs", CustomMap.create()
                        .add("dialog_name", title)
                        .add("creator", creator)
                        .add("type", Integer.toString(DialogInfo.GROUP)));
        if (dialogId == -1)
            return dialogId;
        connector.insert("user_dialog", CustomMap.create()
                        .add("login", creator)
                        .add("dialog_id", Integer.toString(dialogId)));
        for(String s: partners) {
            connector.insert("user_dialog", CustomMap.create()
                    .add("login", s)
                    .add("dialog_id", Integer.toString(dialogId)));
        }
        return dialogId;
    }

    @Override
    public int createChannel(String creator, String title, List<String> partners) throws SQLException {
        int dialogId = connector.insert("dialogs",CustomMap.create()
                .add("dialog_name", title)
                .add("creator", creator)
                .add("type", Integer.toString(DialogInfo.CHANNEL)));
        if (dialogId == -1)
            throw new SQLException();
        connector.insert("user_dialog",CustomMap.create()
                        .add("login", creator)
                        .add("dialog_id", Integer.toString(dialogId)));
        for(String partner: partners)
        {
            connector.insert("user_dialog", CustomMap.create()
                            .add("login", partner)
                            .add("dialog_id", Integer.toString(dialogId)));
        }
        return dialogId;
    }

    @Override
    public int createDialog(String creator, String partner) throws SQLException {
        int dialogId = connector.insert("dialogs",CustomMap.create()
                .add("creator", creator)
                .add("type", Integer.toString(DialogInfo.DIALOG)));
        if (dialogId == -1)
            return dialogId;
        connector.insert("user_dialog", CustomMap.create()
                .add("login", partner)
                .add("dialog_id", Integer.toString(dialogId)));
        connector.insert("user_dialog", CustomMap.create()
                .add("login", creator)
                .add("dialog_id", Integer.toString(dialogId)));
        return dialogId;
    }

    @Override
    public List<User> getUsersInDialog(Integer dialogId) throws SQLException {
        String query = "SELECT u.login, u.name FROM user_dialog AS u_d LEFT JOIN users AS u ON u_d.login = u.login WHERE dialog_id = ?;";
        return connector.select(query, resultSet -> {
            List<User> users = new LinkedList<>();
            while (resultSet.next()) {
                users.add(buildUser(resultSet));
            }
            return users;
        }, Integer.toString(dialogId));
    }

    @Override
    public List<User> findUsers(String mask) throws SQLException {
        String sqlMask = "%"+mask+"%";
        String query = "SELECT u.login AS login, u.name as name, o.last_online as last_online, o.online AS online, u.has_avatar AS has_avatar FROM users AS u LEFT JOIN online AS o ON u.login = o.login WHERE u.login LIKE ? OR u.name LIKE ?;";
        return connector.select(query, resultSet->{
            List<User> users = new LinkedList<>();
            while (resultSet.next())
            {
                users.add(buildUser(resultSet));
            }
            return users;
            }, sqlMask, sqlMask);
    }


    @Override
    public DialogList getDialogs(String login, Integer count) throws SQLException {
        String query = "SELECT d.dialog_id AS dialog_id, d.dialog_name AS dialog_name, d.creator AS creator, d.last_message_id as message_id, d.type as type, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from user_dialog AS u_d LEFT JOIN dialogs AS d on u_d.dialog_id=d.dialog_id LEFT JOIN messages as m ON d.last_message_id = m.message_id where login = ? ORDER BY time LIMIT "+count+";";

        DialogList dialogList = connector.select(query, rs->{
            DialogList templateList = new DialogList();
            while (rs.next())
            {
                DialogInfo info = buildDialogInfo(rs);
                Message m = buildMessage(rs);
                info.setLastMessage(m);
                templateList.addDialog(info);
            }
            return templateList;
        },login);
        if (dialogList.getDialogs().size() == 0)
            return dialogList;
        for(DialogInfo di: dialogList.getDialogs())
        {
            di.setUsers(getUsersInDialog(di.getDialogId()));
        }
        String dialogIds = dialogList.getDialogs().stream()
                .map(DialogInfo::getDialogId)
                .map(Object::toString)
                .collect(Collectors.joining(",","(",")"));
        query = "SELECT m.dialog_id as dialog_id, count(*) as count FROM messages as m left join unread_messages as u_m on m.message_id = u_m.message_id where u_m.login = ? AND m.dialog_id IN "+dialogIds+" group by m.dialog_id;";
        connector.select(query, resultSet->{
            while (resultSet.next()) {
                int dialogId = resultSet.getInt("dialog_id");
                int unread = resultSet.getInt("count");
                dialogList.getDialogs().stream().filter(d->d.getDialogId() == dialogId).findFirst().ifPresent(dialogInfo -> dialogInfo.setUnread(unread));
            }
            return null;
        },login);
        return dialogList;
    }

    @Override
    public UserProfile getUserProfile(String login) throws SQLException {
        return connector.select("SELECT login, email, info FROM users WHERE login = ?;", resultSet -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setLogin(login);
            if (resultSet.next())
            {
                userProfile.setEmail(resultSet.getString("email"));
                userProfile.setInfo(resultSet.getString("info"));
                userProfile.setName(resultSet.getString("name"));
                userProfile.setLogin(resultSet.getString("login"));
            }
            return userProfile;
        }, login);
    }

    @Override
    public User getUserStatus(String login) throws SQLException {
        return connector.select("SELECT last_online, login FROM online WHERE login = ?;", rs->{
            User u = null;
            if (rs.next())
            {
                u = buildUser(rs);
            }
            return u;
        },login);
    }

    @Override
    public List<Message> getMessagesByIds(List<Integer> ids) throws SQLException {
        String query = "SELECT * FROM messages WHERE message_id IN "+
                ids.stream().map(Object::toString).collect(Collectors.joining(",","(", ")"))+";";
        return connector.select(query, rs->{
            List<Message> messages = new LinkedList<>();
            while (rs.next())
            {
                messages.add(buildMessage(rs));
            }
            return messages;
        });
    }

    @Override
    public User getUser(String login) throws SQLException {
        return connector.select("SELECT login, name, has_avatar FROM users WHERE login = ?;",rs->
        {
            User u = null;
            if (rs.next())
                u = buildUser(rs);
            return u;
        }, login);
    }

    @Override
    public void readMessages(Integer dialogId, String login) throws SQLException {
        String query = "DELETE from unread_messages where login = '"+login+"' and message_id IN (select message_id from messages where dialog_id = "+dialogId+");";
        connector.doQuery(query);
    }

    @Override
    public void addUsersToDialog(Integer dialogId, List<String> users) throws SQLException {
        for(String str: users)
        {
            connector.insert("user_dialog", CustomMap.create()
                            .add("dialog_id", dialogId.toString())
                            .add("login", str));
        }
    }

    @Override
    public Dialog getDialogById(Integer dialogId) throws SQLException {
        String query = "SELECT dialog_id, message_id, sender, text, time, is_system FROM messages WHERE dialog_id = ?;";
        return connector.select(query,rs->{
            Dialog dialog = new Dialog();
            List<Message> messages = new ArrayList<>();
            while (rs.next())
            {
                messages.add(buildMessage(rs));
            }
            dialog.setMessages(messages);
            dialog.setDialogId(dialogId);
            return dialog;
        }, dialogId.toString());
    }

    @Override
    public void setOnline(String login) throws SQLException {
        connector.update("login", login, CustomMap.create().add("online","1"), "online");
    }

    @Override
    public void setOffline(String login) throws SQLException {
        connector.update("login", login, CustomMap.create()
                .add("online","0")
                .add("last_online", Long.toString(System.currentTimeMillis())), "online");
    }

    @Override
    public FullDialog getFullDialog(Integer dialogId) throws SQLException {
        String query = "SELECT d.dialog_id as dialog_id, d.dialog_name AS dialog_name, d.creator AS creator, d.last_message_id as message_id, d.type AS type, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from dialogs AS d LEFT JOIN messages as m ON d.last_message_id = m.message_id where d.dialog_id = ?;";
        DialogInfo dialogInfo = connector.select(query, rs -> {
            DialogInfo di = null;
            if (rs.next()) {
                di = buildDialogInfo(rs);
                Message m = buildMessage(rs);
                di.setLastMessage(m);
            }
            return di;
        }, Integer.toString(dialogId));
        dialogInfo.setUsers(getUsersInDialog(dialogId));
        FullDialog fullDialog = new FullDialog();
        fullDialog.setDialogInfo(dialogInfo);
        fullDialog.setDialog(getDialogById(dialogId));
        return fullDialog;
    }

    @Override
    public int addMessage(Integer dialogId, String sender, String text, Long time) throws SQLException {
        int messageId = connector.insert("messages", CustomMap.create()
                .add("sender", sender)
                .add("dialog_id", dialogId.toString())
                .add("text",text)
                .add("time",time.toString())
                .add("is_system", sender == null? "1": "0"));
        List<String> users = getUsersInDialog(dialogId).stream().map(User::getLogin)
                .filter(s->!s.equalsIgnoreCase(sender)).collect(Collectors.toList());
        for(String str: users)
        {
            connector.insert("unread_messages", CustomMap.create()
                    .add("login", str)
                    .add("message_id", Integer.toString(messageId)));
        }
        return messageId;
    }

    @Override
    public void setLastMessage(Integer dialogId, Integer messageId) throws SQLException {
        connector.update("dialog_id", dialogId.toString(), CustomMap.create().add("last_message_id", messageId.toString()), "dialogs");
    }

    public static class CustomMap extends TreeMap<String, String>
    {
        public static CustomMap create()
        {
            return new CustomMap();
        }
        public CustomMap add(String key, String value)
        {
            this.put(key, value);
            return this;
        }
    }
    private static String hash256(String data) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
