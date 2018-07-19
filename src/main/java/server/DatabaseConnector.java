package server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import common.objects.Dialog;
import common.objects.DialogInfo;
import common.objects.DialogList;
import common.objects.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;
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

    public int createDialog(String creator, String partner)
    {
        int dialogId = insert(new TreeMap<String, String>(){{
            this.put("creator", creator);
            this.put("type", "0");
        }}, "dialogs");
        if (dialogId == -1)
            return dialogId;
        insert(new TreeMap<String, String>(){{
            this.put("login", partner);
            this.put("dialog_id", Integer.toString(dialogId));
        }}, "user_dialog");
        insert(new TreeMap<String, String>(){{
            this.put("login", creator);
            this.put("dialog_id", Integer.toString(dialogId));
        }}, "user_dialog");
        return dialogId;
    }
    public DialogList getDialogs(String login, String count)
    {
        DialogList dialogList = new DialogList();
        try(Statement statement = connection.createStatement())
        {
            String query = "SELECT d.dialog_id, d.dialog_name, d.creator, d.last_message_id, m.sender, m.text, m.time from user_dialog AS u_d LEFT JOIN dialogs AS d on u_d.dialog_id=d.dialog_id LEFT JOIN messages as m ON d.last_message_id = m.message_id where login = "+wrapInQuotes(login)+" LIMIT "+count+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                while(resultSet.next())
                {
                    DialogInfo info = new DialogInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                    Message m = new Message(resultSet.getInt(4), resultSet.getInt(1), resultSet.getString(5),
                            resultSet.getString(6), resultSet.getLong(7));
                    info.setLastMessage(m);
                    dialogList.addDialog(info);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return dialogList;
    }
    private int insert(Map<String, String> map, String table)
    {
        try{
//            Statement statement = connection.createStatement();
//
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
    public Dialog getDialogById(int id)
    {
        return null;
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
    public void setLastMessage(String dialogId, String messageId)
    {
        update("dialog_id", dialogId, CustomMap.create().add("last_message_id", messageId), "dialogs");
    }
    public int addMessage(String dialogId, String sender, String text, String time)
    {
        return insert(CustomMap.create()
                .add("sender", sender)
                .add("dialog_id", dialogId)
                .add("text",text)
                .add("time",time),"messages");
    }

    public boolean checkUserExistence(String field, String value)
    {
        return checkExistence(CustomMap.create().add(field, value), "users");
    }
    private String wrapInQuotes(String str)
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
    public List<String> getUsersInDialog(String dialogId)
    {
        try (Statement statement = connection.createStatement())
        {
            String query = "SELECT login from user_dialog WHERE dialog_id = "+wrapInQuotes(dialogId)+";";
            System.out.println(query);
            try(ResultSet resultSet = statement.executeQuery(query))
            {
                List<String> users = new LinkedList<>();
                while (resultSet.next())
                {
                    users.add(resultSet.getString(1));
                }
                return users;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
