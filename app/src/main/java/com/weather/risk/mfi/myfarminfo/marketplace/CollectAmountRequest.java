package com.weather.risk.mfi.myfarminfo.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectAmountRequest {

    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("dateFrom")
    @Expose
    private String dateFrom;

    @SerializedName("dateTo")
    @Expose
    private String dateTo;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
}
