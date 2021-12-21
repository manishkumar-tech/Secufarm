package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceDetailsResponse {
    @SerializedName("Orderdate")
    @Expose
    private String orderdate;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("CollectedAmount")
    @Expose
    private Double collectedAmount;
    @SerializedName("BalanceAmount")
    @Expose
    private Double balanceAmount;
    @SerializedName("FarmID")
    @Expose
    private Integer farmID;
    @SerializedName("LoginUserID")
    @Expose
    private Object loginUserID;
    @SerializedName("DTPolicy")
    @Expose
    private List<DTPolicy> dTPolicy = null;
    @SerializedName("dataDT")
    @Expose
    private List<DataDT> dataDT = null;
    @SerializedName("OrderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("TransactionID")
    @Expose
    private Object transactionID;
    @SerializedName("FlgDelivery")
    @Expose
    private Boolean flgDelivery;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("TransactionImage")
    @Expose
    private Object transactionImage;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("DeliveryStatus")
    @Expose
    private Object deliveryStatus;
    @SerializedName("partialDeliveryModelslst")
    @Expose
    private List<Object> partialDeliveryModelslst = null;
    @SerializedName("ExcepetedCollectionDate")
    @Expose
    private String excepetedCollectionDate;
    @SerializedName("SurvoyerID")
    @Expose
    private Integer survoyerID;

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }

    public Object getLoginUserID() {
        return loginUserID;
    }

    public void setLoginUserID(Object loginUserID) {
        this.loginUserID = loginUserID;
    }

    public List<DTPolicy> getDTPolicy() {
        return dTPolicy;
    }

    public void setDTPolicy(List<DTPolicy> dTPolicy) {
        this.dTPolicy = dTPolicy;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Object getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Object transactionID) {
        this.transactionID = transactionID;
    }

    public Boolean getFlgDelivery() {
        return flgDelivery;
    }

    public void setFlgDelivery(Boolean flgDelivery) {
        this.flgDelivery = flgDelivery;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Object getTransactionImage() {
        return transactionImage;
    }

    public void setTransactionImage(Object transactionImage) {
        this.transactionImage = transactionImage;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Object getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Object deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public List<Object> getPartialDeliveryModelslst() {
        return partialDeliveryModelslst;
    }

    public void setPartialDeliveryModelslst(List<Object> partialDeliveryModelslst) {
        this.partialDeliveryModelslst = partialDeliveryModelslst;
    }

    public String getExcepetedCollectionDate() {
        return excepetedCollectionDate;
    }

    public void setExcepetedCollectionDate(String excepetedCollectionDate) {
        this.excepetedCollectionDate = excepetedCollectionDate;
    }

    public Integer getSurvoyerID() {
        return survoyerID;
    }

    public void setSurvoyerID(Integer survoyerID) {
        this.survoyerID = survoyerID;
    }

}
