package server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    public static void init()
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
    public void addUser(Map<String, String> values)
    {
        if (values.size() != 5)
            throw new RuntimeException("Wrong number of user's fields.");
        insert(values, "users");
    }
    public void setToken(String login, String token)
    {
        insert(new TreeMap<String, String>(){{
            this.put("login", login);
            this.put("token", token);
        }}, "tokens");
    }
    private void insert(Map<String, String> map, String table)
    {
        try{
            Statement statement = connection.createStatement();

            String[] wrapped = wrap(map);

            String query = "INSERT INTO "+table+" ("+wrapped[0]+") VALUES ("+wrapped[1]+");";
            System.out.println(query);
            statement.executeUpdate(query);
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //True, if exists
    public boolean checkExistence(Map<String, String> values, String table)
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

    public boolean checkUserExistence(String field, String value)
    {
        return checkExistence(new TreeMap<String, String>(){
            {this.put(field, value);}
        }, "users");
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

}
