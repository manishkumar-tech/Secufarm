package com.weather.risk.mfi.myfarminfo.entities;

import android.content.ContentValues;
import android.util.Log;

import com.weather.risk.mfi.myfarminfo.utils.CustomHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by piyush on 10/1/2015.
 */
public class FeedBackData {

    private String yourName;
    private String yourEmail;
    private String yourPhoneNo;
    private String yourMessage;

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getYourEmail() {
        return yourEmail;
    }

    public void setYourEmail(String yourEmail) {
        this.yourEmail = yourEmail;
    }

    public String getYourPhoneNo() {
        return yourPhoneNo;
    }

    public void setYourPhoneNo(String yourPhoneNo) {
        this.yourPhoneNo = yourPhoneNo;
    }

    public String getYourMessage() {
        return yourMessage;
    }

    public void setYourMessage(String yourMessage) {
        this.yourMessage = yourMessage;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    public static String sentFeedBackData(FeedBackData feedBackData) {
        String message = "";

        String response = null;
        try {
                        //put                                                                       //change here method name
            response = CustomHttpClient.executeHttpPut("http://myfarminfo.com/yfirest.svc/saveRequest/" + feedBackData.getYourName() + "/" + feedBackData.getYourEmail() + "/" + feedBackData.getYourPhoneNo() + "/" + feedBackData.getYourMessage());
            System.out.println("response " + response);
            Log.d("FeedbackData", response);
//            response "[{\"UserID\":100055,\"VisibleName\":\"jyfjyd\",\"UserSince\":\"September 2015\"}]"
            JSONArray jArray = new JSONArray(response);
            if (jArray.length() == 0) {
                message = "you feedBack not sent successfully";
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
