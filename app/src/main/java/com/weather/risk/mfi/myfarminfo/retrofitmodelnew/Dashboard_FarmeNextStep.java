package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeNextStep {

    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("lstnextPop")
    @Expose
    private List<Object> lstnextPop = null;
    @SerializedName("diseasemessagelst")
    @Expose
    private List<Object> diseasemessagelst = null;
    @SerializedName("weathermessagelst")
    @Expose
    private List<Object> weathermessagelst = null;
    @SerializedName("lstnextPopDT")
    @Expose
    private List<Dashboard_FarmeLstnextPopDT> lstnextPopDT = null;
    @SerializedName("CurrentDASfromto")
    @Expose
    private String currentDASfromto;
    @SerializedName("CurrentDatefrom")
    @Expose
    private String currentDatefrom;
    @SerializedName("CurrentDateto")
    @Expose
    private String currentDateto;
    @SerializedName("lstExpertAdvisory")
    @Expose
    private List<Object> lstExpertAdvisory = null;
    @SerializedName("AutoIrrigationMessage")
    @Expose
    private Object autoIrrigationMessage;
    @SerializedName("soiladvisorymsg")
    @Expose
    private String soiladvisorymsg;
    @SerializedName("nDVIAdvisory")
    @Expose
    private Dashboard_FarmeNDVIAdvisory nDVIAdvisory;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Object> getLstnextPop() {
        return lstnextPop;
    }

    public void setLstnextPop(List<Object> lstnextPop) {
        this.lstnextPop = lstnextPop;
    }

    public List<Object> getDiseasemessagelst() {
        return diseasemessagelst;
    }

    public void setDiseasemessagelst(List<Object> diseasemessagelst) {
        this.diseasemessagelst = diseasemessagelst;
    }

    public List<Object> getWeathermessagelst() {
        return weathermessagelst;
    }

    public void setWeathermessagelst(List<Object> weathermessagelst) {
        this.weathermessagelst = weathermessagelst;
    }

    public List<Dashboard_FarmeLstnextPopDT> getLstnextPopDT() {
        return lstnextPopDT;
    }

    public void setLstnextPopDT(List<Dashboard_FarmeLstnextPopDT> lstnextPopDT) {
        this.lstnextPopDT = lstnextPopDT;
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

    public List<Object> getLstExpertAdvisory() {
        return lstExpertAdvisory;
    }

    public void setLstExpertAdvisory(List<Object> lstExpertAdvisory) {
        this.lstExpertAdvisory = lstExpertAdvisory;
    }

    public Object getAutoIrrigationMessage() {
        return autoIrrigationMessage;
    }

    public void setAutoIrrigationMessage(Object autoIrrigationMessage) {
        this.autoIrrigationMessage = autoIrrigationMessage;
    }

    public String getSoiladvisorymsg() {
        return soiladvisorymsg;
    }

    public void setSoiladvisorymsg(String soiladvisorymsg) {
        this.soiladvisorymsg = soiladvisorymsg;
    }

    public Dashboard_FarmeNDVIAdvisory getNDVIAdvisory() {
        return nDVIAdvisory;
    }

    public void setNDVIAdvisory(Dashboard_FarmeNDVIAdvisory nDVIAdvisory) {
        this.nDVIAdvisory = nDVIAdvisory;
    }
}
