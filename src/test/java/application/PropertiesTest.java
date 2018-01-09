package application;

import com.mysql.cj.core.exceptions.WrongArgumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertiesTest {

    private java.util.Properties props;
    private Properties properties;

    @Before
    public void before() {
        props = mock(java.util.Properties.class);
        properties = new Properties(props);
    }

    @Test
    public void get_validLocationGiven_returnsString() throws IOException {
        doNothing().when(props).load(any(InputStream.class));
        when(props.getProperty(anyString())).thenReturn("test");

        String result = properties.get("test", "test");

        assertEquals("test", result);
    }

    @Test
    public void get_invalidLocationGiven_returnsEmptyString() throws IOException {
        doThrow(IOException.class).when(props).load(any(InputStream.class));

        String result = properties.get("test", "test");

        assertNull(result);
    }

    @Test
    public void getConnection_forAuthentication_returnsCorrectConnection() {
        Properties propSpy = spy(Properties.class);
        when(propSpy.getConnection(Properties.Db.AUTHENTICATION)).thenReturn(mock(Connection.class));

        propSpy.getConnection(Properties.Db.AUTHENTICATION);

        verify(propSpy).getAuthConnection();
    }

    @Test
    public void getConnection_forProject_returnsCorrectConnection() {
        Properties propSpy = spy(Properties.class);
        when(propSpy.getConnection(Properties.Db.PROJECT)).thenReturn(mock(Connection.class));

        propSpy.getConnection(Properties.Db.PROJECT);

        verify(propSpy).getProjectConnection();
    }
}