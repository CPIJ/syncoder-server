package data.repository;

import domain.Account;
import domain.Client;

import java.util.List;

public interface IAuthenticationRepository {
    Client login(String email, String password);
    boolean register(Account account);
    List<Account> getAllAccounts();
    Account find(String email);
}

