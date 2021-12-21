package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard_FarmeForecastdatum {
    @SerializedName("DateTime")
    @Expose
    private String dateTime;
    @SerializedName("DataType")
    @Expose
    private String dataType;
    @SerializedName("MaxTemp")
    @Expose
    private Double maxTemp;
    @SerializedName("MinTemp")
    @Expose
    private Double minTemp;
    @SerializedName("Rain")
    @Expose
    private Double rain;
    @SerializedName("Humidity")
    @Expose
    private String humidity;
    @SerializedName("HumMor")
    @Expose
    private Double humMor;
    @SerializedName("HumEve")
    @Expose
    private Double humEve;
    @SerializedName("MaxWindspeed")
    @Expose
    private Double maxWindspeed;
    @SerializedName("FeelsLike")
    @Expose
    private String feelsLike;
    @SerializedName("WindDirection")
    @Expose
    private String windDirection;
    @SerializedName("SolarRadiation")
    @Expose
    private String solarRadiation;
    @SerializedName("RainAlert")
    @Expose
    private Object rainAlert;
    @SerializedName("RainProbality")
    @Expose
    private Object rainProbality;
    @SerializedName("TempAlert")
    @Expose
    private Object tempAlert;
    @SerializedName("LogDate")
    @Expose
    private String logDate;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("RefId")
    @Expose
    private String refId;
    @SerializedName("RefLoc")
    @Expose
    private String refLoc;
    @SerializedName("RefDis")
    @Expose
    private Object refDis;
    @SerializedName("WeatherCondition")
    @Expose
    private String weatherCondition;
    @SerializedName("NewDateTime")
    @Expose
    private String newDateTime;
    @SerializedName("RH")
    @Expose
    private String rH;
    @SerializedName("AvgTemp")
    @Expose
    private String avgTemp;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getRain() {
        return rain;
    }

    public void setRain(Double rain) {
        this.rain = rain;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public Double getHumMor() {
        return humMor;
    }

    public void setHumMor(Double humMor) {
        this.humMor = humMor;
    }

    public Double getHumEve() {
        return humEve;
    }

    public void setHumEve(Double humEve) {
        this.humEve = humEve;
    }

    public Double getMaxWindspeed() {
        return maxWindspeed;
    }

    public void setMaxWindspeed(Double maxWindspeed) {
        this.maxWindspeed = maxWindspeed;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getSolarRadiation() {
        return solarRadiation;
    }

    public void setSolarRadiation(String solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public Object getRainAlert() {
        return rainAlert;
    }

    public void setRainAlert(Object rainAlert) {
        this.rainAlert = rainAlert;
    }

    public Object getRainProbality() {
        return rainProbality;
    }

    public void setRainProbality(Object rainProbality) {
        this.rainProbality = rainProbality;
    }

    public Object getTempAlert() {
        return tempAlert;
    }

    public void setTempAlert(Object tempAlert) {
        this.tempAlert = tempAlert;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getRefLoc() {
        return refLoc;
    }

    public void setRefLoc(String refLoc) {
        this.refLoc = refLoc;
    }

    public Object getRefDis() {
        return refDis;
    }

    public void setRefDis(Object refDis) {
        this.refDis = refDis;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getNewDateTime() {
        return newDateTime;
    }

    public void setNewDateTime(String newDateTime) {
        this.newDateTime = newDateTime;
    }

    public String getRH() {
        return rH;
    }

    public void setRH(String rH) {
        this.rH = rH;
    }

    public String getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(String avgTemp) {
        this.avgTemp = avgTemp;
    }

}
