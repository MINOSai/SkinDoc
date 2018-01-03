package com.minosai.skindoc.auth.data;

/**
 * Created by minos.ai on 03/01/18.
 */

public class TokenString {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenString(String token) {

        this.token = token;
    }
}
