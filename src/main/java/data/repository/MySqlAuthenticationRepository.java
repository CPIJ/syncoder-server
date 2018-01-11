package data.repository;

import application.Properties;
import domain.Account;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class MySqlAuthenticationRepository implements IAuthenticationRepository {

    private Connection connection;
    private static final String USERNAME = "username";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String IS_ADMIN = "isAdmin";

    @Autowired
    public MySqlAuthenticationRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client login(String email, String password) {
        String query = "SELECT * FROM account WHERE email = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return new Client(
                            set.getString(USERNAME),
                            set.getString(PASSWORD),
                            set.getString(EMAIL),
                            set.getBoolean(IS_ADMIN)
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return null;
    }

    @Override
    public boolean register(Account account) {
        String query = "INSERT INTO account (username, password, email, isAdmin) VALUES(?,?,?,?)";
        int affectedRows;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getEmail());
            statement.setBoolean(4, account.getIsAdmin());

            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
            return false;
        }

        return affectedRows > 0;
    }

    @Override
    public List<Account> getAllAccounts() {
        String query = "SELECT * FROM account";
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    accounts.add(new Account(
                            set.getString(USERNAME),
                            set.getString(PASSWORD),
                            set.getString(EMAIL),
                            set.getBoolean(IS_ADMIN)));
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return accounts;
    }

    @Override
    public Account find(String email) {
        String query = "SELECT * FROM account WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try(ResultSet set = statement.executeQuery()) {

                if (set.next()) {
                    return new Account(
                            set.getString(USERNAME),
                            set.getString(PASSWORD),
                            set.getString(EMAIL),
                            set.getBoolean(IS_ADMIN));
                }
            }
        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, Properties.ERROR_MESSAGE, e);
        }

        return null;
    }
}
