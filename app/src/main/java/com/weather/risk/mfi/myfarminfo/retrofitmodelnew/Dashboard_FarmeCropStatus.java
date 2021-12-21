package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeCropStatus {

    @SerializedName("Ndvi")
    @Expose
    private Dashboard_FarmeNdvi ndvi;
    @SerializedName("rain")
    @Expose
    private Dashboard_FarmeRain rain;
    @SerializedName("soilMoisture")
    @Expose
    private Dashboard_FarmeSoilMoisture soilMoisture;
    @SerializedName("lstdisesecond")
    @Expose
    private List<Object> lstdisesecond = null;
    @SerializedName("lstweathercond")
    @Expose
    private List<Object> lstweathercond = null;
    @SerializedName("lstdiseasealert")
    @Expose
    private List<Object> lstdiseasealert = null;
    @SerializedName("lstweatheralert")
    @Expose
    private List<Object> lstweatheralert = null;
    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusThumb")
    @Expose
    private String statusThumb;

    public Dashboard_FarmeNdvi getNdvi() {
        return ndvi;
    }

    public void setNdvi(Dashboard_FarmeNdvi ndvi) {
        this.ndvi = ndvi;
    }

    public Dashboard_FarmeRain getRain() {
        return rain;
    }

    public void setRain(Dashboard_FarmeRain rain) {
        this.rain = rain;
    }

    public Dashboard_FarmeSoilMoisture getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(Dashboard_FarmeSoilMoisture soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public List<Object> getLstdisesecond() {
        return lstdisesecond;
    }

    public void setLstdisesecond(List<Object> lstdisesecond) {
        this.lstdisesecond = lstdisesecond;
    }

    public List<Object> getLstweathercond() {
        return lstweathercond;
    }

    public void setLstweathercond(List<Object> lstweathercond) {
        this.lstweathercond = lstweathercond;
    }

    public List<Object> getLstdiseasealert() {
        return lstdiseasealert;
    }

    public void setLstdiseasealert(List<Object> lstdiseasealert) {
        this.lstdiseasealert = lstdiseasealert;
    }

    public List<Object> getLstweatheralert() {
        return lstweatheralert;
    }

    public void setLstweatheralert(List<Object> lstweatheralert) {
        this.lstweatheralert = lstweatheralert;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusThumb() {
        return statusThumb;
    }

    public void setStatusThumb(String statusThumb) {
        this.statusThumb = statusThumb;
    }

}
