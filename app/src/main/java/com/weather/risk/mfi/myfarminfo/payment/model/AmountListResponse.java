package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AmountListResponse {


    @SerializedName("TotalFarmer")
    @Expose
    private Integer totalFarmer;
    @SerializedName("NoofFarmerPaymentPending")
    @Expose
    private Integer noofFarmerPaymentPending;
    @SerializedName("TotalProjectWiseTotalAmount")
    @Expose
    private Integer totalProjectWiseTotalAmount;
    @SerializedName("TotalProjectWiseCollectedAmount")
    @Expose
    private Integer totalProjectWiseCollectedAmount;
    @SerializedName("TotalProjectWisePendingAmount")
    @Expose
    private Integer totalProjectWisePendingAmount;
    @SerializedName("ProjectName")
    @Expose
    private String projectName;
    @SerializedName("TotalAreaGeoTag")
    @Expose
    private Double totalAreaGeoTag;
    @SerializedName("TotalAreaServiceTaken")
    @Expose
    private Integer totalAreaServiceTaken;
    @SerializedName("PendingArea")
    @Expose
    private Double pendingArea;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;
    @SerializedName("ModelList")
    @Expose
    private List<ModelList> modelList = null;

    public Integer getTotalFarmer() {
        return totalFarmer;
    }

    public void setTotalFarmer(Integer totalFarmer) {
        this.totalFarmer = totalFarmer;
    }

    public Integer getNoofFarmerPaymentPending() {
        return noofFarmerPaymentPending;
    }

    public void setNoofFarmerPaymentPending(Integer noofFarmerPaymentPending) {
        this.noofFarmerPaymentPending = noofFarmerPaymentPending;
    }

    public Integer getTotalProjectWiseTotalAmount() {
        return totalProjectWiseTotalAmount;
    }

    public void setTotalProjectWiseTotalAmount(Integer totalProjectWiseTotalAmount) {
        this.totalProjectWiseTotalAmount = totalProjectWiseTotalAmount;
    }

    public Integer getTotalProjectWiseCollectedAmount() {
        return totalProjectWiseCollectedAmount;
    }

    public void setTotalProjectWiseCollectedAmount(Integer totalProjectWiseCollectedAmount) {
        this.totalProjectWiseCollectedAmount = totalProjectWiseCollectedAmount;
    }

    public Integer getTotalProjectWisePendingAmount() {
        return totalProjectWisePendingAmount;
    }

    public void setTotalProjectWisePendingAmount(Integer totalProjectWisePendingAmount) {
        this.totalProjectWisePendingAmount = totalProjectWisePendingAmount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getTotalAreaGeoTag() {
        return totalAreaGeoTag;
    }

    public void setTotalAreaGeoTag(Double totalAreaGeoTag) {
        this.totalAreaGeoTag = totalAreaGeoTag;
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

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public List<ModelList> getModelList() {
        return modelList;
    }

    public void setModelList(List<ModelList> modelList) {
        this.modelList = modelList;
    }

}
