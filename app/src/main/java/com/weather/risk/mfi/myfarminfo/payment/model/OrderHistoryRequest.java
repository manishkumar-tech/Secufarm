package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryRequest  {

    @SerializedName("projectID")
    @Expose
    private Integer projectID;

    @SerializedName("farmerID")
    @Expose
    private Integer farmerID;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;

    @SerializedName("UserID")
    @Expose
    private Integer UserID;

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
        return UserID;
    }

    public void setUserID(Integer userID) {
        UserID = userID;
    }

    public Integer getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(Integer farmerID) {
        this.farmerID = farmerID;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }
}
