package com.weather.risk.mfi.myfarminfo.utils;

import java.util.HashMap;

/**
 */
public class StateName  {

    HashMap<String, String> stateName;



    public  StateName()
    {
        stateName = new HashMap<String, String>();
        stateName.put("Bihar","5");
        stateName.put("Uttar Pradesh","6");
        stateName.put("Rajasthan","7");


    }
    public String getStateID(String stateName){

        return this.stateName.get(stateName);

    }










}
