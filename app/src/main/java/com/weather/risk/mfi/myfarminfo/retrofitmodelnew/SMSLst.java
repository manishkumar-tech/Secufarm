package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SMSLst {

    @SerializedName("Message")
    @Expose
    private String message;

    public String getFeddbackDate() {
        return FeddbackDate;
    }

    public void setFeddbackDate(String feddbackDate) {
        FeddbackDate = feddbackDate;
    }

    @SerializedName("FeddbackDate")
    @Expose
    private String FeddbackDate;

    public String getCCSID() {
        return CCSID;
    }

    public void setCCSID(String CCSID) {
        this.CCSID = CCSID;
    }

    @SerializedName("CCSID")
    @Expose
    private String CCSID;

    @SerializedName("OutDate")
    @Expose
    private String outDate;
    @SerializedName("MessageType")
    @Expose
    private String messageType;
    @SerializedName("Feedback")
    @Expose
    private String Feedback;
    @SerializedName("Id")
    @Expose
    private Integer Id;
    @SerializedName("foundflag")
    @Expose
    private String foundflag;

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public String getMediaURL() {
        return MediaURL;
    }

    public void setMediaURL(String mediaURL) {
        MediaURL = mediaURL;
    }

    @SerializedName("MediaType")
    @Expose
    private String MediaType;
    @SerializedName("MediaURL")
    @Expose
    private String MediaURL;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutDate() {
        return outDate;
    }

    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getFoundflag() {
        return foundflag;
    }

    public void setFoundflag(String foundflag) {
        this.foundflag = foundflag;
    }
}
