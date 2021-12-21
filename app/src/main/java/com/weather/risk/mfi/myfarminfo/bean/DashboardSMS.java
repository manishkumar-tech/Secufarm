package com.weather.risk.mfi.myfarminfo.bean;

public class DashboardSMS {

    String Id, Message, OutDate, MessageType, Feedback, foundflag, MediaType, MediaURL;

    public String getMessage() {
        return Message;
    }

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

    public void setMessage(String message) {
        Message = message;
    }

    public String getOutDate() {
        return OutDate;
    }

    public void setOutDate(String outDate) {
        OutDate = outDate;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    @Override
    public String toString() {
        return "DashboardSMS{" +
                "Id='" + Id + '\'' +
                ", Message='" + Message + '\'' +
                ", OutDate='" + OutDate + '\'' +
                ", MessageType='" + MessageType + '\'' +
                ", Feedback='" + Feedback + '\'' +
                ", foundflag='" + foundflag + '\'' +
                ", MediaType='" + MediaType + '\'' +
                ", MediaURL='" + MediaURL + '\'' +
                '}';
    }

    public String getFoundflag() {
        return foundflag;
    }

    public void setFoundflag(String foundflag) {
        this.foundflag = foundflag;
    }
}
