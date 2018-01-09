package data.repository;

import application.Properties;
import domain.Project;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MySqlProjectRepositoryTest {

    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private MySqlProjectRepository repository;

    @Before
    public void before() {
        connection = mock(Connection.class);
        statement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        repository = new MySqlProjectRepository(connection);
    }

    @Test
    public void insert_isValid_returnsTrue() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        Boolean result = repository.insert(new Project());

        assertTrue(result);
    }

    @Test
    public void insert_notValid_returnsFalse() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        Boolean result = repository.insert(new Project());

        assertFalse(result);
    }

    @Test
    public void update_isValid_returnsTrue() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        Boolean result = repository.update(new Project());

        assertTrue(result);
    }

    @Test
    public void update_isNotValid_returnsFalse() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        Boolean result = repository.update(new Project());

        assertFalse(result);
    }

    @Test
    public void find_givenValidId_returnsTheProject() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        Project project = repository.find("test");

        assertNotNull(project);
    }

    @Test
    public void find_givenInvalidId_returnsNull() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Project project = repository.find("test");

        assertNull(project);
    }

    @Test
    public void getAll_hasRows_returnsList() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, true, false);

        List<Project> projects = repository.getAll();

        assertTrue(projects.size() > 0);
    }

    @Test
    public void getAll_hasNoRows_returnsEmptyList() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Project> projects = repository.getAll();

        assertTrue(projects.size() == 0);
    }
}