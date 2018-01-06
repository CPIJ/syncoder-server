package web.controller.api;

import application.Properties;
import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmi.fontys.IRemotePublisherForListener;
import rmi.fontys.RemotePublisher;
import web.model.LoginRequest;

import java.rmi.RemoteException;

@RestController
@RequestMapping("/authentication")
public class AuthenticationApiController {

    private final IAuthenticationService service;
    private final RemotePublisher publisher;

    @Autowired
    public AuthenticationApiController(IAuthenticationService service, RemotePublisher publisher) {
        this.service = service;
        this.publisher = publisher;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        if (request.getPassword() == null || request.getEmail() == null) {
            return BadRequest("No username and/or password provided!");
        }

        Client client = service.login(request.getEmail(), request.getPassword());

        if (client == null) {
            return BadRequest("No user found with this combination of username and password.");
        }

        return Ok(client);
    }

    @RequestMapping(value = "/isAuthorized", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity isAuthorized(@RequestBody Client client) {
        if (client == null) return BadRequest();

        return Ok(service.isAuthorized(client));
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {
        if (email == null) return BadRequest("No email provided!");
        Account account = service.find(email);

        if (account == null) return BadRequest("No user found with this email.");

        return Ok(new Object() {
            public final String password = account.getPassword();
        });
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity register(@RequestBody Account account) throws RemoteException {
        if (service.register(account)) {

            publisher.inform(
                    Properties.get("rmi", "registerProperty"),
                    service.getAllAccounts(),
                    service.getAllAccounts()
            );

            return Ok(new Object() {
                public final String message = "You can now login with your new account!";
            });
        }

        return BadRequest("An account with this email already exists.");
    }


    @RequestMapping(value = "/account/all", method = RequestMethod.GET)
    public ResponseEntity getAllUsers() {
        return Ok(service.getAllAccounts());
    }

    //region helper methods
    private <T> ResponseEntity<T> Ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private ResponseEntity Ok() {
        return new ResponseEntity(HttpStatus.OK);
    }

    private <T> ResponseEntity<T> BadRequest(T body) {
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity BadRequest() {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    //endregion
}
