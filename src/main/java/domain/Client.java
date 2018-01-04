package domain;

import java.io.Serializable;

public final class Client implements Serializable {

    private String id;
    private Account account;

    public Client() {

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //region getters & setters

    public String getId() {
        return id;
    }

    public String getUsername() {
        return account.getUsername();
    }

    public String getEmail() {
        return account.getEmail();
    }

    public String getPassword() {
        return account.getPassword();
    }


    //endregion

}
