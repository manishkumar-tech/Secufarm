package com.weather.risk.mfi.myfarminfo.mapfragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LatLonCellID extends Service {
    public static double lat = 0.0, lon = 0.0, speed = 0.0;
    public static String /*imeino,*/ datetimestamp = null;
    LocationManager _locationManager;
    LocationListener _connector;
    public static double currentLat = 0.0;
    public static double currentLon = 0.0;

    TelephonyManager tm;
//    GsmCellLocation gsm_cell_location;

    @Override
    public void onCreate() {
        try {
            tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//            imeino = tm.getDeviceId();
//            gsm_cell_location = (GsmCellLocation) tm.getCellLocation();

            //CellID = gsm_cell_location.getCid();
            turnGPSOn();
            _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            _connector = new MyLocationListener();
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _connector);
            _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _connector);

            final Timer timer = new Timer();

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(LatLonCellID.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(LatLonCellID.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _connector);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //      _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _connector);
                    if (AppManager.getInstance().isLocationServicesAvailable(LatLonCellID.this)) {
                        if (lat != 0.0 && lon != 0.0) {
                            if (AppConstant.isWrite) {
                                if (AppConstant.routeArray != null) {
                                    int ss = AppConstant.routeArray.size();
                                    if (ss > 0) {
                                        LatLng aa = AppConstant.routeArray.get(ss - 1);
                                        double distance = distFrom(lat, lon, aa.latitude, aa.longitude);
                                        Log.v("distance", "" + distance);
                                        if (distance < AppConstant.maxDistance) {
                                            AppConstant.routeArray.add(new LatLng(lat, lon));
                                        }
                                    } else {
                                        AppConstant.routeArray.add(new LatLng(lat, lon));
                                    }
                                }
                            }

                            LatLonCellID.currentLat = lat;
                            LatLonCellID.currentLon = lon;
                        } else {
                            //System.out.println("Trying to get GPS Signal");
                        }
                    } else {
                        lat = 0.0;
                        lon = 0.0;
                        LatLonCellID.currentLat = 0.0;
                        LatLonCellID.currentLon = 0.0;
                    }
                    //  lat = 0.0;
                    //
                    //   lon = 0.0;
                }

            }, 10000, 5000);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("Exception" + e);
        }
        //stopSelf();


    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);

        return dist;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private void turnGPSOn() {
        try {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (!provider.contains("gps")) { //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}

class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location loc) {
        try {
//            Toast.makeText(this, text, duration)
            LatLonCellID.lat = loc.getLatitude();
            LatLonCellID.lon = loc.getLongitude();
            LatLonCellID.speed = loc.getSpeed();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LatLonCellID.datetimestamp = sdf.format(new Date(loc.getTime()));

            System.out.println("Lat_Lon_CellID.No FOund " + loc);
            if (loc != null) {
                System.out.println("Lat_Lon_CellID.lat " + loc);
                //System.out.println("Lat_Lon_CellID.lon "+LatLonCellID.lon);
            }
            System.out.println("Get the GPS Signal" + "-" + LatLonCellID.lat + "-" + LatLonCellID.lon);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


}

