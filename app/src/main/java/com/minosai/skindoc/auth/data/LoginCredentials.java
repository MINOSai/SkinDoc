package com.minosai.skindoc.auth.data;

/**
 * Created by minos.ai on 29/12/17.
 */

public class LoginCredentials {
    private String user;
    private String pass;

    public LoginCredentials(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public LoginCredentials() {
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
