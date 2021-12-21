package com.weather.risk.mfi.myfarminfo.firebasenotification;

public class NotificationData {
    String SMS;
    String Datetime;

    public NotificationData(String SMS, String Datetime) {
        this.SMS = SMS;
        this.Datetime = Datetime;
    }

    public String getSMS() {
        return SMS;
    }

    public String getDatetime() {
        return Datetime;
    }


}