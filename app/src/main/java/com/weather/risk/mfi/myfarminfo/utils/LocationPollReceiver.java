package com.weather.risk.mfi.myfarminfo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.weather.risk.mfi.myfarminfo.mapfragments.LatLonCellID;


/**
 * Created by Admin on 23-08-2016.
 */
public class LocationPollReceiver extends BroadcastReceiver {

    private static final int PERIOD = 5000;//5 secs

    @Override
    public void onReceive(Context ctxt, Intent i) {
        scheduleAlarms(ctxt);
    }

    private static int count = 0;

    public static void scheduleAlarms(Context ctxt) {

        /*final LocationManager manager = (LocationManager) ctxt.getSystemService(Context.LOCATION_SERVICE);
        if ( manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            Intent intent = new Intent(ctxt, weatherrisk.com.wrms.cce.app.LocationService.class);
            ctxt.startService(intent);
        }*/

        System.out.println("scheduleAlarms has been invoked : "+count);
        AlarmManager mgr =
                (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, LatLonCellID.class);
        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, 0);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(), PERIOD, pi);
        count++;

    }
}
