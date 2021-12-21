package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerServices {

    @SerializedName("UserID")
    @Expose
    private String UserID;

    @SerializedName("ReciptNo")
    @Expose
    private String reciptNo;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("AmountRate")
    @Expose
    private String amountRate;
    @SerializedName("ProjectID")
    @Expose
    private String projectID;
    @SerializedName("FarmerServiceID")
    @Expose
    private Integer farmerServiceID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getReciptNo() {
        return reciptNo;
    }

    public void setReciptNo(String reciptNo) {
        this.reciptNo = reciptNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmountRate() {
        return amountRate;
    }

    public void setAmountRate(String amountRate) {
        this.amountRate = amountRate;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public Integer getFarmerServiceID() {
        return farmerServiceID;
    }

    public void setFarmerServiceID(Integer farmerServiceID) {
        this.farmerServiceID = farmerServiceID;
    }

}
