package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerListRequest {

    @SerializedName("projectID")
    @Expose
    private Integer projectID;

    @SerializedName("farmerPersonelID")
    @Expose
    private Integer farmerPersonelID;

    @SerializedName("adhaar")
    @Expose
    private String adhaar;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("villageID")
    @Expose
    private Integer villageID;

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getAdhaar() {
        return adhaar;
    }

    public void setAdhaar(String adhaar) {
        this.adhaar = adhaar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getVillageID() {
        return villageID;
    }

    public void setVillageID(Integer villageID) {
        this.villageID = villageID;
    }

    public Integer getFarmerPersonelID() {
        return farmerPersonelID;
    }

    public void setFarmerPersonelID(Integer farmerPersonelID) {
        this.farmerPersonelID = farmerPersonelID;
    }
}
