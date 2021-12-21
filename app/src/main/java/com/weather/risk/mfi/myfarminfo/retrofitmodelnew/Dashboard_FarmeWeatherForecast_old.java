package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeWeatherForecast_old {

    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("forecastdata")
    @Expose
    private List<Dashboard_FarmeForecastdatum> forecastdata = null;
    @SerializedName("lstAlertMessages")
    @Expose
    private List<Object> lstAlertMessages = null;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Dashboard_FarmeForecastdatum> getForecastdata() {
        return forecastdata;
    }

    public void setForecastdata(List<Dashboard_FarmeForecastdatum> forecastdata) {
        this.forecastdata = forecastdata;
    }

    public List<Object> getLstAlertMessages() {
        return lstAlertMessages;
    }

    public void setLstAlertMessages(List<Object> lstAlertMessages) {
        this.lstAlertMessages = lstAlertMessages;
    }

}
