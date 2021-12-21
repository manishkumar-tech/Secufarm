package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutResponse {

    @SerializedName("AddServicesResult")
    @Expose
    private String addServicesResult;

    public String getAddServicesResult() {
        return addServicesResult;
    }

    public void setAddServicesResult(String addServicesResult) {
        this.addServicesResult = addServicesResult;
    }
}
