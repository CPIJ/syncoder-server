package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class Properties {

    private final java.util.Properties props;

    @Autowired
    public Properties(java.util.Properties props) {
        this.props = props;
    }

    public Properties() {
        props = new java.util.Properties();
    }

    public enum Db {
        AUTHENTICATION,
        PROJECT
    }

    public String get(String from, String key) {
        InputStream inputStream = Properties.class.getResourceAsStream("/" + from + ".properties");

        try {
            props.load(inputStream);
            return props.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public Connection getConnection(Db db) {
        switch (db) {
            case AUTHENTICATION:
                return getAuthConnection();
            case PROJECT:
                return getProjectConnection();
            default:
                return null;
        }
    }

    Connection getProjectConnection() {
        String dbUrl = get("connectionStrings", "project_database");
        return getConnection(dbUrl);
    }

    Connection getAuthConnection() {
        String dbUrl = get("connectionStrings", "authentication_database");
        return getConnection(dbUrl);
    }

    Connection getConnection(String dbUrl) {
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
