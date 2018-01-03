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
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("portal")
    @Expose
    private String portal;
    @SerializedName("portal")
    @Expose
    private List<String> plist = null;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("ap_details")
    @Expose
    private List<ApDetail> apDetails = null;
    @SerializedName("history")
    @Expose
    private String history;
    @SerializedName("valid")
    @Expose
    private Boolean valid;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("qualifications")
    @Expose
    private String qualification;
    @SerializedName("availability")
    @Expose
    private Boolean avaliability;

    public List<String> getPlist() {
        return plist;
    }

    public void setPlist(List<String> plist) {
        this.plist = plist;
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

    public Boolean getAvaliability() {
        return avaliability;
    }

    public void setAvaliability(Boolean avaliability) {
        this.avaliability = avaliability;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<ApDetail> getApDetails() {
        return apDetails;
    }

    public void setApDetails(List<ApDetail> apDetails) {
        this.apDetails = apDetails;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
