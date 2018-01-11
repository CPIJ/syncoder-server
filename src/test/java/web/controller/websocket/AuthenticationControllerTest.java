package web.controller.websocket;

import data.service.IRmiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import web.model.StateChange;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuthenticationControllerTest {

    private SimpMessagingTemplate template;
    private IRmiService rmiService;
    private AuthenticationController controller;

    @Before
    public void before() throws RemoteException, NotBoundException {
        template = mock(SimpMessagingTemplate.class);
        rmiService = mock(IRmiService.class);
        controller = new AuthenticationController(template, rmiService);
    }

    //region constructor
    @Test
    public void constructor_whenValid_callsSubscribeMethod() throws RemoteException, NotBoundException {
        controller = new AuthenticationController(template, rmiService);

        verify(rmiService).subscribe(anyString(), eq(controller));
    }
    //endregion

    //region propertyChange
    @Test
    public void propertyChange_isCalled_sendTemplateMessage() throws RemoteException {
        controller.propertyChange(new PropertyChangeEvent(new Object(), "test", new StateChange(), new StateChange()));

        verify(template).convertAndSend(anyString(), any(StateChange.class));
    }
    //endregion

    @Test
    public void hashcode() {
        int test = controller.hashCode();

        assertEquals(test, controller.hashCode());
    }

}