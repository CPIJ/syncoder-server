package web.controller.api;

import data.repository.MySqlAuthenticationRepository;
import data.service.AuthenticationService;
import data.service.IAuthenticationService;
import domain.Account;
import domain.Client;
import domain.ProjectManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.LoginRequest;

import java.util.Optional;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private IAuthenticationService service = new AuthenticationService(new MySqlAuthenticationRepository());

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
    public ResponseEntity forgotPassword(@RequestParam String email) {
        if (email == null) return BadRequest("No email provided!");

        Optional<Account> account = service
                .getAllAccounts()
                .stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst();

        //noinspection OptionalIsPresent
        if (!account.isPresent()) return BadRequest("No user found with this email.");

        return Ok(account.get().getPassword());
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity register(@RequestBody Account account) {
        if (service.register(account)) return Ok("Account was registered!");

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
