package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weather.risk.mfi.myfarminfo.marketplace.DataDT;

import java.util.List;

public class OrderDetailResponse {




    @SerializedName("Orderdate")
    @Expose
    private String orderdate;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("CollectedAmount")
    @Expose
    private Double collectedAmount;
    @SerializedName("BalanceAmount")
    @Expose
    private Double balanceAmount;
    @SerializedName("dataDT")
    @Expose
    private List<DataDT> dataDT = null;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("TransactionID")
    @Expose
    private String transactionID;
    @SerializedName("FlgDelivery")
    @Expose
    private boolean flgDelivery;
    @SerializedName("TransactionImage")
    @Expose
    private String transactionImage;

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
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

    public List<DataDT> getDataDT() {
        return dataDT;
    }

    public void setDataDT(List<DataDT> dataDT) {
        this.dataDT = dataDT;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public boolean getFlgDelivery() {
        return flgDelivery;
    }

    public void setFlgDelivery(boolean flgDelivery) {
        this.flgDelivery = flgDelivery;
    }

    public String getTransactionImage() {
        return transactionImage;
    }

    public void setTransactionImage(String transactionImage) {
        this.transactionImage = transactionImage;
    }


}
