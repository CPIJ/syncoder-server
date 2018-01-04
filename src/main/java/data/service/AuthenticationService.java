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
        Account account = repository.find(client.getAccount().getEmail());
        Account clientAccount = client.getAccount();

        return account != null
                && clientAccount.getEmail().equals(account.getEmail())
                && clientAccount.getPassword().equals(account.getPassword())
                && clientAccount.getUsername().equals(account.getUsername());

    }

    @Override
    public Account find(String email) {
        return repository.find(email);
    }
}
