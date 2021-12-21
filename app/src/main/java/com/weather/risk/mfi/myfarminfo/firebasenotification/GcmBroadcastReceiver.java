package com.weather.risk.mfi.myfarminfo.firebasenotification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.weather.risk.mfi.myfarminfo.services.MFI_FirebaseSMSService;

//import me.leolin.shortcutbadger.ShortcutBadger;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                MFI_FirebaseSMSService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
//        int badgeCount = 1;
//        ShortcutBadger.applyCount(context, badgeCount); //for 1.1.4+
    }
}
