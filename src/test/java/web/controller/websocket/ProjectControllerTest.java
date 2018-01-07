package web.controller.websocket;

import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import domain.IProjectManager;
import domain.Project;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import web.model.ClientChange;
import web.model.ProjectChange;
import web.model.StateChange;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {

    private SimpMessagingTemplate template;
    private IAuthenticationService service;
    private IProjectManager manager;
    private ProjectController controller;
    private Project project;
    private Client client;
    private ClientChange clientChange;

    @Before
    public void before() {
        template = mock(SimpMessagingTemplate.class);
        service = mock(IAuthenticationService.class);
        manager = mock(IProjectManager.class);
        controller = new ProjectController(template, service, manager);

        project = new Project("test");
        client = newClient();
        clientChange = new ClientChange(client.getId(), client.getAccount().getEmail(), project.getId());
    }

    private Client newClient() {
        return new Client(UUID.randomUUID(), new Account("test", "test", "test@test.nl", false));
    }

    //region onClosed
    @Test
    public void onClosed_projectHasNoMoreClients_projectIsUnloaded() {
        project.addClient(client);

        Mockito.doNothing().when(manager).unload(project);
        when(manager.find(project.getId())).thenReturn(project);

        controller.onClosed(clientChange);

        verify(manager).unload(project);
    }

    @Test
    public void onClosed_projectHasActiveClients_clientsAreNotified() {
        project.addClient(client);
        project.addClient(newClient());

        when(manager.find(project.getId())).thenReturn(project);

        controller.onClosed(clientChange);

        verify(manager).find(project.getId());
        verify(template).convertAndSend(anyString(), any(StateChange.class));
        verify(manager, never()).unload(project);
    }

    @Test
    public void onClosed_clientHasMoreThanZeroWindows_methodTerminates() {
        client.addWindow();
        client.addWindow();
        project.addClient(client);

        controller.onClosed(clientChange);

        verify(manager).find(project.getId());
        verify(template, never()).convertAndSend(anyString(), any(StateChange.class));
        verify(manager, never()).unload(project);
    }

    @Test
    public void onClosed_projectDoesNotExist_methodTerminates() {
        ClientChange change = new ClientChange(client.getId(), client.getAccount().getEmail(), project.getId());
        when(manager.find(project.getId())).thenReturn(null);

        controller.onClosed(change);

        verify(manager).find(project.getId());
        verify(template, never()).convertAndSend(anyString(), any(StateChange.class));
        verify(manager, never()).unload(project);
    }
    //endregion

    //region onOpened
    @Test
    public void onOpened_projectDidNotYetExist_createsNewProjectAndProvidesTheClientWithData() {
        InOrder inOrder = inOrder(template);
        when(manager.load(project.getId())).thenReturn(project);
        when(service.find(clientChange.getEmail())).thenReturn(client.getAccount());

        controller.onOpened(clientChange);

        verify(service).find(clientChange.getEmail());
        inOrder.verify(template).convertAndSend("/topic/project/onJoin/" + client.getId(), project);
        inOrder.verify(template, never()).convertAndSend(eq("/topic/project/onClientCountChange/" + project.getId()), any(StateChange.class));
    }

    @Test
    public void onOpened_projectExists_providesTheClientWithDataAndInformsOtherClients() {
        project.addClient(client);
        project.addClient(newClient());
        InOrder inOrder = inOrder(template);
        when(manager.load(project.getId())).thenReturn(project);

        controller.onOpened(clientChange);

        verify(service, never()).find(clientChange.getEmail());
        inOrder.verify(template).convertAndSend("/topic/project/onJoin/" + client.getId(), project);
        inOrder.verify(template).convertAndSend(eq("/topic/project/onClientCountChange/" + project.getId()), any(StateChange.class));
    }
    //endregion

    //region onChange
    @Test
    public void onChange_isCalled_informsAllClients() {
        ProjectChange change = new ProjectChange(project.getId(), project.getContent(), client.getId());
        when(manager.load(project.getId())).thenReturn(project);

        controller.onChange(change);

        verify(template).convertAndSend(eq("/topic/project/onchange/" + change.getId()), any(StateChange.class));
    }
    //endregion
}