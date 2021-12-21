package com.weather.risk.mfi.myfarminfo.bean;

import java.io.Serializable;

/**
 * Created by Admin on 04-04-2018.
 */
public class Min1Bean implements Serializable {

    String vValue;
    String yValue;



    public String getvValue() {
        return vValue;
    }

    public void setvValue(String vValue) {
        this.vValue = vValue;
    }

    public String getyValue() {
        return yValue;
    }

    public void setyValue(String yValue) {
        this.yValue = yValue;
    }

}
