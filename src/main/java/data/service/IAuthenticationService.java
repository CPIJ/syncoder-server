package data.service;

import domain.Account;
import domain.Client;
import java.util.List;

public interface IAuthenticationService {
    Client login(String username, String password);
    boolean register(Client client);
    List<Account> getAllAccounts();
    boolean isAuthorized(Client client);
}
