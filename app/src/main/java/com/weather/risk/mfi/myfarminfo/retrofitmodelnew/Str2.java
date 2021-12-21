package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Str2 {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Comp")
    @Expose
    private Integer comp;
    @SerializedName("Pending")
    @Expose
    private Integer pending;
    @SerializedName("Total")
    @Expose
    private Integer total;
    @SerializedName("TotalUnRead")
    @Expose
    private String totalUnRead;
    @SerializedName("UserID")
    @Expose
    private Integer userID;
    @SerializedName("Farmers")
    @Expose
    private Object farmers;
    @SerializedName("CropID")
    @Expose
    private Integer cropID;
    @SerializedName("CropName")
    @Expose
    private String cropName;
    @SerializedName("ClientID")
    @Expose
    private Integer clientID;
    @SerializedName("CustomCreateID")
    @Expose
    private String customCreateID;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getComp() {
        return comp;
    }

    public void setComp(Integer comp) {
        this.comp = comp;
    }

    public Integer getPending() {
        return pending;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getTotalUnRead() {
        return totalUnRead;
    }

    public void setTotalUnRead(String totalUnRead) {
        this.totalUnRead = totalUnRead;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Object getFarmers() {
        return farmers;
    }

    public void setFarmers(Object farmers) {
        this.farmers = farmers;
    }

    public Integer getCropID() {
        return cropID;
    }

    public void setCropID(Integer cropID) {
        this.cropID = cropID;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getCustomCreateID() {
        return customCreateID;
    }

    public void setCustomCreateID(String customCreateID) {
        this.customCreateID = customCreateID;
    }

}
