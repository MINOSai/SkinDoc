package com.minosai.skindoc.user.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 04/01/18.
 */

public class ResolveBody {

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("doctor")
    @Expose
    private String doctor;

    public ResolveBody(String token, String user, String doctor) {
        this.token = token;
        this.user = user;
        this.doctor = doctor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
