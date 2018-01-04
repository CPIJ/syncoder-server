package web.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import rmi.fontys.IRemotePropertyListener;
import rmi.fontys.IRemotePublisherForListener;
import util.Config;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@Controller
public class AuthenticationController extends UnicastRemoteObject implements IRemotePropertyListener  {

    private SimpMessagingTemplate template;

    @Autowired
    public AuthenticationController(SimpMessagingTemplate template) throws RemoteException, NotBoundException {
        this.template = template;

        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(Config.get("rmi", "port")));
        IRemotePublisherForListener publisher = (IRemotePublisherForListener) registry.lookup(Config.get("rmi", "registerPublisher"));

        try {
            publisher.subscribeRemoteListener(this, Config.get("rmi", "registerProperty"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) throws RemoteException {
        template.convertAndSend("/topic/onRegister", propertyChangeEvent.getNewValue());
    }
}