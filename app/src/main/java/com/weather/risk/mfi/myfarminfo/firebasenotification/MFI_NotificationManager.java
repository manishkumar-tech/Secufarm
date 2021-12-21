package com.weather.risk.mfi.myfarminfo.firebasenotification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.transition.Visibility;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;
import com.weather.risk.mfi.myfarminfo.activities.NewHomeScreen;
import com.weather.risk.mfi.myfarminfo.live_cotton.ChatRoomThreadAdapter;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;

import java.util.List;

public class MFI_NotificationManager {

    private Context mCtx;
    private static MFI_NotificationManager mInstance;

    private MFI_NotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MFI_NotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MFI_NotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String Messgae, String notftytype, String StepID, String FarmID, String Title, String tokenID,
                                    final String NotifImageURL, final Bitmap bitmap) {

//         imagePath = "http://pdjalna.apimfi.com/LogImage/626b67fa-6b45-46b2-940e-fba9c9f70b14.jpg";

        NotificationCountSMS.setNotificationValueData(mCtx, Messgae, notftytype, StepID, FarmID, Title, tokenID, NotifImageURL);

        final RemoteViews contentView;
        contentView = new RemoteViews(mCtx.getPackageName(), R.layout.custom_notification);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher_mfi);
        if (NotifImageURL != null) {
            contentView.setViewVisibility(R.id.image, View.VISIBLE);
            contentView.setImageViewBitmap(R.id.image, bitmap);
        } else {
            contentView.setViewVisibility(R.id.image, View.GONE);
        }
        contentView.setTextViewText(R.id.title, Title);
        contentView.setTextViewText(R.id.text, Messgae);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mCtx, mCtx.getResources().getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher_mfi)
                .setLargeIcon(bitmap) /*Notification icon image*/
//                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                .setNumber(1)
                .setContent(contentView);
        NotificationManager notificationManager = (NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibrate = {0, 100, 200, 300};

        notification.setSound(sound);
        notification.setVibrate(vibrate);
        notification.setAutoCancel(true);
        //If device is Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(mCtx.getResources().getString(R.string.default_notification_channel_id),
                    "MFI Notification", NotificationManager.IMPORTANCE_DEFAULT);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.enableVibration(true);
            channel.setSound(sound, attributes);
            channel.setShowBadge(true);
            notification.setChannelId(mCtx.getResources().getString(R.string.default_notification_channel_id));
            notificationManager.createNotificationChannel(channel);
        }
        // Create an Intent for the activity you want to start
        Intent resultIntent = null;
        if (AppConstant.isLogin = true && AppConstant.user_id != null) {
            resultIntent = new Intent(mCtx, MainProfileActivity.class);
        } else {
            resultIntent = new Intent(mCtx, NewHomeScreen.class);
        }
        resultIntent.putExtra("Messgae", Messgae);
        resultIntent.putExtra("notftytype", notftytype);
        resultIntent.putExtra("StepID", StepID);
        resultIntent.putExtra("FarmID", FarmID);
        resultIntent.putExtra("Title", Title);
        resultIntent.putExtra("tokenID", tokenID);
        resultIntent.putExtra("NotifImageURL", NotifImageURL);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 1,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
//        notificationManager.notify(11, notification.build()); //This line will not call with Image Loading bacuz this will show 2 notification for once

        //for IMAGE Loading
        final Notification notifications = notification.build();
        // set big content view for newer androids
        if (Build.VERSION.SDK_INT >= 16) {
            notification.setContent(contentView);
        }
        try {
//            if (NotifImageURL != null && NotifImageURL.length() > 0) {
//                Handler uiHandler = new Handler(Looper.getMainLooper());
//                uiHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Picasso.with(mCtx).load(NotifImageURL).into(contentView, R.id.image, R.string.default_notification_channel_id, notifications);
//                    }
//                });
//            } else {
//                //If device is Android 8+
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    NotificationChannel channel = new NotificationChannel(mCtx.getResources().getString(R.string.default_notification_channel_id),
////                            "MFI Notification", NotificationManager.IMPORTANCE_DEFAULT);
////                    AudioAttributes attributes = new AudioAttributes.Builder()
////                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
////                            .build();
////                    channel.enableVibration(true);
////                    channel.setSound(sound, attributes);
////                    notification.setChannelId(mCtx.getResources().getString(R.string.default_notification_channel_id));
////                    notificationManager.createNotificationChannel(channel);
////                }
//                //Image URL is not Exist then Normal Notification will be called
//                notificationManager.notify(11, notification.build());
//            }
            notificationManager.notify(11, notification.build());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    //Check Application is ruuing or not
    public boolean ApplicationCheckRunning() {
        if (isAppRunning(mCtx, "com.weather.risk.mfi.myfarminfo")) {
            // App is running
            return true;
        } else {
            // App is not running
            return false;
        }
    }


    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}