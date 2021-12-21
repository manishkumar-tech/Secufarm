package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmerResponse {

    @SerializedName("NextStep")
    @Expose
    private Dashboard_FarmeNextStep nextStep;
    @SerializedName("cropStatus")
    @Expose
    private Dashboard_FarmeCropStatus cropStatus;
    @SerializedName("IrrigationAdvisory")
    @Expose
    private Dashboard_FarmerIrrigationAdvisory irrigationAdvisory;
    @SerializedName("weatherForecast")
    @Expose
    private Dashboard_FarmeWeatherForecast weatherForecast;
    @SerializedName("PestDiseaseAlert")
    @Expose
    private Dashboard_FarmePestDiseaseAlert pestDiseaseAlert;
    @SerializedName("PriceOutlook")
    @Expose
    private Dashboard_FarmePriceOutlook priceOutlook;
    @SerializedName("SoilTest")
    @Expose
    private Dashboard_FarmeSoilTest soilTest;
    @SerializedName("FarmScoreMessage")
    @Expose
    private String farmScoreMessage;
    @SerializedName("FarmScore")
    @Expose
    private String farmScore;

    @SerializedName("Logo")
    @Expose
    private String Logo;

    @SerializedName("SMSLst")
    @Expose
    private List<SMSLst> sMSLst = null;

    public Dashboard_FarmeNextStep getNextStep() {
        return nextStep;
    }

    public void setNextStep(Dashboard_FarmeNextStep nextStep) {
        this.nextStep = nextStep;
    }

    public Dashboard_FarmeCropStatus getCropStatus() {
        return cropStatus;
    }

    public void setCropStatus(Dashboard_FarmeCropStatus cropStatus) {
        this.cropStatus = cropStatus;
    }

    public Dashboard_FarmerIrrigationAdvisory getIrrigationAdvisory() {
        return irrigationAdvisory;
    }

    public void setIrrigationAdvisory(Dashboard_FarmerIrrigationAdvisory irrigationAdvisory) {
        this.irrigationAdvisory = irrigationAdvisory;
    }

    public Dashboard_FarmeWeatherForecast getWeatherForecast() {
        return weatherForecast;
    }

    public void setWeatherForecast(Dashboard_FarmeWeatherForecast weatherForecast) {
        this.weatherForecast = weatherForecast;
    }

    public Dashboard_FarmePestDiseaseAlert getPestDiseaseAlert() {
        return pestDiseaseAlert;
    }

    public void setPestDiseaseAlert(Dashboard_FarmePestDiseaseAlert pestDiseaseAlert) {
        this.pestDiseaseAlert = pestDiseaseAlert;
    }

    public Dashboard_FarmePriceOutlook getPriceOutlook() {
        return priceOutlook;
    }

    public void setPriceOutlook(Dashboard_FarmePriceOutlook priceOutlook) {
        this.priceOutlook = priceOutlook;
    }

    public Dashboard_FarmeSoilTest getSoilTest() {
        return soilTest;
    }

    public void setSoilTest(Dashboard_FarmeSoilTest soilTest) {
        this.soilTest = soilTest;
    }

    public String getFarmScoreMessage() {
        return farmScoreMessage;
    }

    public void setFarmScoreMessage(String farmScoreMessage) {
        this.farmScoreMessage = farmScoreMessage;
    }

    public String getFarmScore() {
        return farmScore;
    }

    public void setFarmScore(String farmScore) {
        this.farmScore = farmScore;
    }

    public List<SMSLst> getSMSLst() {
        return sMSLst;
    }

    public void setSMSLst(List<SMSLst> sMSLst) {
        this.sMSLst = sMSLst;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }
}

