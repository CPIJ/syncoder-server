package data.service;

import data.repository.IAuthenticationRepository;
import domain.Account;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements IAuthenticationService {

    private IAuthenticationRepository repository;

    @Autowired
    public AuthenticationService(IAuthenticationRepository repository) {
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
