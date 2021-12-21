package com.weather.risk.mfi.myfarminfo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AlarmReceiver extends BroadcastReceiver {


    Context context;
    public String deviceID;

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences prefs = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, context.MODE_PRIVATE);
        deviceID = prefs.getString(AppConstant.PREFRENCE_KEY_MOBILE, null);
        AppConstant.mobile_no = deviceID;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
      //  deviceID = "8285686540";

        String output = String.format("%02d:%02d",  hour, minute);

        Log.v("datetime",output);

        SharedPreferences prf = context.getSharedPreferences("irrigation", context.MODE_PRIVATE);

        String st = prf.getString("start_time", null);
        String sd = prf.getString("start_date", null);
        String sh = prf.getString("start_hours", null);
        String oo = prf.getString("on_off", null);

      //  Toast.makeText(context,"Alarm Testing",Toast.LENGTH_SHORT).show();

        Log.v("alarm_reciever",""+ oo+"--"+ sh);

        if (st!=null && sh!=null) {

            Log.v("alarm_reciever1",""+ oo+"--"+ sh);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date1 = null;
            Date date2 = null;
            try {
                date2 = simpleDateFormat.parse(output);
                date1 = simpleDateFormat.parse(sh);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long difference = date2.getTime() - date1.getTime();
            int dys = (int) (difference / (1000 * 60 * 60 * 24));
            int hrs = (int) ((difference - (1000 * 60 * 60 * 24 * dys)) / (1000 * 60 * 60));
            int minn = (int) (difference - (1000 * 60 * 60 * 24 * dys) - (1000 * 60 * 60 * hrs)) / (1000 * 60);
            int aa = (hrs*60)+minn;



            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
            Date date11 = null;
            Date date21 = null;
            try {
                date21 = simpleDateFormat1.parse(output);
                date11 = simpleDateFormat1.parse(st);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference1 = date21.getTime() - date11.getTime();
            int dys1 = (int) (difference1 / (1000 * 60 * 60 * 24));
            int hrs1 = (int) ((difference1 - (1000 * 60 * 60 * 24 * dys1)) / (1000 * 60 * 60));
            int minn1 = (int) (difference1 - (1000 * 60 * 60 * 24 * dys1) - (1000 * 60 * 60 * hrs1)) / (1000 * 60);
            int aa1 = (hrs1*60)+minn1;


            if (aa1>=0){
                try {

                    if (deviceID!=null) {
                        if (oo != null && oo.equalsIgnoreCase("#9:Y;")) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(deviceID, null, "#9:Y;", null, null);

                            SharedPreferences prefs1 = context.getSharedPreferences("irrigation", context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = prefs1.edit();

                            editor1.putString("on_off", "#9:N;");
                            editor1.apply();

                        }
                    }


                } catch (Exception ex) {

                    ex.printStackTrace();
                }

            }

            if (oo != null && oo.equalsIgnoreCase("#9:N;")) {
                if (aa >=0) {



                        try {

                            if (deviceID!=null) {

                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(deviceID, null, "#9:N;", null, null);

                                SharedPreferences pref = context.getSharedPreferences("irrigation", context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();

                            }


                        } catch (Exception ex) {

                            ex.printStackTrace();
                        }


                }
            }
        }

    }


}