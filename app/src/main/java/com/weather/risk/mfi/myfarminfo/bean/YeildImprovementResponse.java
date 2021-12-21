package com.weather.risk.mfi.myfarminfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YeildImprovementResponse {

    @SerializedName("Symptoms")
    @Expose
    private String symptoms;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ImageURL")
    @Expose
    private String imageURL;
    @SerializedName("ControlMeasures")
    @Expose
    private String controlMeasures;

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getControlMeasures() {
        return controlMeasures;
    }

    public void setControlMeasures(String controlMeasures) {
        this.controlMeasures = controlMeasures;
    }

}