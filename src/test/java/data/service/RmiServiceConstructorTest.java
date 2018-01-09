package data.service;

import org.junit.Before;
import org.junit.Test;
import rmi.fontys.RemotePublisher;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RmiServiceConstructorTest {

    private Registry registry;
    private RemotePublisher publisher;
    private RmiService service;

    @Before
    public void before() throws RemoteException {
        registry = mock(Registry.class);
        publisher = mock(RemotePublisher.class);
    }

    @Test
    public void constructor_isCalled_rebindIsCalled() throws RemoteException {
        service = new RmiService(publisher, registry);

        verify(registry).rebind(anyString(), eq(publisher));
    }

    @Test
    public void constructor_isCalled_registerPropertyIsCalled() throws RemoteException {
        service = new RmiService(publisher, registry);

        verify(publisher).registerProperty(anyString());
    }
}