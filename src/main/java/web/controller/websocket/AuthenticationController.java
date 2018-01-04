package web.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rmi.fontys.IRemotePropertyListener;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;

@Controller
public class AuthenticationController implements IRemotePropertyListener {
    private final SimpMessagingTemplate template;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        template.convertAndSend("/topic/onRegister", propertyChangeEvent.getNewValue());
    }
}
