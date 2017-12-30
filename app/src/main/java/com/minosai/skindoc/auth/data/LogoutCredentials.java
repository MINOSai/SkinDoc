package com.minosai.skindoc.auth.data;

/**
 * Created by minos.ai on 31/12/17.
 */

public class LogoutCredentials {
    String token;

    public LogoutCredentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
