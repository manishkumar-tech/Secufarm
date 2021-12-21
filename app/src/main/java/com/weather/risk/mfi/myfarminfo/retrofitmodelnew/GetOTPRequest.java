package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetOTPRequest {

    @SerializedName("FarmID")
    @Expose
    private Integer FarmID;

    @SerializedName("transactionID")
    @Expose
    private String transactionID;

    @SerializedName("deliveryStatus")
    @Expose
    private String deliveryStatus;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("collectedAmount")
    @Expose
    private Double collectedAmount;
    @SerializedName("balanceAmount")
    @Expose
    private Double balanceAmount;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;

    @SerializedName("flgDelivery")
    @Expose
    private boolean flgDelivery;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    //Add Herojit 20210513

    @SerializedName("language")
    @Expose
    private String language;

    //Pormise to Pay Date
    @SerializedName("PTPDate")
    @Expose
    private String PTPDate ;



    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(Double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public boolean isFlgDelivery() {
        return flgDelivery;
    }

    public void setFlgDelivery(boolean flgDelivery) {
        this.flgDelivery = flgDelivery;
    }


    public Integer getFarmID() {
        return FarmID;
    }

    public void setFarmID(Integer farmID) {
        FarmID = farmID;
    }

    public String getPTPDate() {
        return PTPDate;
    }

    public void setPTPDate(String PTPDate) {
        this.PTPDate = PTPDate;
    }
}
