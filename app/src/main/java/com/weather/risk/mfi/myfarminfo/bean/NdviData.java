package com.weather.risk.mfi.myfarminfo.bean;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Admin on 03-05-2018.
 *
 * GridID"));
 data.setLatitudeFrom(locationObject.getString("LatFrom"));
 data.setLongitudeFrom(locationObject.getString("LonFrom"));
 data.setLatitudeTo(locationObject.getString("LatTo"));
 data.setLongitudeTo(locationObject.getString("LonTo"));
 data.setNdvi(locationObject.getString("NDVI"));
 data.setLocation(locationObject.getString("Location"));
 */
public class NdviData {

    String GridID;
    String LatFrom;
    String LonFrom;
    String LatTo;
    String LonTo;
    String NDVI;
    String Location;
    Marker marker;

    public String getGridID() {
        return GridID;
    }

    public void setGridID(String gridID) {
        GridID = gridID;
    }

    public String getLatFrom() {
        return LatFrom;
    }

    public void setLatFrom(String latFrom) {
        LatFrom = latFrom;
    }

    public String getLonFrom() {
        return LonFrom;
    }

    public void setLonFrom(String lonFrom) {
        LonFrom = lonFrom;
    }

    public String getLonTo() {
        return LonTo;
    }

    public void setLonTo(String lonTo) {
        LonTo = lonTo;
    }

    public String getLatTo() {
        return LatTo;
    }

    public void setLatTo(String latTo) {
        LatTo = latTo;
    }

    public String getNDVI() {
        return NDVI;
    }

    public void setNDVI(String NDVI) {
        this.NDVI = NDVI;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
