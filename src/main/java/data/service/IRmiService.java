package data.service;

import domain.Account;
import rmi.fontys.IRemotePropertyListener;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public interface IRmiService {
    void subscribe(String property, IRemotePropertyListener listener) throws RemoteException, NotBoundException;
    void inform(String property, Object data) throws RemoteException, NotBoundException;
}

