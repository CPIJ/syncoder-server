package domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientTest {

    private Client client;

    @Before
    public void before() {
        client = new Client("test", "test", "test", false);
    }

    @Test
    public void hashcode_isCalled_isSameAsId() {
        assertEquals(client.hashCode(), client.getId().hashCode());
    }

    @Test
    public void removeWindow_moreThanOneWindowOpen_windowIsDecreasedByOne() {
        client.addWindow();
        client.removeWindow();

        assertEquals(0, client.getOpenWindows());
    }

}