package data.repository;

import application.Properties;
import domain.Account;
import domain.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlAuthenticationRepository implements IAuthenticationRepository {

    private Connection connection;

    public MySqlAuthenticationRepository() {
        connection = Properties.getConnection(Properties.Db.AUTHENTICATION);

        if (connection == null) {
            throw new IllegalArgumentException("No database found!");
        }
    }

    @Override
    public Client login(String email, String password) {
        String query = "SELECT * FROM account WHERE email = ? AND password = ?";

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
    public boolean register(Account account) {
        if (find(account.getEmail()) != null) return false;

        String query = "INSERT INTO account (username, password, email) VALUES(?,?,?)";
        int affectedRows = -1;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setString(3, account.getEmail());

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

    @Override
    public Account find(String email) {
        String query = "SELECT * FROM account WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            ResultSet set = statement.executeQuery();

            if (set.next()) {
                return new Account(
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
}
