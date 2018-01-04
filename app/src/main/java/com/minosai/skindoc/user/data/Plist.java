package com.minosai.skindoc.user.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 05/01/18.
 */

public class Plist {
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("name")
    @Expose
    private String userName;
    @SerializedName("description")
    @Expose
    private String description;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
