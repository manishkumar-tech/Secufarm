package com.weather.risk.mfi.myfarminfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IrrrigationMngtResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("DAS")
    @Expose
    private String das;
    @SerializedName("Notification")
    @Expose
    private String notification;
    @SerializedName("soiltype")
    @Expose
    private String soiltype;
    @SerializedName("cropid")
    @Expose
    private Integer cropid;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("cropname")
    @Expose
    private String cropname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDas() {
        return das;
    }

    public void setDas(String das) {
        this.das = das;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
    }

    public Integer getCropid() {
        return cropid;
    }

    public void setCropid(Integer cropid) {
        this.cropid = cropid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

}