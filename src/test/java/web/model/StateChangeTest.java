package web.model;

import domain.Client;
import domain.Project;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class StateChangeTest {
    private StateChange change;
    private Project project;
    private Client client;

    @Before
    public void before() {
        project = new Project();
        client = new Client(UUID.randomUUID(), null);
        change = new StateChange(project, client);
    }


    @Test
    public void getProject() throws Exception {
        assertEquals(project, change.getProject());
    }

    @Test
    public void getSender() throws Exception {
        assertEquals(client, change.getSender());
    }

}