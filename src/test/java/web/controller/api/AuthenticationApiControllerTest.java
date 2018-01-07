package web.controller.api;

import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import web.model.LoginRequest;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationApiControllerTest {

    private IAuthenticationService service;
    private AuthenticationApiController controller;
    private ResponseEntity response;

    @Before
    public void before() throws RemoteException {
        service = mock(IAuthenticationService.class);
        controller = new AuthenticationApiController(service);
    }

    //region login
    @Test
    public void login_ValidEmailAndPassword_ReturnsOk() {
        when(service.login(anyString(), anyString())).thenReturn(new Client());
        response = controller.login(new LoginRequest("", ""));

        verify(service).login(anyString(), anyString());
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void login_NoUserFound_ReturnsBadRequest() {
        when(service.login(anyString(), anyString())).thenReturn(null);
        response = controller.login(new LoginRequest("", ""));

        verify(service).login(anyString(), anyString());
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    public void login_NoEmailSupplied_ReturnsBadRequest() {
        response = controller.login(new LoginRequest(null, ""));

        verify(service, never()).login(null,"");
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    public void login_noPasswordSupplied_ReturnsBadRequest() {
        response = controller.login(new LoginRequest("", null));

        verify(service, never()).login("",null);
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }
    //endregion

    //region isAuthorized
    @Test
    public void isAuthorized_validClient_ReturnsOk() {
        when(service.isAuthorized(any())).thenReturn(true);
        response = controller.isAuthorized(new Client());

        verify(service).isAuthorized(any());
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void isAuthorized_noClientGiven_returnsBadRequest() {
        response = controller.isAuthorized(null);

        verify(service, never()).isAuthorized(null);
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }
    //endregion

    //region forgotPassword
    @Test
    public void forgotPassword_validEmail_ReturnsOk() {
        when(service.find(anyString())).thenReturn(new Account());
        response = controller.forgotPassword("");

        verify(service).find(anyString());
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void forgotPassword_noAccountFoundForEmail_ReturnsBadRequest() {
        when(service.find(anyString())).thenReturn(null);
        response = controller.forgotPassword("");

        verify(service).find("");
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }

    @Test
    public void forgotPassword_noEmailProvided_returnsBadRequest() {
        response = controller.forgotPassword(null);

        verify(service, never()).find(null);
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }
    //endregion

    //region register
    @Test
    public void register_validAccount_returnsOk() throws RemoteException, NotBoundException {
        Account account = new Account();

        when(service.register(account)).thenReturn(true);
        response = controller.register(account);

        verify(service).register(account);
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void register_emailAlreadyExists_returnsBadRequest() throws RemoteException, NotBoundException {
        Account account = new Account();

        when(service.register(any())).thenReturn(false);
        response = controller.register(account);

        verify(service).register(account);
        assertTrue(response.getStatusCode() == HttpStatus.BAD_REQUEST);
    }
    //endregion

    //region getAll
    @Test
    public void getAll_IsCalled_returnsOk() {
        response = controller.getAllUsers();

        verify(service).getAllAccounts();
        assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    //endregion
}