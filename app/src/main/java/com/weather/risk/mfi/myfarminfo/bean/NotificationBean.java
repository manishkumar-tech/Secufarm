package com.weather.risk.mfi.myfarminfo.bean;

import java.io.Serializable;

public class NotificationBean implements Serializable {

    String Messgae;
    String Notftytype;
    String StepID;
    String FarmID;
    String Title;
    String DateTime;
    String NotifImageURL;
    String DateTimeHHMMSS;
    //Add New
    String FeedbackStatus;

    public String getDateTimeHHMMSS() {
        return DateTimeHHMMSS;
    }

    public void setDateTimeHHMMSS(String dateTimeHHMMSS) {
        DateTimeHHMMSS = dateTimeHHMMSS;
    }

    public String getNotifImageURL() {
        return NotifImageURL;
    }

    public void setNotifImageURL(String notifImageURL) {
        NotifImageURL = notifImageURL;
    }

    public String getMessgae() {
        return Messgae;
    }

    public void setMessgae(String messgae) {
        Messgae = messgae;
    }

    public String getNotftytype() {
        return Notftytype;
    }

    public void setNotftytype(String notftytype) {
        Notftytype = notftytype;
    }

    public String getStepID() {
        return StepID;
    }

    public void setStepID(String stepID) {
        StepID = stepID;
    }

    public String getFarmID() {
        return FarmID;
    }

    public void setFarmID(String farmID) {
        FarmID = farmID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getFeedbackStatus() {
        return FeedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        FeedbackStatus = feedbackStatus;
    }

    public NotificationBean() {
    }

    public NotificationBean(String Messgaes, String notftytypes, String StepIDs, String Titles, String FarmIDs, String DateTimes) {
        //super(null);
        this.Messgae = Messgaes;
        this.Notftytype = notftytypes;
        this.StepID = StepIDs;
        this.FarmID = FarmIDs;
        this.Title = Titles;
        this.DateTime = DateTimes;
        // TODO Auto-generated constructor stub
    }


}