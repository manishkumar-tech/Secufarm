package com.weather.risk.mfi.myfarminfo.bean;

import java.io.Serializable;

/**
 * Created by Admin on 09-04-2018.
 */
public class MotorStatus implements Serializable {

    String date;
    String startTime;
    String stopTime;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }


}
