package server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.objects.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    static void init()
    {
        instance = new DatabaseConnector();
    }

    public static DatabaseConnector getInstance() {
        return instance;
    }

    private Connection connection;
    private DatabaseConnector()
    {
        try(Scanner s = new Scanner(new File("target/classes/server/data.txt")))
        {
            StringBuilder builder = new StringBuilder();
            while (s.hasNext())
            {
                builder.append(s.nextLine());
                builder.append("\n");
            }
            JSONObject o = (JSONObject) JSON.parse(builder.toString());
            String login = (String) o.get("login");
            String password = (String) o.get("password");
            String url = (String) o.get("url");

            connection = DriverManager.getConnection(url, login, password);
            //System.out.println("OK");

        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean verifyUser(String login, String password)
    {
        return checkExistence(new TreeMap<String, String>(){
            {this.put("login", login); this.put("password", password);}
        }, "users");
    }
    public void addUser(String login, String password, String name, String email, String info)
    {
        insert(CustomMap.create()
                .add("login", login)
                .add("password", password)
                .add("name", name)
                .add("email", email)
                .add("info", info), "users");
        insert(CustomMap.create()
                .add("login", login)
                .add("last_online", Long.toString(System.currentTimeMillis()))
                .add("online", "0"), "online");
    }
    public int createGroup(String creator, String title, List<String> partners)
    {
        int dialogId = insert(CustomMap.create()
                .add("dialog_name", title)
                .add("creator", creator)
                .add("type", Integer.toString(DialogInfo.GROUP))
                , "dialogs");
        if (dialogId == -1)
            return dialogId;
        insert(CustomMap.create()
                .add("login", creator)
                .add("dialog_id", Integer.toString(dialogId)),
                "user_dialog");
        partners.forEach(s->
                insert(CustomMap.create()
                        .add("login", s)
                        .add("dialog_id", Integer.toString(dialogId)),
                        "user_dialog"
                ));
        return dialogId;
    }
    public int createChannel(String creator, String title, List<String> partners)
    {
        int dialogId = insert(CustomMap.create()
                .add("dialog_name", title)
                .add("creator", creator)
                .add("type", Integer.toString(DialogInfo.CHANNEL)), "dialogs");
        if (dialogId == -1)
            return dialogId;
        insert(CustomMap.create()
                .add("login", creator)
                .add("dialog_id", Integer.toString(dialogId)),
                "user_dialog");
        partners.forEach(s->
                insert(CustomMap.create()
                                .add("login", s)
                                .add("dialog_id", Integer.toString(dialogId)),
                        "user_dialog"));
        return dialogId;
    }

    public int createDialog(String creator, String partner)
    {
        int dialogId = insert(CustomMap.create()
                .add("creator", creator)
                .add("type", Integer.toString(DialogInfo.DIALOG)), "dialogs");
        if (dialogId == -1)
            return dialogId;
        insert(CustomMap.create()
                .add("login", partner)
                .add("dialog_id", Integer.toString(dialogId)), "user_dialog");
        insert(CustomMap.create()
                .add("login", creator)
                .add("dialog_id", Integer.toString(dialogId)), "user_dialog");
        return dialogId;
    }
    public List<User> getUsersInDialog(Integer dialogId)
    {
        List<User> users = new LinkedList<>();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT u.login, u.name FROM user_dialog AS u_d LEFT JOIN users AS u ON u_d.login = u.login WHERE dialog_id = "+wrapInQuotes(dialogId)+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                while (resultSet.next()) {
                    User u = new User();
                    u.setLogin(resultSet.getString(1));
                    u.setName(resultSet.getString(2));
                    users.add(u);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return users;
    }
    public List<User> findUsers(String mask)
    {
        List<User> userList = new LinkedList<>();
        try (Statement statement = connection.createStatement())
        {
            String str = wrapInQuotes("%"+mask+"%");
            String query = "SELECT u.login, u.name, o.last_online FROM users AS u LEFT JOIN online AS o ON u.login = o.login WHERE u.login LIKE "+str+
                    " OR u.name LIKE "+str+";";
            System.out.println(query);
            try (ResultSet resultSet = statement.executeQuery(query))
            {
                while (resultSet.next())
                {
                    User u = new User();
                    u.setLogin(resultSet.getString(1));
                    u.setName(resultSet.getString(2));
                    u.setLastOnline(resultSet.getLong(3));
                    u.setOnline(OnlineManager.getInstance().isOnline(u.getLogin()));
                    userList.add(u);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    public DialogList getDialogs(String login, String count)
    {
        DialogList dialogList = new DialogList();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT d.dialog_id as dialog_id, d.dialog_name, d.creator, d.last_message_id as message_id, d.type, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from user_dialog AS u_d LEFT JOIN dialogs AS d on u_d.dialog_id=d.dialog_id LEFT JOIN messages as m ON d.last_message_id = m.message_id where login = "+wrapInQuotes(login)+" ORDER BY time LIMIT "+count+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {

                while(resultSet.next())
                {
                    DialogInfo info = new DialogInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(5));
                    Message m = buildMessage(resultSet);
                    info.setLastMessage(m);
                    dialogList.addDialog(info);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        dialogList.getDialogs().forEach(d-> d.setUsers(getUsersInDialog(d.getDialogId())));
        String dialogIds = dialogList.getDialogs().stream().map(DialogInfo::getDialogId).map(Object::toString).collect(Collectors.joining(",","(",")"));
        String query = "SELECT m.dialog_id as dialog_id, count(*) as count FROM messages as m left join unread_messages as u_m on m.message_id = u_m.message_id where u_m.login = "+wrapInQuotes(login)+" AND m.dialog_id IN "+dialogIds+" group by m.dialog_id;";
        executeSelect(query, resultSet -> {
            try {
                while (resultSet.next()) {
                    int dialogId = resultSet.getInt("dialog_id");
                    int unread = resultSet.getInt("count");
                    dialogList.getDialogs().stream().filter(d->d.getDialogId() == dialogId).findFirst().ifPresent(dialogInfo -> dialogInfo.setUnread(unread));
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        });
        return dialogList;
    }
    private int insert(Map<String, String> map, String table)
    {
        try{
            String[] wrapped = wrap(map);
            String query = "INSERT INTO "+table+" ("+wrapped[0]+") VALUES ("+wrapped[1]+");";
            System.out.println(query);
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            try(ResultSet generatedKey = statement.getGeneratedKeys())
            {
              if (generatedKey.next())
                  return generatedKey.getInt(1);
              else
                  return -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public User getUserStatus(String login)
    {
        return executeSelect("SELECT last_online FROM online WHERE login = " + wrapInQuotes(login) + ";",
                resultSet -> {
                    User u = new User();
                    try {
                        if (resultSet.next())
                        {
                            u.setLastOnline(resultSet.getLong(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return u;
                });
    }
    private <E> E executeSelect(String query, Function<ResultSet, E> consumer)
    {
        E result = null;
        System.out.println(query);
        try(Statement statement = connection.createStatement())
        {
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                result = consumer.apply(resultSet);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    private boolean doQuery(String query)
    {
        System.out.println(query);
        try(Statement statement = connection.createStatement())
        {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public List<Message> getMessagesByIds(List<Integer> ids)
    {
        String query = "SELECT * FROM messages WHERE message_id IN "+
                ids.stream().map(Object::toString).collect(Collectors.joining(",","(", ")"))+";";
        return executeSelect(query, resultSet -> {
            List<Message> messages = new LinkedList<>();
            try {
                while (resultSet.next())
                {
                    messages.add(buildMessage(resultSet));
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            return messages;
        });
    }
    public User getUser(String login)
    {
        User u = new User();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT login, name FROM users WHERE login = "+wrapInQuotes(login)+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                resultSet.next();
                u.setLogin(resultSet.getString(1));
                u.setName(resultSet.getString(2));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return u;
    }
    public boolean readMessages(Integer dialogId, String login)
    {
        String query = "DELETE from unread_messages where login = "+wrapInQuotes(login)+" and message_id IN (select message_id from messages where dialog_id = "+wrapInQuotes(dialogId)+");";
        return doQuery(query);
    }
    public void addUsersToDialog(Integer dialogId, List<String> users)
    {
        users.forEach(str-> insert(CustomMap.create()
                .add("dialog_id", dialogId.toString())
                .add("login", str),
                "user_dialog"));
    }
    public Dialog getDialogById(Integer dialogId)
    {
        Dialog dialog = new Dialog();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT dialog_id, message_id, sender, text, time, is_system FROM messages WHERE dialog_id = "+wrapInQuotes(dialogId)+";";
            List<Message> messages = new LinkedList<>();
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                while (resultSet.next())
                {
                    Message m = buildMessage(resultSet);
                    messages.add(m);
                }
            }
            dialog.setDialogId(dialogId);
            dialog.setMessages(messages);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dialog;
    }
    public void setOnline(String login)
    {
        update("login", login, new TreeMap<String, String>(){{
            this.put("online", "1");
        }}, "online");
    }
    public void setOffline(String login)
    {
        update("login", login, new TreeMap<String, String>(){{
            this.put("online", "0");
            this.put("last_online", Long.toString(System.currentTimeMillis()));
        }}, "online");
    }
    public FullDialog getFullDialog(Integer dialogId)
    {
        FullDialog fullDialog = new FullDialog();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT d.dialog_id as dialog_id, d.dialog_name, d.creator, d.last_message_id as message_id, d.type, m.sender as sender, m.text as text, m.time as time, m.is_system as is_system from dialogs AS d LEFT JOIN messages as m ON d.last_message_id = m.message_id where d.dialog_id = "+wrapInQuotes(dialogId)+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                resultSet.next();
                DialogInfo info = new DialogInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(5));
                Message m = buildMessage(resultSet);
                info.setLastMessage(m);
                fullDialog.setDialogInfo(info);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        fullDialog.getDialogInfo().setUsers(getUsersInDialog(dialogId));
        fullDialog.setDialog(getDialogById(dialogId));
        return fullDialog;
    }
    private void update(String field, String id, Map<String, String> values, String table)
    {
        try
        {
            Statement statement = connection.createStatement();
            String newValues = values.entrySet().stream()
                    .map(e-> e.getKey() +" = "+ (e.getKey().equals("password")? wrapPassword(e.getValue()): wrapInQuotes(e.getValue())))
                    .collect(Collectors.joining(", "));
            String query = "UPDATE " + table+" SET " + newValues+" WHERE "+field+" = "+wrapInQuotes(id)+";";
            System.out.println(query);
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //True, if exists
    private boolean checkExistence(Map<String, String> values, String table)
    {
        try {
            Statement statement = connection.createStatement();
            String condition = values.entrySet().stream()
                                    .map(e -> {
                                        StringBuilder s = new StringBuilder();
                                        s.append(e.getKey()).append(" = ");
                                        if (e.getKey().equals("password"))
                                            s.append(wrapPassword(e.getValue()));
                                        else
                                            s.append(wrapInQuotes(e.getValue()));
                                        return s.toString();
                                    })
                                    .collect(Collectors.joining(" AND "));

            String q = "SELECT COUNT(*) FROM "+table+" WHERE "+condition+";";
            System.out.println(q);
            ResultSet resultSet = statement.executeQuery(q);
            resultSet.next();
            boolean result = resultSet.getInt(1) != 0;
            statement.close();
            resultSet.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setLastMessage(Integer dialogId, Integer messageId)
    {
        update("dialog_id", dialogId.toString(), CustomMap.create().add("last_message_id", messageId.toString()), "dialogs");
    }
    private Message buildMessage(ResultSet resultSet) throws SQLException
    {
        Message m = new Message();
        m.setDialogId(resultSet.getInt("dialog_id"));
        m.setText(resultSet.getString("text"));
        m.setTime(resultSet.getLong("time"));
        m.setMessageId(resultSet.getInt("message_id"));
        m.setIsSystem(resultSet.getBoolean("is_system"));
        if (!m.getIsSystem())
        {
            m.setSender(resultSet.getString("sender"));
        }
        return m;
    }
    public int addMessage(Integer dialogId, String sender, String text, Long time)
    {
        int messageId = insert(CustomMap.create()
                .add("sender", sender)
                .add("dialog_id", dialogId.toString())
                .add("text",text)
                .add("time",time.toString())
                .add("is_system", sender == null? "1": "0"),"messages");
        getUsersInDialog(dialogId).stream().map(User::getLogin).filter(s->!s.equalsIgnoreCase(sender))
                .forEach(str-> insert(CustomMap.create()
                .add("login", str)
                .add("message_id", Integer.toString(messageId)), "unread_messages"));

        return messageId;
    }

    public boolean checkUserExistence(String field, String value)
    {
        return checkExistence(CustomMap.create().add(field, value), "users");
    }
    private String wrapInQuotes(Object str)
    {
        return "'"+str+"'";
    }
    private String wrapPassword(String pass)
    {
        return "sha2('"+pass+"', 256)";
    }
    private String[] wrap(Map<String, String> map)
    {
        StringBuilder fields = new StringBuilder(), vals = new StringBuilder();

        String pass = map.remove("password");
        if (pass != null)
        {
            fields.append("password, ");
            vals.append(wrapPassword(pass)).append(",");
        }

        map.forEach((k,v) -> {
            fields.append(k).append(",");
            vals.append(wrapInQuotes(v)).append(",");
        });
        if (pass != null)
        {
            map.put("password", pass);
        }

        vals.deleteCharAt(vals.length() - 1);
        fields.deleteCharAt(fields.length() - 1);
        return new String[]{fields.toString(), vals.toString()};

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

}
