package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CheckoutRequest {


    @SerializedName("PaymentType")
    @Expose
    private String PaymentType;
    @SerializedName("DeliveryStatus")
    @Expose
    private String DeliveryStatus;


    @SerializedName("UserID")
    @Expose
    private String UserID;

    @SerializedName("farmerid")
    @Expose
    private String farmerid;

    @SerializedName("projectid")
    @Expose
    private String projectid;

    @SerializedName("services")
    @Expose
    private String services = null;

    public String getFarmerid() {
        return farmerid;
    }

    public void setFarmerid(String farmerid) {
        this.farmerid = farmerid;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }
}

