package com.weather.risk.mfi.myfarminfo.firebasenotification;

import android.content.Context;
import android.content.SharedPreferences;

import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class NotificationCountSMS {

    public static void getNotificationCount(Context context) {
        ArrayList<String> totalvalue = getNotificationValueData(context);
//        if (totalvalue.size() > 0) { //have to reset the Notification even the coount is 0
        totalvalue.removeAll(Collections.singleton(null));
        totalvalue.removeAll(Collections.singleton(""));
        AppConstant.NotificationCountNo_badge = totalvalue.size();
//        }
    }

    public static void setNotificationValueData(Context context, String Messgae, String notftytype, String StepID,
                                                String FarmID, String Title, String tokenID, String NotifImageURL) {
        SharedPreferences notificationShrpre = context.getSharedPreferences("appNotification.xml", MODE_PRIVATE);
        SharedPreferences.Editor notificationData = notificationShrpre.edit();
//        if (tokenID != null && tokenID.equalsIgnoreCase(AppConstant.RegisterDeviceTokenKey)) {
        if (Messgae != null) {
//            notificationData.putString(Title, Messgae + "#" + notftytype + "#" + StepID + "#" + FarmID + "#" + Title + "#" + Utility.getdate() + " " + Utility.gettime() + "#" + NotifImageURL);
            notificationData.putString(StepID, Messgae + "#" + notftytype + "#" + StepID + "#" + FarmID + "#" + Title + "#" + Utility.getdate() + " " + Utility.gettime() + "#" + NotifImageURL);
        }
        notificationData.commit();
        getNotificationCount(context);
    }

    public static void setNotificationValueData_AddFeedback(Context context, String Messgae, String notftytype, String StepID,
                                                            String FarmID, String Title, String tokenID, String NotifImageURL, String FeedbacksStatus) {
        SharedPreferences notificationShrpre = context.getSharedPreferences("appNotification.xml", MODE_PRIVATE);
        SharedPreferences.Editor notificationData = notificationShrpre.edit();
//        if (tokenID != null && tokenID.equalsIgnoreCase(AppConstant.RegisterDeviceTokenKey)) {
        if (Messgae != null) {
//            notificationData.putString(Title, Messgae + "#" + notftytype + "#" + StepID + "#" + FarmID + "#" + Title + "#" + Utility.getdate() + " " + Utility.gettime() + "#" + NotifImageURL + "#" + FeedbacksStatus);
            notificationData.putString(StepID, Messgae + "#" + notftytype + "#" + StepID + "#" + FarmID + "#" + Title + "#" + Utility.getdate() + " " + Utility.gettime() + "#" + NotifImageURL + "#" + FeedbacksStatus);
        }
        notificationData.commit();
        getNotificationCount(context);
    }

    public static void setRemoveNoficationKeyValue(Context context, String StepID, String flags) {
        SharedPreferences notificationShrpre = context.getSharedPreferences("appNotification.xml", MODE_PRIVATE);
        SharedPreferences.Editor notificationData = notificationShrpre.edit();
        if (flags.equalsIgnoreCase("removesinglevalues")) {
            notificationData.remove(StepID); //we are removing only one key and value
        } else if (flags.equalsIgnoreCase("removeallvalues")) {
            notificationData.clear(); //we are removing all values and key
        }
        notificationData.commit();
        getNotificationCount(context);
    }

    public static ArrayList<String> getNotificationValueData(Context context) {
        SharedPreferences notificationShrpre = context.getSharedPreferences("appNotification.xml", MODE_PRIVATE);
        HashMap<String, String> map = (HashMap<String, String>) notificationShrpre.getAll();
        ArrayList<String> value = new ArrayList<>();
        List<String> maplist = new ArrayList(map.keySet());
        for (int i = 0; i < map.keySet().size(); i++) {
            //check 7 days less or not
            try {
                String value1 = map.get(maplist.get(i));
                String[] data = value1.split("#");
                String notftytype = data[1];
                boolean check7dayslessornot = Utility.checkNoofDaysislessthan7or3(Utility.getdate() + " " + Utility.gettime(), data[5], notftytype);
                if (check7dayslessornot) {
                    value.add(map.get(maplist.get(i)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return value;
    }

}
