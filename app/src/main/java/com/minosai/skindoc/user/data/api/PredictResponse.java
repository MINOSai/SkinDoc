package com.minosai.skindoc.user.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by minos.ai on 05/01/18.
 */

public class PredictResponse {

    @SerializedName("confidence")
    @Expose
    private Double confidence;
    @SerializedName("result")
    @Expose
    private String message;

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
