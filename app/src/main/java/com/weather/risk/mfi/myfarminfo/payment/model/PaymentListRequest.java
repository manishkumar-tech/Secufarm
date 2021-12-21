package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListRequest {


    @SerializedName("projectID")
    @Expose
    private Integer projectID;

    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("pendingflag")
    @Expose
    private Boolean pendingflag;
    @SerializedName("detailSummary")
    @Expose
    private Boolean detailSummary;

    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Boolean getPendingflag() {
        return pendingflag;
    }

    public void setPendingflag(Boolean pendingflag) {
        this.pendingflag = pendingflag;
    }

    public Boolean getDetailSummary() {
        return detailSummary;
    }

    public void setDetailSummary(Boolean detailSummary) {
        this.detailSummary = detailSummary;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }
}


