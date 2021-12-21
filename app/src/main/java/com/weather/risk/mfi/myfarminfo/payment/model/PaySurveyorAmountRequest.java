package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaySurveyorAmountRequest {


    @SerializedName("amountTransfer")
    @Expose
    private String amountTransfer;
    @SerializedName("transactionID")
    @Expose
    private String transactionID;
    @SerializedName("transactionImage")
    @Expose
    private String transactionImage;
    @SerializedName("surveyerID")
    @Expose
    private Integer surveyerID;

    public String getAmountTransfer() {
        return amountTransfer;
    }

    public void setAmountTransfer(String amountTransfer) {
        this.amountTransfer = amountTransfer;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionImage() {
        return transactionImage;
    }

    public void setTransactionImage(String transactionImage) {
        this.transactionImage = transactionImage;
    }

    public Integer getSurveyerID() {
        return surveyerID;
    }

    public void setSurveyerID(Integer surveyerID) {
        this.surveyerID = surveyerID;
    }


}
