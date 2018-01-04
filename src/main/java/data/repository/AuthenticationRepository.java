package data.repository;

import domain.Account;
import domain.Client;

import java.util.List;

public interface AuthenticationRepository {
    Client login(String email, String password);
    boolean register(Client client);
    List<Account> getAllAccounts();
}

