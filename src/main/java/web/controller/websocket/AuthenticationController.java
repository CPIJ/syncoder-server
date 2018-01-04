package web.controller.websocket;

import data.repository.MySqlAuthenticationRepository;
import data.service.AuthenticationService;
import data.service.IAuthenticationService;
import domain.Account;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.List;

public class AuthenticationController {
    private IAuthenticationService service = new AuthenticationService(new MySqlAuthenticationRepository());

    @MessageMapping("user/after-registration")
    @SendTo("/topic/after-registration")
    public List<Account> getAllClients(){
        return service.getAllAccounts();
    }
}
