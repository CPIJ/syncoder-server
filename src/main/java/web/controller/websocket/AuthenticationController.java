package web.controller.websocket;

import application.Properties;
import data.service.IRmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rmi.fontys.IRemotePropertyListener;

import java.beans.PropertyChangeEvent;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Controller
public class AuthenticationController extends UnicastRemoteObject implements IRemotePropertyListener {

    private final SimpMessagingTemplate template;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate template, IRmiService rmiService) throws RemoteException {
        super();
        this.template = template;
        String property = new Properties(new java.util.Properties()).get("rmi", "registerProperty");
        rmiService.subscribe(property, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        template.convertAndSend("/topic/onRegister", propertyChangeEvent.getNewValue());
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}