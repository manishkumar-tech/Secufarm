package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDetectionResponse {

    @SerializedName("lebal")
    @Expose
    private String lebal;
    @SerializedName("Confidence")
    @Expose
    private String confidence;
    @SerializedName("ImagePath")
    @Expose
    private String imagePath;

    public String getLebal() {
        return lebal;
    }

    public void setLebal(String lebal) {
        this.lebal = lebal;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}