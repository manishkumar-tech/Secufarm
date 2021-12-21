package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTPResponse {

    @SerializedName("TransactionNumber")
    @Expose
    private String transactionNumber;
    @SerializedName("OTP")
    @Expose
    private Integer oTP;
    @SerializedName("ExcecutionStatus")
    @Expose
    private String excecutionStatus;

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public Integer getOTP() {
        return oTP;
    }

    public void setOTP(Integer oTP) {
        this.oTP = oTP;
    }

    public String getExcecutionStatus() {
        return excecutionStatus;
    }

    public void setExcecutionStatus(String excecutionStatus) {
        this.excecutionStatus = excecutionStatus;
    }
}
