package com.minosai.skindoc.user.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 04/01/18.
 */

public class ResolveBody {
    @SerializedName("user")
    @Expose
    private String user;

    @SerializedName("doctor")
    @Expose

    private String doctor;

    public ResolveBody(String user, String doctor) {
        this.user = user;
        this.doctor = doctor;
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
