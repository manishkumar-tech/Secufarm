package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeWeatherForecast {

    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("forecastdata")
    @Expose
    private String forecastdata;
    @SerializedName("lstAlertMessages")
    @Expose
    private List<Object> lstAlertMessages = null;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getForecastdata() {
        return forecastdata;
    }

    public void setForecastdata(String forecastdata) {
        this.forecastdata = forecastdata;
    }

    public List<Object> getLstAlertMessages() {
        return lstAlertMessages;
    }

    public void setLstAlertMessages(List<Object> lstAlertMessages) {
        this.lstAlertMessages = lstAlertMessages;
    }
}
