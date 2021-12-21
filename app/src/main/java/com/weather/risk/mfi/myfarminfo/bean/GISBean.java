package com.weather.risk.mfi.myfarminfo.bean;

public class GISBean {

    String FarmIDLatLong;
    String FarmID;
    String FarmName;
    String District;
    String Village_Final;
    String Alert;
    String xmax;
    String ymax;
    String xmin;
    String ymin;
    String centerLat;
    String centerLon;

    public String getFarmIDLatLong() {
        return FarmIDLatLong;
    }

    public void setFarmIDLatLong(String farmIDLatLong) {
        FarmIDLatLong = farmIDLatLong;
    }

    public String getFarmID() {
        return FarmID;
    }

    public void setFarmID(String farmID) {
        FarmID = farmID;
    }

    public String getFarmName() {
        return FarmName;
    }

    public void setFarmName(String farmName) {
        FarmName = farmName;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getVillage_Final() {
        return Village_Final;
    }

    public void setVillage_Final(String village_Final) {
        Village_Final = village_Final;
    }

    public String getAlert() {
        return Alert;
    }

    public void setAlert(String alert) {
        Alert = alert;
    }

    public String getXmax() {
        return xmax;
    }

    public void setXmax(String xmax) {
        this.xmax = xmax;
    }

    public String getYmax() {
        return ymax;
    }

    public void setYmax(String ymax) {
        this.ymax = ymax;
    }

    public String getXmin() {
        return xmin;
    }

    public void setXmin(String xmin) {
        this.xmin = xmin;
    }

    public String getYmin() {
        return ymin;
    }

    public void setYmin(String ymin) {
        this.ymin = ymin;
    }



    public String getCenterLat() {
        return centerLat;
    }

    public void setCenterLat(String centerLat) {
        this.centerLat = centerLat;
    }

    public String getCenterLon() {
        return centerLon;
    }

    public void setCenterLon(String centerLon) {
        this.centerLon = centerLon;
    }
}
