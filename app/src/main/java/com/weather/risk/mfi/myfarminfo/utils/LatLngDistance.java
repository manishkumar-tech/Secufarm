package com.weather.risk.mfi.myfarminfo.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLat;
import static com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID.currentLon;

public class LatLngDistance {

    public static Double getDistanceLatLng(LatLng point1, LatLng point2) {
        if (point1 == null || point2 == null) {
            return null;
        }
        return SphericalUtil.computeDistanceBetween(point1, point2);
    }

    //LatLonCellID
//    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
    public static double getDistanceLatLng(double lat1, double lng1, double lat2, double lng2) {
        double dist = 0;
        try {
            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(lat2 - lat1);
            double dLng = Math.toRadians(lng2 - lng1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLng / 2) * Math.sin(dLng / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            dist = (double) (earthRadius * c);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dist;
    }

    public static Integer getDistanceLagLong(Double FarmerLag, Double FarmerLong) {
        int distancevalue = 0;
        try {
            LatLng FarmerLocation = new LatLng(FarmerLag, FarmerLong);
            if (currentLat > 0 && currentLon > 0) {
                LatLng currentLocation = new LatLng(currentLat, currentLon);

                Double Dist_meter = getDistanceLatLng(currentLocation, FarmerLocation);
//                Double Dist_meter = getDistanceLatLng(currentLocation.latitude, currentLocation.longitude,
//                        FarmerLocation.latitude, FarmerLocation.longitude);
//                String Dist_meter_new = String.format("%.2f", Dist_meter);
                String Dist_meter_new = String.format("%.0f", Dist_meter);
//                Double Distance_km = Double.valueOf(Dist_meter_new) / 1000;//COnvert meter to KM
                Double Distance_km = Double.valueOf(Dist_meter_new);//In  KM
//                String Value = String.format("%.2f", Distance_km);
                String Value = String.format("%.0f", Distance_km);
//                distancevalue = Double.valueOf(Value);
                distancevalue = Integer.valueOf(Value);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return distancevalue;
    }
}
