package data.repository;

import domain.Account;
import domain.Client;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
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
    public void login_salExceptionOccurred_terminates() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        repository.login("email", "password");

        verify(connection).prepareStatement(anyString());
    }
    //endregion

    //region register
    @Test
    public void register_AccountDoesNotYetExists_returnsTrue() throws SQLException {
        Account account = new Account("test", "test", "test", false);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        Boolean result = repository.register(account);

        assertTrue(result);
    }

    @Test
    public void register_AccountDoesExist_returnsFalse() throws SQLException {
        Account account = new Account("test", "test", "test", false);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(SQLIntegrityConstraintViolationException.class);

        Boolean result = repository.register(account);

        assertFalse(result);
    }

    @Test()
    public void register_cannotCreateQuery_throwsErrorReturnsFalse() throws SQLException {
        Account account = new Account("test", "test", "test", false);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        Boolean result = repository.register(account);

        assertFalse(result);
    }
    //endregion

    //region getAllAccounts
    @Test
    public void getAll_isCalled_listOfAccountsIsReturned() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(anyString())).thenReturn("test");
        when(resultSet.getBoolean(any())).thenReturn(false);

        List<Account> accounts = repository.getAllAccounts();

        assertNotNull(accounts);
    }

    @Test
    public void getAll_isCalledAndThrowsError_emptyListIsReturned() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(SQLException.class);

        List<Account> accounts = repository.getAllAccounts();

        assertNotNull(accounts);
    }
    //endregion

    //region find
    @Test
    public void find_accountWasFound_itIsReturned() throws SQLException {
        Account account = new Account("test", "test", "test", false);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getString(anyString())).thenReturn("test");
        when(resultSet.getBoolean(any())).thenReturn(false);
        when(resultSet.next()).thenReturn(true);

        Account found = repository.find(account.getEmail());

        assertEquals(account.getEmail(), found.getEmail());
    }

    @Test
    public void find_accountCouldNotBeFound_returnsNull() throws SQLException {
        Account account = new Account("test", "test", "test", false);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Account found = repository.find(account.getEmail());

        assertNull(found);
    }

    @Test
    public void find_errorWasThrown_returnsNull() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(SQLException.class);

        Account found = repository.find("test");

        assertNull(found);
    }
    //endregion


}