package data.service;

import org.junit.Before;
import org.junit.Test;
import rmi.fontys.IPropertyListener;
import rmi.fontys.IRemotePropertyListener;
import rmi.fontys.IRemotePublisherForListener;
import rmi.fontys.RemotePublisher;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RmiServiceTest {

    private Registry registry;
    private RemotePublisher publisher;
    private RmiService service;

    @Before
    public void before() throws RemoteException {
        registry = mock(Registry.class);
        publisher = mock(RemotePublisher.class);

        service = new RmiService(publisher, registry);
    }

    @Test
    public void subscribe_isSuccessful_returnsTrue() throws RemoteException, NotBoundException {
        when(registry.lookup(anyString())).thenReturn(publisher);

        Boolean result = service.subscribe("testProp", mock(IRemotePropertyListener.class));

        verify(publisher).subscribeRemoteListener(any(), anyString());
        assertTrue(result);
    }

    @Test
    public void subscribe_isNotSuccessful_returnsFalse() throws RemoteException, NotBoundException {
        when(registry.lookup(anyString())).thenThrow(NotBoundException.class);

        Boolean result = service.subscribe("testProp", mock(IRemotePropertyListener.class));

        verify(publisher, never()).subscribeRemoteListener(any(), anyString());
        assertFalse(result);
    }

    @Test
    public void inform_informedSuccessful_returnsTrue() throws RemoteException, NotBoundException {
        when(registry.lookup(anyString())).thenReturn(publisher);

        Boolean result = service.inform("testProp", mock(IRemotePropertyListener.class));

        verify(publisher).inform(anyString(), any(), any());
        assertTrue(result);
    }

    @Test
    public void inform_informUnsuccessful_returnsFalse() throws RemoteException, NotBoundException {
        when(registry.lookup(anyString())).thenThrow(NotBoundException.class);

        Boolean result = service.inform("testProp", mock(IRemotePropertyListener.class));

        verify(publisher, never()).inform(anyString(), any(), any());
        assertFalse(result);
    }
}