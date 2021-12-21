package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmerIrrigationAdvisory {

    @SerializedName("lstirrigationadvisory")
    @Expose
    private List<Object> lstirrigationadvisory = null;
    @SerializedName("CurrentDASfromto")
    @Expose
    private String currentDASfromto;
    @SerializedName("CurrentDatefrom")
    @Expose
    private String currentDatefrom;
    @SerializedName("CurrentDateto")
    @Expose
    private String currentDateto;
    @SerializedName("irrgationadvisoryDT")
    @Expose
    private List<Object> irrgationadvisoryDT = null;
    @SerializedName("dynamicguidance")
    @Expose
    private Object dynamicguidance;

    public List<Object> getLstirrigationadvisory() {
        return lstirrigationadvisory;
    }

    public void setLstirrigationadvisory(List<Object> lstirrigationadvisory) {
        this.lstirrigationadvisory = lstirrigationadvisory;
    }

    public String getCurrentDASfromto() {
        return currentDASfromto;
    }

    public void setCurrentDASfromto(String currentDASfromto) {
        this.currentDASfromto = currentDASfromto;
    }

    public String getCurrentDatefrom() {
        return currentDatefrom;
    }

    public void setCurrentDatefrom(String currentDatefrom) {
        this.currentDatefrom = currentDatefrom;
    }

    public String getCurrentDateto() {
        return currentDateto;
    }

    public void setCurrentDateto(String currentDateto) {
        this.currentDateto = currentDateto;
    }

    public List<Object> getIrrgationadvisoryDT() {
        return irrgationadvisoryDT;
    }

    public void setIrrgationadvisoryDT(List<Object> irrgationadvisoryDT) {
        this.irrgationadvisoryDT = irrgationadvisoryDT;
    }

    public Object getDynamicguidance() {
        return dynamicguidance;
    }

    public void setDynamicguidance(Object dynamicguidance) {
        this.dynamicguidance = dynamicguidance;
    }

}
