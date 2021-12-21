package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard_FarmeSoilMoisture {

    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("SoilValue")
    @Expose
    private String soilValue;
    @SerializedName("SoilBenchMark")
    @Expose
    private String soilBenchMark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoilValue() {
        return soilValue;
    }

    public void setSoilValue(String soilValue) {
        this.soilValue = soilValue;
    }

    public String getSoilBenchMark() {
        return soilBenchMark;
    }

    public void setSoilBenchMark(String soilBenchMark) {
        this.soilBenchMark = soilBenchMark;
    }
}
