package com.weather.risk.mfi.myfarminfo.groundwater;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroundWaterBeans {
    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lon")
    @Expose
    private Double lon;

    @SerializedName("year")
    @Expose
    private Integer year;

    @SerializedName("season")
    @Expose
    private String season;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
