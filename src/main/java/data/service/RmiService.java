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

@Service
public class RmiService implements IRmiService {

    private final Registry registry;

    @Autowired
    public RmiService(RemotePublisher publisher, Registry registry) throws RemoteException {
        String name = new Properties().get("rmi", "registerPublisher");
        String property = new Properties().get("rmi", "registerProperty");

        this.registry = registry;

        registry.rebind(name, publisher);
        publisher.registerProperty(property);
    }

    public RmiService(RemotePublisher publisher) throws RemoteException {
        String name = new Properties().get("rmi", "registerPublisher");
        String property = new Properties().get("rmi", "registerProperty");
        int port = Integer.parseInt(new Properties().get("rmi", "port"));

        registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, publisher);
        publisher.registerProperty(property);
    }

    @Override
    public boolean subscribe(String property, IRemotePropertyListener listener) {
        try {
            IRemotePublisherForListener publisher = (IRemotePublisherForListener) this.registry.lookup(new Properties().get("rmi", "registerPublisher"));
            publisher.subscribeRemoteListener(listener, property);

            return true;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean inform(String property, Object data) {
        try {
            RemotePublisher publisher = (RemotePublisher) this.registry.lookup(new Properties().get("rmi", "registerPublisher"));
            publisher.inform(property, data, data);

            return true;
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
