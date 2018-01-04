package com.minosai.skindoc.user.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by minos.ai on 31/12/17.
 */

public class User {
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("google")
    @Expose
    private String google;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("portal")
    @Expose
    private int portal;
    @SerializedName("plist")
    @Expose
    private List<Plist> plist = null;
    @SerializedName("ap_details")
    @Expose
    private List<ApDetail> apDetails = null;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("qualifications")
    @Expose
    private String qualification;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPortal() {
        return portal;
    }

    public void setPortal(int portal) {
        this.portal = portal;
    }

    public List<Plist> getPlist() {
        return plist;
    }

    public void setPlist(List<Plist> plist) {
        this.plist = plist;
    }

    public List<ApDetail> getApDetails() {
        return apDetails;
    }

    public void setApDetails(List<ApDetail> apDetails) {
        this.apDetails = apDetails;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
