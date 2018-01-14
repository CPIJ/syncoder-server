package data.service;

import application.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmi.fontys.IRemotePropertyListener;
import rmi.fontys.IRemotePublisherForListener;
import rmi.fontys.RemotePublisher;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RmiService implements IRmiService {

    private final Registry registry;
    private static final String PUBLISHER = "registerProperty";
    private static final String FROM = "rmi";

    @Autowired
    public RmiService(RemotePublisher publisher, Registry registry) throws RemoteException {
        String name = new Properties().get(FROM, PUBLISHER);
        String property = new Properties().get(FROM, PUBLISHER);

        this.registry = registry;

        registry.rebind(name, publisher);
        publisher.registerProperty(property);
    }

    public RmiService(RemotePublisher publisher) throws RemoteException {
        String name = new Properties().get(FROM, PUBLISHER);
        String property = new Properties().get(FROM, PUBLISHER);
        int port = Integer.parseInt(new Properties().get(FROM, "port"));

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, publisher);
        publisher.registerProperty(property);
    }

    @Override
    public boolean subscribe(String property, IRemotePropertyListener listener) {
        try {
            IRemotePublisherForListener publisher = (IRemotePublisherForListener) this.registry.lookup(new Properties().get(FROM, PUBLISHER));
            publisher.subscribeRemoteListener(listener, property);

            return true;
        } catch (RemoteException | NotBoundException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "an exception was thrown", e);
            return false;
        }
    }

    @Override
    public boolean inform(String property, Object data) {
        try {
            RemotePublisher publisher = (RemotePublisher) this.registry.lookup(new Properties().get(FROM, PUBLISHER));
            publisher.inform(property, data, data);

            return true;
        } catch (RemoteException | NotBoundException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "an exception was thrown", e);
            return false;
        }
    }
}
