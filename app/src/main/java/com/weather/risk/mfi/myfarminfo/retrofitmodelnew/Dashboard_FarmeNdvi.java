package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard_FarmeNdvi {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("NdviValue")
    @Expose
    private String ndviValue;
    @SerializedName("NdviBenchMark")
    @Expose
    private String ndviBenchMark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNdviValue() {
        return ndviValue;
    }

    public void setNdviValue(String ndviValue) {
        this.ndviValue = ndviValue;
    }

    public String getNdviBenchMark() {
        return ndviBenchMark;
    }

    public void setNdviBenchMark(String ndviBenchMark) {
        this.ndviBenchMark = ndviBenchMark;
    }
}
