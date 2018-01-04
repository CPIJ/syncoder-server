package data.service;

import data.repository.AuthenticationRepository;
import domain.Account;
import domain.Client;

import java.util.List;

public class AuthenticationService implements IAuthenticationService {

    private AuthenticationRepository repository;

    public AuthenticationService(AuthenticationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Client login(String username, String password) {
        return repository.login(username, password);
    }

    @Override
    public boolean register(Account account) {
        return repository.register(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return repository.getAllAccounts();
    }

    @Override
    public boolean isAuthorized(Client client) {
        return repository
                .getAllAccounts()
                .stream()
                .anyMatch(c ->
                        c.getUsername().equals(client.getAccount().getUsername())
                                && c.getPassword().equals(client.getAccount().getPassword())
                                && c.getEmail().equals(client.getAccount().getEmail()));
    }
}