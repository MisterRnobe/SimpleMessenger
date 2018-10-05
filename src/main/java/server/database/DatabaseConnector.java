package server.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DatabaseConnector {
    void update(String field, String id, Map<String, String> values, String table) throws SQLException;
    int insert(String table, Map<String, String> values) throws SQLException;
    <E> E select(String query, Function<ResultSet, E> consumer, String... values) throws SQLException;
    void doQuery(String query) throws SQLException;

}
