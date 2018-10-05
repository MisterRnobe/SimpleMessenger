package server.database;

import common.objects.DialogInfo;
import common.objects.Message;
import common.objects.User;
import common.objects.UserProfile;
import server.utils.FIleSaver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class ObjectsBuilder {
    private static final Map<String, TriConsumer<ResultSet, Integer, User>> userBuilder
            = new TreeMap<>();
    private static final Map<String, TriConsumer<ResultSet, Integer, DialogInfo>> dialogInfoBuilder = new TreeMap<>();
    private static final Map<String, TriConsumer<ResultSet, Integer, Message>> messageBuilder = new TreeMap<>();

    static {
        userBuilder.put("login", (rs, i, user)-> user.setLogin(rs.getString(i)));
        userBuilder.put("name", (rs, i, user)-> user.setName(rs.getString(i)));
        userBuilder.put("last_online", (rs, i, user)-> user.setLastOnline(rs.getLong(i)));
        userBuilder.put("online", (rs, i, user)-> user.setOnline(rs.getBoolean(i)));
        userBuilder.put("has_avatar",(rs,i,user)->
        {
            if (rs.getBoolean(i))
            {
                user.setAvatar(FIleSaver.loadAvatarFor(user.getLogin()));
            }
        });

        dialogInfoBuilder.put("dialog_id", (rs,i,di)->di.setDialogId(rs.getInt(i)));
        dialogInfoBuilder.put("creator", (rs,i,di)->di.setCreator(rs.getString(i)));
        dialogInfoBuilder.put("dialog_name", (rs,i,di)->di.setDialogName(rs.getString(i)));
        dialogInfoBuilder.put("type", (rs,i,di)->di.setType(rs.getInt(i)));

        messageBuilder.put("dialog_id", (rs,i,m)->m.setDialogId(rs.getInt(i)));
        messageBuilder.put("text", (rs,i,m)->m.setText(rs.getString(i)));
        messageBuilder.put("time", (rs,i,m)->m.setTime(rs.getLong(i)));
        messageBuilder.put("message_id", (rs,i,m)->m.setMessageId(rs.getInt(i)));
        messageBuilder.put("is_system", (rs,i,m)->m.setIsSystem(rs.getBoolean(i)));
        messageBuilder.put("sender", (rs,i,m)->m.setSender(rs.getString(i)));

    }
    public static User buildUser(ResultSet resultSet) throws SQLException {
        return build(resultSet, userBuilder, new User());
    }
    public static DialogInfo buildDialogInfo(ResultSet resultSet) throws SQLException
    {
        return build(resultSet, dialogInfoBuilder, new DialogInfo());
    }
    public static Message buildMessage(ResultSet resultSet) throws SQLException
    {
        return build(resultSet, messageBuilder, new Message());
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
