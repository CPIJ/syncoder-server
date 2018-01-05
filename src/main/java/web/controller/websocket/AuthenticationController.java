package web.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rmi.fontys.IRemotePropertyListener;
import rmi.fontys.IRemotePublisherForListener;
import application.AppConfig;

import java.beans.PropertyChangeEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Controller
public class AuthenticationController extends UnicastRemoteObject implements IRemotePropertyListener {

    private SimpMessagingTemplate template;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate template) throws RemoteException, NotBoundException {
        this.template = template;

        configureRmi();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        template.convertAndSend("/topic/onRegister", propertyChangeEvent.getNewValue());
    }

    //region helper methods
    private void configureRmi() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(AppConfig.get("rmi", "port")));
        IRemotePublisherForListener publisher = (IRemotePublisherForListener) registry.lookup(AppConfig.get("rmi", "registerPublisher"));
        publisher.subscribeRemoteListener(this, AppConfig.get("rmi", "registerProperty"));
    }
    //endregion
}