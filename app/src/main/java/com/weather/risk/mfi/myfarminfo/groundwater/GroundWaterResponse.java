package com.weather.risk.mfi.myfarminfo.groundwater;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroundWaterResponse {

    @SerializedName("agriarea")
    @Expose
    private Double agriarea;
    @SerializedName("rainfall")
    @Expose
    private Double rainfall;
    @SerializedName("actualgroundwater")
    @Expose
    private Double actualgroundwater;
    @SerializedName("estimatedgroundwater")
    @Expose
    private Double estimatedgroundwater;
    @SerializedName("runoff")
    @Expose
    private Double runoff;
    @SerializedName("divergance")
    @Expose
    private Double divergance;
    @SerializedName("maxlat")
    @Expose
    private Double maxlat;
    @SerializedName("minlat")
    @Expose
    private Double minlat;
    @SerializedName("maxlon")
    @Expose
    private Double maxlon;
    @SerializedName("minlon")
    @Expose
    private Double minlon;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("gridName")
    @Expose
    private String gridName;

    public Double getAgriarea() {
        return agriarea;
    }

    public void setAgriarea(Double agriarea) {
        this.agriarea = agriarea;
    }

    public Double getRainfall() {
        return rainfall;
    }

    public void setRainfall(Double rainfall) {
        this.rainfall = rainfall;
    }

    public Double getActualgroundwater() {
        return actualgroundwater;
    }

    public void setActualgroundwater(Double actualgroundwater) {
        this.actualgroundwater = actualgroundwater;
    }

    public Double getEstimatedgroundwater() {
        return estimatedgroundwater;
    }

    public void setEstimatedgroundwater(Double estimatedgroundwater) {
        this.estimatedgroundwater = estimatedgroundwater;
    }

    public Double getRunoff() {
        return runoff;
    }

    public void setRunoff(Double runoff) {
        this.runoff = runoff;
    }

    public Double getDivergance() {
        return divergance;
    }

    public void setDivergance(Double divergance) {
        this.divergance = divergance;
    }

    public Double getMaxlat() {
        return maxlat;
    }

    public void setMaxlat(Double maxlat) {
        this.maxlat = maxlat;
    }

    public Double getMinlat() {
        return minlat;
    }

    public void setMinlat(Double minlat) {
        this.minlat = minlat;
    }

    public Double getMaxlon() {
        return maxlon;
    }

    public void setMaxlon(Double maxlon) {
        this.maxlon = maxlon;
    }

    public Double getMinlon() {
        return minlon;
    }

    public void setMinlon(Double minlon) {
        this.minlon = minlon;
    }

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

    public String getGridName() {
        return gridName;
    }

    public void setGridName(String gridName) {
        this.gridName = gridName;
    }

}
