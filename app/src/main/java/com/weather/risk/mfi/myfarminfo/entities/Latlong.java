package com.weather.risk.mfi.myfarminfo.entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by piyush on 9/29/2015.
 */
public class Latlong {

    Double lat;
    Double log;

    public Latlong(LatLng point) {
        this.lat = point.latitude;
        this.log = point.longitude;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }
}
