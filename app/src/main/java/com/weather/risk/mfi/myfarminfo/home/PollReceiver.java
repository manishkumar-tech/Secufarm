package com.weather.risk.mfi.myfarminfo.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.weather.risk.mfi.myfarminfo.services.AuthenticateService;
import com.weather.risk.mfi.myfarminfo.services.BackgroundUploadService;

import java.lang.reflect.Method;

public class PollReceiver extends BroadcastReceiver {
    //public class PollReceiver extends WakefulBroadcastReceiver {
    //  private static final int PERIOD=1200000;//20*60*1000; 20 minutes
//    private static final int PERIOD = 900000;//15 min
//    private static final int PERIOD = 300000;//5 min
    private static final int PERIOD = 180000;//3 min
//  private static final int PERIOD=6000;//6 sec

    @Override
    public void onReceive(Context ctxt, Intent i) {
        scheduleAlarms(ctxt);
    }

    public static void scheduleAlarms(Context ctxt) {
        AlarmManager mgr =
                (AlarmManager) ctxt.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(ctxt, AuthenticateService.class);
//        Intent i = new Intent(ctxt, BackgroundUploadService.class);
        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, 0);
//        PendingIntent pi = PendingIntent.getService(ctxt, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);
//        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + PERIOD, PERIOD, pi);

//        Class<?> c = Class.forName("android.os.SystemProperties");
//        Method get = c.getMethod("get", String.class);
//        String miui = (String) get.invoke(c, "ro.miui.ui.version.name");
//        if (miui != null && miui.contains("11")) {
//            PermissionData mPopup = new PermissionData();
//            mPopup.text = "Other permissions > Display pop-up while in background";
//            mPopup.onClickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                    Uri uri = Uri.fromParts("package", getPackageName(), null);
//                    intent.setData(uri);
//                    startActivity(intent);
//                }
//            };
//
//            mPermissionData.add(mPopup);
//        }

    }
}
