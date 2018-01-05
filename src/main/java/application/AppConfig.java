package application;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppConfig {

    public enum Db {
        AUTHENTICATION,
        PROJECT
    }

    public static String get(String from, String key) {
        InputStream inputStream = AppConfig.class.getResourceAsStream("/" + from + ".properties");
        java.util.Properties props = new java.util.Properties();

        try {
            props.load(inputStream);
            return props.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Connection getConnection(Db db) {
        switch (db) {
            case AUTHENTICATION:
                return getAuthConnection();
            case PROJECT:
                return getProjectConnection();
            default:
                return null;
        }
    }

    private static Connection getProjectConnection() {
        String dbUrl = get("connectionStrings", "project_database");
        return getConnection(dbUrl);
    }

    private static Connection getAuthConnection() {
        String dbUrl = get("connectionStrings", "authentication_database");
        return getConnection(dbUrl);
    }

    private static Connection getConnection(String dbUrl) {
        String user = get("connectionStrings", "username");
        String password = get("connectionStrings", "password");

        try {
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
