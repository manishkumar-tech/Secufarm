package com.weather.risk.mfi.myfarminfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeedMngtRequest {
    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("cropid")
    @Expose
    private int cropid;

    @SerializedName("FarmID")
    @Expose
    private String FarmID;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getCropid() {
        return cropid;
    }

    public void setCropid(int cropid) {
        this.cropid = cropid;
    }

    public String getFarmID() {
        return FarmID;
    }

    public void setFarmID(String farmID) {
        FarmID = farmID;
    }
}
