package server.database;

import common.objects.*;
import common.objects.requests.DialogTypes;
import common.objects.requests.RegistrationData;
import common.objects.requests.UserPasswordData;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static server.database.ObjectsBuilder.buildGroupInfo;
import static server.database.ObjectsBuilder.buildMessage;
import static server.database.ObjectsBuilder.buildUser;

public class MySQLExtractor implements DatabaseExtractor {
    private DatabaseConnector connector;

    MySQLExtractor(DatabaseConnector connector) {
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
        connector.insert("online", CustomMap.create()
                .add("login", registrationData.getLogin())
                .add("last_online", Long.toString(System.currentTimeMillis()))
                .add("online", "0"));
    }

    @Override
    public int createGroup(String creator, String title, List<String> partners, boolean hasPhoto) throws SQLException {
        int dialogId = connector.insert("dialogs", CustomMap.create()
                        .add("dialog_name", title)
                        .add("creator", creator)
                        .add("type", Integer.toString(DialogTypes.GROUP))
                        .add("has_photo", hasPhoto? "1":"0"));
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
                .add("type", Integer.toString(DialogTypes.CHANNEL)));
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
                .add("type", Integer.toString(DialogTypes.DIALOG)));
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
        String query = "SELECT u.login as login, u.name as name, u.has_avatar as has_avatar FROM user_dialog AS u_d LEFT JOIN users AS u ON u_d.login = u.login WHERE dialog_id = ?;";
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
    public List<DialogMarker> getDialogs(String login, Integer count) throws SQLException {
        String query = "SELECT d.dialog_id AS dialog_id, d.type as type from user_dialog AS u_d LEFT JOIN dialogs AS d on u_d.dialog_id=d.dialog_id LEFT JOIN messages as m ON d.last_message_id = m.message_id where login = ? ORDER BY time LIMIT "+count+";";
        Map<Integer, Integer> dialogIdsTypes = connector.select(query, rs->
        {
            Map<Integer, Integer> map = new TreeMap<>();
            while (rs.next())
                map.put(rs.getInt("dialog_id"), rs.getInt("type"));
            return map;
        }, login);
        List<Integer> dialogIds = dialogIdsTypes.entrySet().stream().filter(e->e.getValue() == DialogTypes.DIALOG).map(Map.Entry::getKey).collect(Collectors.toList());
        List<Integer> groupIds = dialogIdsTypes.entrySet().stream().filter(e->e.getValue() != DialogTypes.DIALOG).map(Map.Entry::getKey).collect(Collectors.toList());

        List<DialogMarker> dialogs = getAsDialog(dialogIds, login), groups = getAsGroup(groupIds, login);
        dialogs.addAll(groups);
        return dialogs;

        /*String query = "SELECT d.dialog_id AS dialog_id, d.dialog_name AS dialog_name, d.creator AS creator, d.last_message_id as message_id, d.type as type, d.has_photo as has_photo, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from user_dialog AS u_d LEFT JOIN dialogs AS d on u_d.dialog_id=d.dialog_id LEFT JOIN messages as m ON d.last_message_id = m.message_id where login = ? ORDER BY time LIMIT "+count+";";

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
        return dialogList;*/
    }

    @Override
    public UserProfile getUserProfile(String login) throws SQLException {
        return connector.select("SELECT login, email, info, name, has_avatar FROM users WHERE login = ?;", resultSet -> {
            UserProfile userProfile = new UserProfile();
            if (resultSet.next())
            {
                userProfile = ObjectsBuilder.buildProfile(resultSet);
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
    public FullDialog getFullDialog(Integer dialogId, String login) throws SQLException {
        //Extracting type
        String query = "SELECT type FROM dialogs WHERE dialog_id = ?;";
        Integer type = connector.select(query, rs -> {
            if (rs.next())
                return rs.getInt(1);
            else
                return -1;
        }, dialogId.toString());
        if (type == -1)
            throw new SQLException("GROUP "+dialogId+" NOT FOUND!");
        List<Integer> id = new LinkedList<>();
        id.add(dialogId);
        //According to its type, extracting info
        DialogMarker dialog = (type == DialogTypes.DIALOG? getAsDialog(id, login): getAsGroup(id, login)).get(0);
        FullDialog fullDialog = new FullDialog();
        fullDialog.setDialog(getDialogById(dialogId));
        fullDialog.setInfo(dialog);

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
    private List<DialogMarker> getAsDialog(List<Integer> dialogIds, String login) throws SQLException
    {
        if (dialogIds.isEmpty())
            return new LinkedList<>();
        //Extracting info
        String wrappedIds = dialogIds.stream().map(Object::toString).collect(Collectors.joining(",", "(", ")"));
        String query = "SELECT d.dialog_id AS dialog_id, d.dialog_name AS dialog_name, d.last_message_id as message_id, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from dialogs AS d LEFT JOIN messages as m ON d.last_message_id = m.message_id where d.dialog_id in "+wrappedIds+";";
        List<DialogInfo> infos =  connector.select(query, rs->{
            List<DialogInfo> dialogMarkers = new LinkedList<>();
            while (rs.next())
            {
                DialogInfo dialogInfo = new DialogInfo();
                dialogInfo.setType(DialogTypes.DIALOG).setDialogId(rs.getInt("dialog_id"))
                        .setLastMessage(ObjectsBuilder.buildMessage(rs));
                dialogMarkers.add(dialogInfo);
            }
            return dialogMarkers;
        });
        //Extracting a partner
        query = "SELECT u.login AS login, u.name AS name, u.has_avatar AS has_avatar, u_d.dialog_id as dialog_id FROM user_dialog AS u_d LEFT JOIN users AS u ON u_d.login = u.login WHERE u_d.dialog_id IN "+wrappedIds+" AND u_d.login <> ?;";
        final Map<Integer, User> partners = connector.select(query, rs->{
            Map<Integer, User> map = new TreeMap<>();
            while (rs.next())
            {
                map.put(rs.getInt("dialog_id"), buildUser(rs));
            }
            return map;
        }, login);
        //Extracting count of unread messages
        query = "SELECT m.dialog_id AS dialog_id, count(*) AS count FROM messages as m LEFT JOIN unread_messages AS u_m ON m.message_id = u_m.message_id WHERE u_m.login = ? AND m.dialog_id IN "+wrappedIds+" GROUP BY m.dialog_id;";
        final Map<Integer, Integer> unread = connector.select(query, rs->{
            Map<Integer, Integer> map = new TreeMap<>();
            while (rs.next())
                map.put(rs.getInt("dialog_id"), rs.getInt("count"));
            return map;
        }, login);
        //Joining
        infos.forEach(di->{
            di.setPartner(partners.get(di.getDialogId()));
            di.setUnread(unread.getOrDefault(di.getDialogId(), 0));
        });
        return new LinkedList<>(infos);
    }
    private List<DialogMarker> getAsGroup(List<Integer> dialogIds, String login) throws SQLException
    {
        if (dialogIds.isEmpty())
            return new LinkedList<>();
        //Extracting info
        String wrappedIds = dialogIds.stream().map(Object::toString).collect(Collectors.joining(",", "(", ")"));
        String query = "SELECT d.dialog_id AS dialog_id, d.dialog_name AS dialog_name, d.creator AS creator, d.type AS type, d.has_photo AS has_photo, count(*) AS user_count, m.message_id AS message_id, m.sender AS sender, m.text AS text, m.time AS time, m.is_system AS is_system FROM dialogs AS d LEFT JOIN messages AS m ON d.last_message_id=m.message_id LEFT JOIN user_dialog AS u_d ON u_d.dialog_id = d.dialog_id WHERE d.dialog_id IN "+wrappedIds+" GROUP BY dialog_id;";
        List<GroupInfo> groupInfos = connector.select(query, rs->{
            List<GroupInfo> list = new LinkedList<>();
            while (rs.next())
            {
               Message last = buildMessage(rs);
               GroupInfo groupInfo = buildGroupInfo(rs).setLastMessage(last);
               list.add(groupInfo);
            }
            return list;
        });
        //Extracting count of unread messages
        query = "SELECT m.dialog_id AS dialog_id, count(*) AS count FROM messages as m LEFT JOIN unread_messages AS u_m ON m.message_id = u_m.message_id WHERE u_m.login = ? AND m.dialog_id IN "+wrappedIds+" GROUP BY m.dialog_id;";
        final Map<Integer, Integer> unread = connector.select(query, rs->{
            Map<Integer, Integer> map = new TreeMap<>();
            while (rs.next()) {
                map.put(rs.getInt("dialog_id"), rs.getInt("count"));
            }
            return map;
        }, login);
        //Joining
        groupInfos.forEach(gi-> gi.setUsersCount(unread.getOrDefault(gi.getDialogId(), 0)));
        return new LinkedList<>(groupInfos);
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
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
