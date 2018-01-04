package domain;

import java.io.Serializable;

final class Account implements Serializable {

    private String username;
    private String password;
    private String email;

    Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    Account(String username) {
        this.username = username;
    }

    //region getters & setters

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getEmail() {
        return email;
    }

    //endregion

}
