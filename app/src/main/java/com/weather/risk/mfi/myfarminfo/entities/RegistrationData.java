package com.weather.risk.mfi.myfarminfo.entities;

//import myfarminfo.com.weather.risk.myfarminfo.background.CustomHttpClient;

import android.content.ContentValues;
import android.util.Log;

import com.weather.risk.mfi.myfarminfo.utils.CustomHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by WRMS on 14-09-2015.
 */
public class RegistrationData {

    private String createUsername;
    private String visibleNamename;
    private String password;
    private String emailAddress;
    private String city;
    private String state;

    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getVisibleNamename() {
        return visibleNamename;
    }

    public void setVisibleNamename(String visibleNamename) {
        this.visibleNamename = visibleNamename;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static String register(RegistrationData data) {
        String message = "";

        String response = null;
        try {

            response = CustomHttpClient.executeHttpPut("https://myfarminfo.com/yfirest.svc/Register/" + data.getEmailAddress() + "/" + data.getCreateUsername() + "/" + data.getPassword() + "/" + data.getVisibleNamename());
            System.out.println("response " + response);
            Log.d("RegistrationData",response);
//            response "[{\"UserID\":100055,\"VisibleName\":\"jyfjyd\",\"UserSince\":\"September 2015\"}]"
            JSONArray jArray = new JSONArray(response);
            if (jArray.length() == 0) {
                message = "No Registered";
                return message;
            }

            for (int i = 0; i < jArray.length(); i++) {

                System.out.println(" i " + i);
                JSONObject jObject = jArray.getJSONObject(i);
                ContentValues values = new ContentValues();

//                    values.put(DBAdapter.VEHICAL_NAME,jObject.getString("UserID"));
                System.out.println("UserID " + jObject.getString("UserID"));

//                    values.put(DBAdapter.IMEI,jObject.getString("VisibleName"));
                System.out.println("VisibleName " + jObject.getString("VisibleName"));

//                    values.put(DBAdapter.VEHICAL_STATUS,jObject.getString("UserSince"));
                System.out.println("UserSince " + jObject.getString("UserSince"));

//                    long k = db.db.insert(DBAdapter.TABLE_VEHICAL, null, values);

//                    System.out.println(" k "+k);
            }
            return message;
        } catch (Exception e) {
//				System.out.println("locality_detail sync Error");
            e.printStackTrace();
            return "No Connection";
        }

    }

}
