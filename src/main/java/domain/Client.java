package domain;

import java.io.Serializable;
import java.util.UUID;

public final class Client implements Serializable {

    private UUID id;
    private Account account;

    public Client() {

    }

    public Client(Account account) {
        id = UUID.randomUUID();
        this.account = account;
    }

    public Client(String username, String password, String email) {
        id = UUID.randomUUID();
        account = new Account(username, password, email);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //region getters & setters
    public UUID getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }
    //endregion

}
