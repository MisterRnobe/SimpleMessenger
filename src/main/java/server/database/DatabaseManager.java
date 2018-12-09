package server.database;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseExtractor extractorInterface;
    private static void init()
    {
        String url = "jdbc:mariadb://nikitamedvedev.ddns.net:3306/simple_messenger";
        String login = "nikita";
        String pass = "pass";
        try {
            DatabaseConnector connectorInterface = new MySQLConnector(DriverManager.getConnection(url, login, pass));
            extractorInterface = new MySQLExtractor(connectorInterface);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static DatabaseExtractor getExtractor()
    {
        if (extractorInterface == null)
            init();
        return extractorInterface;
    }
}
