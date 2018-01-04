package com.minosai.skindoc.user.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 31/12/17.
 */

public class ApDetail {
    @SerializedName("doctor")
    @Expose
    private String doctor;
    @SerializedName("doctor_name")
    @Expose
    private String doctorName;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("description")
    @Expose
    private String description;

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
