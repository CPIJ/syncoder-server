package application;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Properties {

    private final java.util.Properties props;
    private final static String CS_KEY = "connectionStrings";

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
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
        }

        return "";
    }

    Connection getConnection(Db db) {
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
        String dbUrl = get(CS_KEY, "project_database");
        return getConnection(dbUrl);
    }

    Connection getAuthConnection() {
        String dbUrl = get(CS_KEY, "authentication_database");
        return getConnection(dbUrl);
    }

    private Connection getConnection(String dbUrl) {
        String user = get(CS_KEY, "username");
        String password = get(CS_KEY, "password");

        try {
            return DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
        }

        return null;
    }
}
