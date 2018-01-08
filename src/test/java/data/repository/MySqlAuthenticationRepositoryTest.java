package data.repository;

import domain.Account;
import domain.Client;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MySqlAuthenticationRepositoryTest {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private MySqlAuthenticationRepository repository;

    @Before
    public void before() {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        repository = new MySqlAuthenticationRepository(connection);
    }

    //region login
    @Test
    public void login_passwordAndEmailCorrect_returnsClient() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(anyString())).thenReturn("test");
        when(resultSet.getBoolean("isAdmin")).thenReturn(true);

        Client client = repository.login("email", "password");

        assertNotNull(client);
        verify(statement).executeQuery();
        verify(resultSet).next();
        verify(resultSet, atLeast(1)).getString(anyString());
    }

    @Test
    public void login_passwordOrEmailIncorrect_returnsNull() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Client client = repository.login("email", "password");

        assertNull(client);
    }

    @Test
    public void login_sqlExceptionOccured_terminates() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        repository.login("email", "password");

        verify(connection).prepareStatement(anyString());
    }
    //endregion

    //region register
    //endregion



}