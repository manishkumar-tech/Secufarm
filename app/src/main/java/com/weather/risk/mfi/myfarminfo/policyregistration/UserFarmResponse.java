package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserFarmResponse {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("ID1")
    @Expose
    private Integer id1;
    @SerializedName("FarmName")
    @Expose
    private String farmName;
    @SerializedName("Area")
    @Expose
    private Double area;
    @SerializedName("StateName")
    @Expose
    private String stateName;
    @SerializedName("District")
    @Expose
    private String district;
    @SerializedName("StateID")
    @Expose
    private String stateID;
    @SerializedName("DistrictID")
    @Expose
    private String districtID;
    @SerializedName("Village")
    @Expose
    private String village;
    @SerializedName("Contour")
    @Expose
    private String contour;
    @SerializedName("CenterLat")
    @Expose
    private Double centerLat;
    @SerializedName("CenterLon")
    @Expose
    private Double centerLon;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Integer getId1() {
        return id1;
    }

    public void setId1(Integer id1) {
        this.id1 = id1;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getContour() {
        return contour;
    }

    public void setContour(String contour) {
        this.contour = contour;
    }

    public Double getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(Double centerLat) {
        this.centerLat = centerLat;
    }

    public Double getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(Double centerLon) {
        this.centerLon = centerLon;
    }

}
