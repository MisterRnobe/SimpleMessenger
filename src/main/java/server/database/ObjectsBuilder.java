package server.database;

import common.objects.*;
import server.utils.FileSaver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ObjectsBuilder {
    private static final Map<String, TriConsumer<ResultSet, Integer, User>> userBuilder
            = new TreeMap<>();
    private static final Map<String, TriConsumer<ResultSet, Integer, GroupInfo>> groupInfoBuilder = new TreeMap<>();
    private static final Map<String, TriConsumer<ResultSet, Integer, Message>> messageBuilder = new TreeMap<>();
    private static final Map<String, TriConsumer<ResultSet, Integer, UserProfile>> profileBuilder = new TreeMap<>();

    static {
        userBuilder.put("login", (rs, i, user)-> user.setLogin(rs.getString(i)));
        userBuilder.put("name", (rs, i, user)-> user.setName(rs.getString(i)));
        userBuilder.put("last_online", (rs, i, user)-> user.setLastOnline(rs.getLong(i)));
        userBuilder.put("online", (rs, i, user)-> user.setIsOnline(rs.getBoolean(i)));
        userBuilder.put("has_avatar",(rs,i,user)->
        {
            if (rs.getBoolean(i))
                user.setAvatarPath(FileSaver.getPathForUserAvatar(user.getLogin()));
        });

        groupInfoBuilder.put("dialog_id", (rs,i,di)->di.setDialogId(rs.getInt(i)));
        groupInfoBuilder.put("creator", (rs,i,di)->di.setCreator(rs.getString(i)));
        groupInfoBuilder.put("dialog_name", (rs,i,di)->di.setGroupName(rs.getString(i)));
        groupInfoBuilder.put("type", (rs,i,di)->di.setType(rs.getInt(i)));
        groupInfoBuilder.put("user_count", (rs,i,di)->di.setUsersCount(rs.getInt(i)));
        groupInfoBuilder.put("has_photo", (rs,i,di)->{
            if (rs.getBoolean(i))
                di.setAvatarPath(FileSaver.getPathForGroupAvatar(di.getDialogId()));
        });

        messageBuilder.put("dialog_id", (rs,i,m)->m.setDialogId(rs.getInt(i)));
        messageBuilder.put("text", (rs,i,m)->m.setText(rs.getString(i)));
        messageBuilder.put("time", (rs,i,m)->m.setTime(rs.getLong(i)));
        messageBuilder.put("message_id", (rs,i,m)->m.setMessageId(rs.getInt(i)));
        messageBuilder.put("is_system", (rs,i,m)->m.setIsSystem(rs.getBoolean(i)));
        messageBuilder.put("sender", (rs,i,m)->m.setSender(rs.getString(i)));

        profileBuilder.put("login", (rs, i, profile)-> profile.setLogin(rs.getString(i)));
        profileBuilder.put("name", (rs, i, profile)-> profile.setName(rs.getString(i)));
        profileBuilder.put("email", (rs, i, profile)-> profile.setEmail(rs.getString(i)));
        profileBuilder.put("info", (rs, i, profile)-> profile.setInfo(rs.getString(i)));
        profileBuilder.put("has_avatar",(rs,i,profile)->
        {
            if (rs.getBoolean(i))
            {
                profile.setAvatarPath(FileSaver.getPathForUserAvatar(profile.getLogin()));
            }
        });

    }
    static GroupInfo buildGroupInfo(ResultSet resultSet) throws SQLException
    {
        return build(resultSet, groupInfoBuilder, new GroupInfo());
    }
    public static User buildUser(ResultSet resultSet) throws SQLException {
        return build(resultSet, userBuilder, new User());
    }
    public static Message buildMessage(ResultSet resultSet) throws SQLException
    {
        return build(resultSet, messageBuilder, new Message());
    }
    public static UserProfile buildProfile(ResultSet resultSet) throws SQLException
    {
        return build(resultSet, profileBuilder, new UserProfile());
    }
    private static<E> E build(ResultSet resultSet, Map<String, TriConsumer<ResultSet, Integer, E>> map, E object) throws SQLException
    {
        ResultSetMetaData md = resultSet.getMetaData();
        int columns = md.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            TriConsumer<ResultSet, Integer, E> consumer = map.get(md.getColumnName(i));
            if (consumer != null)
                consumer.consume(resultSet, i, object);
        }
        return object;

    }

    @FunctionalInterface
    interface TriConsumer<E,V,T>
    {
        void consume(E e, V v, T t) throws SQLException;
    }
}
