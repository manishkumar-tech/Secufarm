package com.weather.risk.mfi.myfarminfo.bean;

/**
 * Created by Admin on 11-10-2017.
 *
 *  "SNo": "1",
 "Stage": "Nursery",
 "Stage Water Requirement": "83",
 "Expected Rain": "2146",
 "Water Needed": "0"
 */
public class WaterBean {

    String SNo,stage,stage_Requirement,exp_rain,water_needed;

    public String getSNo() {
        return SNo;
    }

    public void setSNo(String SNo) {
        this.SNo = SNo;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStage_Requirement() {
        return stage_Requirement;
    }

    public void setStage_Requirement(String stage_Requirement) {
        this.stage_Requirement = stage_Requirement;
    }

    public String getExp_rain() {
        return exp_rain;
    }

    public void setExp_rain(String exp_rain) {
        this.exp_rain = exp_rain;
    }

    public String getWater_needed() {
        return water_needed;
    }

    public void setWater_needed(String water_needed) {
        this.water_needed = water_needed;
    }
}
