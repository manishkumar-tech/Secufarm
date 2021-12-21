package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.android.gms.maps.model.LatLng;

public class MyItem  {
    public static final int VIEW_TEXT = 1;
    public static final int VIEW_MAP = 2;

    public final int viewType;
    public String text;
    public LatLng position;

    public MyItem(String value) {
        this.viewType = VIEW_TEXT;
        this.text = value;
    }

    public MyItem(LatLng value) {
        this.viewType = VIEW_MAP;
        this.position = value;
    }
}
