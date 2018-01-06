package web.controller.websocket;

import data.service.IRmiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rmi.fontys.IRemotePropertyListener;
import rmi.fontys.IRemotePublisherForListener;
import application.Properties;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Controller
public class AuthenticationController extends UnicastRemoteObject implements IRemotePropertyListener {

    private final SimpMessagingTemplate template;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate template, IRmiService rmiService, @Qualifier("property") String property) throws RemoteException, NotBoundException {
        this.template = template;
        rmiService.subscribe(property, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        template.convertAndSend("/topic/onRegister", propertyChangeEvent.getNewValue());
    }
}