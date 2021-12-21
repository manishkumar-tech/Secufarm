package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InfoDataDT {

    @SerializedName("FarmStatus")
    @Expose
    private String FarmStatus;

    @SerializedName("FarmID")
    @Expose
    private Integer farmID;
    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("CropName")
    @Expose
    private String cropName;
    @SerializedName("logdate")
    @Expose
    private String logdate;

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getLogdate() {
        return logdate;
    }

    public void setLogdate(String logdate) {
        this.logdate = logdate;
    }

    public String getFarmStatus() {
        return FarmStatus;
    }

    public void setFarmStatus(String farmStatus) {
        FarmStatus = farmStatus;
    }
}
