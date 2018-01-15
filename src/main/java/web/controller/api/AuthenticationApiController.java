package web.controller.api;

import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.LoginRequest;
import web.model.ResponseMessage;

@RestController
@RequestMapping("/authentication")
public class AuthenticationApiController {

    private final IAuthenticationService service;

    @Autowired
    public AuthenticationApiController(IAuthenticationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity login(@RequestBody LoginRequest request) {
        if (request.getPassword() == null || request.getEmail() == null) {
            return badRequest(new ResponseMessage("No username and/or password provided!"));
        }

        Client client = service.login(request.getEmail(), request.getPassword());

        if (client == null) {
            return badRequest(new ResponseMessage("No user found with this combination of username and password."));
        }

        return ok(client);
    }

    @RequestMapping(value = "/isAuthorized", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity isAuthorized(@RequestBody Client client) {
        if (client == null) return badRequest();

        return ok(service.isAuthorized(client));
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) {
        if (email == null) return badRequest(new ResponseMessage("No email provided!"));
        Account account = service.find(email);

        if (account == null) return badRequest(new ResponseMessage("No user found with this email."));

        return ResponseEntity.ok(new ResponseMessage("This is your password: " + account.getPassword()));
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity register(@RequestBody Account account) {
        if (service.register(account)) {
            return ResponseEntity.ok(new ResponseMessage("You can now login with your new account."));
        }

        return badRequest(new ResponseMessage("An account with this email already exists."));
    }

    @RequestMapping(value = "/account/all", method = RequestMethod.GET)
    public ResponseEntity getAllUsers() {
        return ok(service.getAllAccounts());
    }

    //region helper methods
    private <T> ResponseEntity<T> ok(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private <T> ResponseEntity<T> badRequest(T body) {
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity badRequest() {
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    //endregion
}
