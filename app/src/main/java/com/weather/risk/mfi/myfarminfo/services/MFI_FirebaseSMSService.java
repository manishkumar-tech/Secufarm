package com.weather.risk.mfi.myfarminfo.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.weather.risk.mfi.myfarminfo.firebasenotification.MFI_NotificationManager;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Map;

//import me.leolin.shortcutbadger.ShortcutBadger;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MFI_FirebaseSMSService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    private static String message_notification = null;
    private static String title_notification = null;
    private static String DISPLAY_MESSAGE_ACTION = "com.weather.risk.mfi.myfarminfo";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Displaying data in log
        //It is optional
        Log.d("PushNotifications", remoteMessage.getData().toString());
        // Log.d(TAG, "Notification: " + remoteMessage.getNotification().toString());
       /* Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "From: " + remoteMessage.getData().get("custom").toString());

        String data = remoteMessage.getData().get("custom").toString();
        try {
            JSONObject js = new JSONObject(data.toString());
            String pushType = js.getString("push_type");

            Log.d(TAG, "From: " + pushType);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        // sendNotification(remoteMessage.getData().get("message"));
//        sendNotification("New notification from Jalna");

        String time, message, tokenID = null, Messgae = "", notftytype = "normal", StepID = "0", FarmID = "0", Title = "",
                NotifImageURL = "", image = "", TrueOrFlase = "";
        Bitmap bitmap = null;
        Uri imageurl = null;
        try {
            if (remoteMessage.getData().size() > 0) {
                //handle the data message here
                Map<String, String> map = remoteMessage.getData();
                message = map.get("message");
                time = map.get("time");
                Messgae = map.get("Messgae");
                notftytype = map.get("notftytype");
                StepID = map.get("StepID");
                FarmID = map.get("FarmID");
                Title = map.get("Title");
                tokenID = map.get("tokenID");
                NotifImageURL = map.get("NotifImageURL");
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }

        if (!Utility.setStringCheck(Messgae)) {
            Messgae = null;
        }
        if (!Utility.setStringCheck(notftytype)) {
            notftytype = null;
        }
        if (!Utility.setStringCheck(StepID)) {
            StepID = null;
        }

        if (!Utility.setStringCheck(FarmID)) {
            FarmID = null;
        }

        if (!Utility.setStringCheck(Title)) {
            Title = null;
        }

        if (!Utility.setStringCheck(tokenID)) {
            tokenID = null;
        }

        if (!Utility.setStringCheck(NotifImageURL)) {
            NotifImageURL = null;
        }

//        int badgeCount = 1;
//        ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
//        if (remoteMessage == null)
//            return;

//        if (AppConstant.user_id != null && AppConstant.user_id.length() > 0) {
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            message_notification = remoteMessage.getNotification().getBody();
            title_notification = remoteMessage.getNotification().getTitle();
            imageurl = remoteMessage.getNotification().getImageUrl();
            if (imageurl != null) {
                image = String.valueOf(imageurl);
            }
            TrueOrFlase = remoteMessage.getData().get("AnotherActivity");

            if (Messgae == null) {
                Messgae = message_notification;
            }
            if (Title == null) {
                Title = title_notification;
                notftytype = "normal";
            }
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                String aap = object.getString("Parameters");
                JSONArray array = new JSONArray(aap);
                JSONObject obj = new JSONObject(array.get(0).toString());
                Messgae = obj.getString("Messgae");
                notftytype = obj.getString("notftytype");
                StepID = obj.getString("StepID");
                FarmID = obj.getString("FarmID");
                Title = obj.getString("Title");
                tokenID = obj.getString("tokenID");
                NotifImageURL = obj.getString("NotifImageURL");
//                image = obj.getString("image");
                TrueOrFlase = obj.getString("AnotherActivity");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            message_notification = remoteMessage.getData().toString();
            if (Messgae == null) {
                Messgae = message_notification;
                notftytype = "normal";
            }
            try {
                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                String aap = object.getString("Parameters");
                JSONArray array = new JSONArray(aap);
                JSONObject obj = new JSONObject(array.get(0).toString());
                Messgae = obj.getString("Messgae");
                notftytype = obj.getString("notftytype");
                StepID = obj.getString("StepID");
                FarmID = obj.getString("FarmID");
                Title = obj.getString("Title");
                tokenID = obj.getString("tokenID");
                NotifImageURL = obj.getString("NotifImageURL");
//                image = obj.getString("image");
                TrueOrFlase = obj.getString("AnotherActivity");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Log.e("message", "Data: " + message_notification);
        try {
            //To get a Bitmap image from the URL received
            if (image != null && image.length() > 2) {
                bitmap = getBitmapfromUrl(image);
//                bitmap = downloadImage(NotifImageURL);
            } else if (NotifImageURL != null && NotifImageURL.length() > 2) {
                bitmap = getBitmapfromUrl(NotifImageURL);
//                bitmap = downloadImage(NotifImageURL);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        displayMessage(getApplicationContext(), Messgae, Title, notftytype);
        MFI_NotificationManager.getInstance(this).displayNotification(Messgae, notftytype, StepID, FarmID, Title, tokenID, NotifImageURL, bitmap);
    }

    public void displayMessage(Context context, String messages, String Title, String notftytype) {
        String message = messages;
//            message = StringEscapeUtils.unescapeJava(message)
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra("Messgae", message);
        intent.putExtra("Title", Title);
        intent.putExtra("notftytype", notftytype);
        context.sendBroadcast(intent);
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(this, token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    public static String sendRegistrationToServer(Context context, String token) {
        SharedPreferences myPreference = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME_Token, MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreference.edit();
        AppConstant.RegisterDeviceTokenKey = myPreference.getString("RegisterDeviceTokenKey", null);
        if (AppConstant.RegisterDeviceTokenKey == null || AppConstant.RegisterDeviceTokenKey.equalsIgnoreCase("")) {
            // Save to SharedPreferences
            editor.putString("RegisterDeviceTokenKey", token);
            editor.putString("CheckRegisterDeviceTokenKey", "PendingUpload");
            editor.apply();
            AppConstant.RegisterDeviceTokenKey = token;
            AppConstant.CheckRegisterDeviceTokenKey = "PendingUpload";
        }
        //You can implement this method to store the token on your server
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //Not required for current project
        return token;
    }

//    @Override
//    public void handleIntent(Intent intent) {
//        super.handleIntent(intent);
//
//        // you can get ur data here
//        //intent.getExtras().get("your_data_key")
//
//    }

    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }



}