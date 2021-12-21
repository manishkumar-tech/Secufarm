package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SurveyorResponseBean {

    @SerializedName("TotalCollectedAmount")
    @Expose
    private String totalCollectedAmount;
    @SerializedName("TotalAmountTransfered")
    @Expose
    private String totalAmountTransfered;

    public String getTotalCollectedAmount() {
        return totalCollectedAmount;
    }

    public void setTotalCollectedAmount(String totalCollectedAmount) {
        this.totalCollectedAmount = totalCollectedAmount;
    }

    public String getTotalAmountTransfered() {
        return totalAmountTransfered;
    }

    public void setTotalAmountTransfered(String totalAmountTransfered) {
        this.totalAmountTransfered = totalAmountTransfered;
    }

}
