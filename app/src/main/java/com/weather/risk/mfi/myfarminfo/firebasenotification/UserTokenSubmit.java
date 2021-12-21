package com.weather.risk.mfi.myfarminfo.firebasenotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.database.DBAdapter;
import com.weather.risk.mfi.myfarminfo.utils.AppConstant;
import com.weather.risk.mfi.myfarminfo.utils.AppManager;
import com.weather.risk.mfi.myfarminfo.volley_class.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CheckTokenKey_Daily;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CheckTokenKey_DailyUpdateOrNot;
import static com.weather.risk.mfi.myfarminfo.utils.AppConstant.CheckTokenKey_DailyUpdate_value;

public class UserTokenSubmit {

    //First time and Update the token for user active tracking base on date
    public static void uploadUserTkenDeviceID(final Context context, JSONObject Userid_Key) {
        try {
            new setUserTokenSubmit(context, Userid_Key.toString()).execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class setUserTokenSubmit extends AsyncTask<Void, Void, String> {
        String JSON;
        ProgressDialog progressDialog;
        Context context;

        public setUserTokenSubmit(final Context contexts, String JSONs) {
            this.JSON = JSONs;
            this.context = contexts;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.Processing));
            progressDialog.setCancelable(true);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String sendPath = AppManager.getInstance().updateDeviceTokenURL;
            response = AppManager.getInstance().httpRequestPutMethod(sendPath, JSON);
            System.out.println("AllResponse :---" + response);
//            response = response.substring(1, response.length() - 1);
            response = "{" + response + "}";
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPreExecute();
            if (response != null) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.get("updateDeviceTokenResult").toString().equalsIgnoreCase("Success")) {
                        SharedPreferences myPreference = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPreference.edit();
                        editor.putString("CheckRegisterDeviceTokenKey", "AlreadyUpload");
                        //Daily check
                        editor.putString(CheckTokenKey_Daily, CheckTokenKey_DailyUpdate_value());
                        editor.putString(CheckTokenKey_DailyUpdateOrNot, "AlreadyUpdate");
                        editor.apply();
                        AppConstant.CheckRegisterDeviceTokenKey = "AlreadyUpload";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, context.getResources().getString(R.string.ResponseFormattingError), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.ServerError), Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }


    public static boolean checkTodayTokenKeyUpdatedOrNot(final Context context) {
        boolean value = false;
        try {
            String TodayDate = CheckTokenKey_DailyUpdate_value();
            SharedPreferences prefs = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
            String CheckDate = prefs.getString(CheckTokenKey_Daily, null);
            if (CheckDate != null && CheckDate.length() > 0 && CheckDate.equalsIgnoreCase(TodayDate)) {
                //Yes today key already generated
                value = checkTodayToeknKeyUpdateorNot(context);
            } else {
                //No key is generated for today
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString(CheckTokenKey_Daily, TodayDate);
//                editor.putString(CheckTokenKey_DailyUpdateOrNot, "PendingUpdate");
//                editor.apply();
                value = false;
            }

        } catch (Exception ex) {
            value = false;
            ex.printStackTrace();
        }
        return value;

    }

    public static boolean checkTodayToeknKeyUpdateorNot(final Context context) {
        boolean value = false;
        SharedPreferences prefs = context.getSharedPreferences(AppConstant.SHARED_PREFRENCE_NAME, MODE_PRIVATE);
        String valueDailyUpdate = prefs.getString(CheckTokenKey_DailyUpdateOrNot, null);
        if (valueDailyUpdate != null && valueDailyUpdate.length() > 0 && valueDailyUpdate.equalsIgnoreCase("AlreadyUpdate")) {
            value = true;
        }
        return value;

    }


}
