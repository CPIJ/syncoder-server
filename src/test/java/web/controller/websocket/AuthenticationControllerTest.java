package web.controller.websocket;

import data.service.IRmiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AuthenticationControllerTest {

    private SimpMessagingTemplate template;
    private IRmiService rmiService;
    private String property = "testProperty";
    private AuthenticationController controller;

    @Before
    public void before() throws RemoteException, NotBoundException {
        template = mock(SimpMessagingTemplate.class);
        rmiService = mock(IRmiService.class);
        controller = new AuthenticationController(template, rmiService, property);
    }

    //region constructor
    @Test
    public void constructor_whenValid_callsSubscribeMethod() throws RemoteException, NotBoundException {
        controller = new AuthenticationController(template, rmiService, property);

        verify(rmiService).subscribe(property, controller);
    }
    //endregion

    //region propertyChange
    @Test
    public void propertyChange_isCalled_sendTemplateMessage() throws RemoteException {
        Object result = new Object();
        String destination = "/topic/onRegister";
        Mockito.doNothing().when(template).convertAndSend(destination, result);

        controller.propertyChange(new PropertyChangeEvent(result, property, result, result));

        verify(template).convertAndSend(destination, result);
    }
    //endregion

}