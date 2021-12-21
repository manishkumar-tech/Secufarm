package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerResponse {

    @SerializedName("farmerPersonelID")
    @Expose
    private Integer farmerPersonelID;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FatherName")
    @Expose
    private String fatherName;
    @SerializedName("VillageAreaGeotagged")
    @Expose
    private String villageAreaGeotagged;

    public Integer getFarmerPersonelID() {
        return farmerPersonelID;
    }

    public void setFarmerPersonelID(Integer farmerPersonelID) {
        this.farmerPersonelID = farmerPersonelID;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getVillageAreaGeotagged() {
        return villageAreaGeotagged;
    }

    public void setVillageAreaGeotagged(String villageAreaGeotagged) {
        this.villageAreaGeotagged = villageAreaGeotagged;
    }

}
