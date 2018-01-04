package com.minosai.skindoc.user.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 04/01/18.
 */

public class AppointBody {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("descriptin")
    @Expose
    private String description;

    public AppointBody(String token, String description) {
        this.token = token;
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
