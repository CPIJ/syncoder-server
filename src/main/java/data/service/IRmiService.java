package data.service;

import rmi.fontys.IRemotePropertyListener;

public interface IRmiService {
    boolean subscribe(String property, IRemotePropertyListener listener);
    boolean inform(String property, Object data);
}

