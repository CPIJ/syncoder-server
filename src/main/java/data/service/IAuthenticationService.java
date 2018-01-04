package data.service;

import domain.Account;
import domain.Client;
import java.util.List;

public interface IAuthenticationService {
    Client login(String email, String password);
    boolean register(Account account);
    List<Account> getAllAccounts();
    boolean isAuthorized(Client client);
}
