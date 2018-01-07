package domain;

import java.io.Serializable;

public final class Account implements Serializable {

    private String username;
    private String password;
    private String email;
    private boolean isAdmin;

    public Account() {
    }

    public Account(String username, String password, String email, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
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

    public boolean getIsAdmin() {
        return isAdmin;
    }

    //endregion

}
