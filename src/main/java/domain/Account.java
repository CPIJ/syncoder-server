package domain;

import java.io.Serializable;

public final class Account implements Serializable {

    private String username;
    private String password;
    private String email;

    public Account() {
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //region getters & setters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    //endregion

}
