package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryBean  {

    @SerializedName("FarmerID")
    @Expose
    private Integer farmerID;
    @SerializedName("TotalAmount")
    @Expose
    private Integer totalAmount;
    @SerializedName("Totalcollectedamount")
    @Expose
    private Integer totalcollectedamount;
    @SerializedName("Totalpendingamount")
    @Expose
    private Integer totalpendingamount;
    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("OrderAmount")
    @Expose
    private Integer orderAmount;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;


    @SerializedName("DeliveryStatus")
    @Expose
    private String DeliveryStatus;

    @SerializedName("PendingAmount")
    @Expose
    private Double PendingAmount;

    @SerializedName("CollectedAmount")
    @Expose
    private Double CollectedAmount;

    @SerializedName("BalanceAmount")
    @Expose
    private Double BalanceAmount;

    public Double getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        BalanceAmount = balanceAmount;
    }

    public Integer getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(Integer farmerID) {
        this.farmerID = farmerID;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalcollectedamount() {
        return totalcollectedamount;
    }

    public void setTotalcollectedamount(Integer totalcollectedamount) {
        this.totalcollectedamount = totalcollectedamount;
    }

    public Integer getTotalpendingamount() {
        return totalpendingamount;
    }

    public void setTotalpendingamount(Integer totalpendingamount) {
        this.totalpendingamount = totalpendingamount;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public Double getPendingAmount() {
        return PendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        PendingAmount = pendingAmount;
    }

    public Double getCollectedAmount() {
        return CollectedAmount;
    }

    public void setCollectedAmount(Double collectedAmount) {
        CollectedAmount = collectedAmount;
    }
}
