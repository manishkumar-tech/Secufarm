package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelList {

    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FatherName")
    @Expose
    private String fatherName;
    @SerializedName("FarmerID")
    @Expose
    private Integer farmerID;
    @SerializedName("TotalAreaServiceTaken")
    @Expose
    private Integer totalAreaServiceTaken;
    @SerializedName("PendingArea")
    @Expose
    private Double pendingArea;
    @SerializedName("TotalAreaGeoTag")
    @Expose
    private Double totalAreaGeoTag;
    @SerializedName("VillageName")
    @Expose
    private String villageName;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("ProjectName")
    @Expose
    private String projectName;
    @SerializedName("CollectedAmount")
    @Expose
    private Integer collectedAmount;
    @SerializedName("PendingAmount")
    @Expose
    private Integer pendingAmount;
    @SerializedName("TotalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;

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

    public Integer getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(Integer farmerID) {
        this.farmerID = farmerID;
    }

    public Integer getTotalAreaServiceTaken() {
        return totalAreaServiceTaken;
    }

    public void setTotalAreaServiceTaken(Integer totalAreaServiceTaken) {
        this.totalAreaServiceTaken = totalAreaServiceTaken;
    }

    public Double getPendingArea() {
        return pendingArea;
    }

    public void setPendingArea(Double pendingArea) {
        this.pendingArea = pendingArea;
    }

    public Double getTotalAreaGeoTag() {
        return totalAreaGeoTag;
    }

    public void setTotalAreaGeoTag(Double totalAreaGeoTag) {
        this.totalAreaGeoTag = totalAreaGeoTag;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(Integer collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public Integer getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Integer pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

}
