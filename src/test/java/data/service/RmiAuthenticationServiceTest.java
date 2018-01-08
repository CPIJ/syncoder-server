package data.service;

import data.repository.IAuthenticationRepository;
import domain.Account;
import domain.Client;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RmiAuthenticationServiceTest {

    private IAuthenticationRepository repository;
    private IRmiService rmiService;
    private RmiAuthenticationService service;
    private Account account;

    @Before
    public void before() {
        repository = mock(IAuthenticationRepository.class);
        rmiService = mock(IRmiService.class);
        service = new RmiAuthenticationService(repository, rmiService);

        account = new Account("test", "test", "test", false);
    }

    @Test
    public void login_isCalled_isPassedToRepository() {
        service.login(anyString(), anyString());

        verify(repository).login(anyString(), anyString());
    }

    @Test
    public void register_accountWasRegisteredSuccessfully_subscribersAreNotified() {
        when(repository.register(account)).thenReturn(true);

        service.register(account);

        verify(rmiService).inform(anyString(), any());
    }

    @Test
    public void register_accountCouldNotBeRegistered_methodTerminates() {
        when(repository.register(account)).thenReturn(false);

        service.register(account);

        verify(rmiService, never()).inform(anyString(), any());
    }

    @Test
    public void find_validEmail_accountIsReturned() {
        when(repository.find("test")).thenReturn(account);

        Account found = service.find("test");

        verify(repository).find("test");
        assertEquals(account, found);
    }

    @Test
    public void isAuthorized_userIsAuthorized_returnsTrue() {
        Client client = new Client(UUID.randomUUID(), account);
        when(repository.find(account.getEmail())).thenReturn(account);

        Boolean result = service.isAuthorized(client);

        assertTrue(result);
    }

    @Test
    public void isAuthorized_userIsNotAuthorized_returnsFalse() {
        Client client = new Client(UUID.randomUUID(), new Account("", "", "", true));
        when(repository.find(account.getEmail())).thenReturn(account);

        Boolean result = service.isAuthorized(client);

        assertFalse(result);
    }

    @Test
    public void isAuthorized_userIsNotFound_returnsFalse() {
        Client client = new Client(UUID.randomUUID(), account);
        when(repository.find(account.getEmail())).thenReturn(null);

        Boolean result = service.isAuthorized(client);

        assertFalse(result);
    }
}