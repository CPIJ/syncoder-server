package domain;

import java.io.Serializable;
import java.util.UUID;

public final class Client implements Serializable {

    private UUID id;
    private Account account;
    private int openWindows;

    public Client() {

    }

    public Client(String username, String password, String email, boolean isAdmin) {
        id = UUID.randomUUID();
        account = new Account(username, password, email, isAdmin);
    }

    public Client(UUID id, Account account) {
        this.id = id;
        this.account = account;
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

    public int getOpenWindows() {
        return openWindows;
    }

    public void addWindow() {
        openWindows++;
    }

    public void removeWindow() {
        if (openWindows != 0)
            openWindows--;
    }
    //endregion

}
