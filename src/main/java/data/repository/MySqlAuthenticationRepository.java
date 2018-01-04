package data.repository;

import config.Properties;
import domain.Account;
import domain.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlAuthenticationRepository implements AuthenticationRepository {

    private Connection connection;

    public MySqlAuthenticationRepository() {
        connection = Properties.getConnection(Properties.Db.AUTHENTICATION);

        if (connection == null) {
            throw new IllegalArgumentException("No database found!");
        }
    }

    @Override
    public Client login(String email, String password) {
        String query = "SELECT * FROM account WHERE email LIKE ? AND password LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new Client(
                        set.getString("username"),
                        set.getString("password"),
                        set.getString("email")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean register(Client client) {
        if (getAllAccounts().stream().anyMatch(a -> a.getEmail().equals(client.getAccount().getEmail()))) {
            return false;
        }

        String query = "INSERT INTO account (username, password, email) VALUES(?,?,?)";
        int affectedRows = -1;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getAccount().getUsername());
            statement.setString(2, client.getAccount().getPassword());
            statement.setString(3, client.getAccount().getEmail());

            affectedRows = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return affectedRows != -1;
    }

    @Override
    public List<Account> getAllAccounts() {
        String query = "SELECT * FROM account";
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                accounts.add(new Account(
                        set.getString("username"),
                        set.getString("password"),
                        set.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }
}
