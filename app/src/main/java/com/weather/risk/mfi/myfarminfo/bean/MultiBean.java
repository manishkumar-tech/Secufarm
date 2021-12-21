package com.weather.risk.mfi.myfarminfo.bean;

/**
 * Created by Admin on 28-09-2017.
 */
public class MultiBean   {

    public String crop_name;
    public String crop_id;
    public boolean box;
   // public String action;

    public MultiBean(String _describe, boolean _box, String _id) {
        crop_name = _describe;
        crop_id = _id;
        box = _box;
       // action = actio;
    }
}