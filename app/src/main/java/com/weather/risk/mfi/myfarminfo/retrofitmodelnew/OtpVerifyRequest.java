package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weather.risk.mfi.myfarminfo.payment.model.PartialDeliveryModel;

import java.util.ArrayList;

public class OtpVerifyRequest {

    @SerializedName("FarmID")
    @Expose
    private String FarmID;

    @SerializedName("LoginUserID")
    @Expose
    private String LoginUserID;

    @SerializedName("excepetedCollectionDate")
    @Expose
    private String excepetedCollectionDate;

    @SerializedName("survoyerID")
    @Expose
    private String survoyerID;

    @SerializedName("TransactionImage")
    @Expose
    private String TransactionImage;

    @SerializedName("partialDeliveryModelslst")
    @Expose
    private ArrayList<PartialDeliveryModel> deliveryList;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("flgDelivery")
    @Expose
    private boolean flgDelivery;

    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("OTPForVerification")
    @Expose
    private String oTPForVerification;

    @SerializedName("transactionID")
    @Expose
    private String transactionID;

    public String getTransactionNumber() {
        return transactionID;
    }

    public void setTransactionNumber(String transactionNumber) {
        transactionID = transactionNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOTPForVerification() {
        return oTPForVerification;
    }

    public void setOTPForVerification(String oTPForVerification) {
        this.oTPForVerification = oTPForVerification;
    }

    public boolean isFlgDelivery() {
        return flgDelivery;
    }

    public void setFlgDelivery(boolean flgDelivery) {
        this.flgDelivery = flgDelivery;
    }

    public String getoTPForVerification() {
        return oTPForVerification;
    }

    public void setoTPForVerification(String oTPForVerification) {
        this.oTPForVerification = oTPForVerification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExcepetedCollectionDate() {
        return excepetedCollectionDate;
    }

    public void setExcepetedCollectionDate(String excepetedCollectionDate) {
        this.excepetedCollectionDate = excepetedCollectionDate;
    }

    public String getSurvoyerID() {
        return survoyerID;
    }

    public void setSurvoyerID(String survoyerID) {
        this.survoyerID = survoyerID;
    }

    public String getTransactionImage() {
        return TransactionImage;
    }

    public void setTransactionImage(String transactionImage) {
        TransactionImage = transactionImage;
    }

    public ArrayList<PartialDeliveryModel> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(ArrayList<PartialDeliveryModel> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getLoginUserID() {
        return LoginUserID;
    }

    public void setLoginUserID(String loginUserID) {
        LoginUserID = loginUserID;
    }

    public String getFarmID() {
        return FarmID;
    }

    public void setFarmID(String farmID) {
        FarmID = farmID;
    }
}
