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

    private Registry registry;
    private static IRmiService instance;

    private RmiService(RemotePublisher publisher, int port, String name, String prop) throws RemoteException {
        this.registry = LocateRegistry.createRegistry(port);
        registry.rebind(name, publisher);
        publisher.registerProperty(prop);
    }

    @Autowired
    public static IRmiService instance(RemotePublisher publisher, int port, String name, String prop) throws RemoteException {
        if (instance == null) {
            instance = new RmiService(publisher, port, name, prop);
        }

        return instance;
    }

    @Override
    public void subscribe(String property, IRemotePropertyListener listener) throws RemoteException, NotBoundException {
        IRemotePublisherForListener publisher = (IRemotePublisherForListener) this.registry.lookup(Properties.get("rmi", "registerPublisher"));
        publisher.subscribeRemoteListener(listener, property);
    }

    @Override
    public void inform(String property, Object data) throws RemoteException, NotBoundException {
        RemotePublisher publisher = (RemotePublisher) this.registry.lookup(Properties.get("rmi", "registerPublisher"));
        publisher.inform(property, data, data);
    }
}
