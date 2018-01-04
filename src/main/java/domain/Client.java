package domain;

import java.io.Serializable;

public final class Client implements Serializable {

    private String id;
    private Account account;

    public Client() {

    }

    public Client(String username, String password, String email) {
        id = "";
        account = new Account(username, password, email);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //region getters & setters
    public String getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }
    //endregion

}
