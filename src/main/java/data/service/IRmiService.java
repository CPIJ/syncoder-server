package data.service;

import rmi.fontys.IRemotePropertyListener;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public interface IRmiService {
    boolean subscribe(String property, IRemotePropertyListener listener);
    boolean inform(String property, Object data);
}

