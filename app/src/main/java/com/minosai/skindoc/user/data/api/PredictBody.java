package com.minosai.skindoc.user.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by minos.ai on 05/01/18.
 */

public class PredictBody {
    @SerializedName("filefieldname")
    @Expose
    private File img;

    public File getImg() {
        return img;
    }

    public void setImg(File img) {
        this.img = img;
    }

    public PredictBody(File img) {
        this.img = img;
    }
}
