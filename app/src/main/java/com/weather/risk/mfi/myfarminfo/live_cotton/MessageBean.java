package com.weather.risk.mfi.myfarminfo.live_cotton;

/**
 * Created by Admin on 07-09-2017.
 *
 *   "Reqid": 3,
 "Name": "Vishal Tripathi",
 "Phoneno": "8285686540",
 "Location": "Gurgaon,Haryana,India",
 "Crop": "Cotton",
 "Status": "Unresolved",
 "RequestDate": "2017-09-06T12:49:11",
 "RequestUser": "wwf",
 "ResolveDate": null,
 "ResolveUser": null,
 "SentDate": null,
 "SentUser": null,
 "CropId": "2",
 "PendingDate": null,
 "LocationID": 2,
 "Id": 6,
 "ReqId1": 3,
 "Message": "just for testing",
 "Messagetype": "Request",
 "MessageDate": "2017-09-06T12:49:11",
 "User": "wwf",
 "ImagePath": "pass.png",
 "SentDate1": null,
 "PendingDate1": null,
 "ResolutionType": null
 *
 */
public class MessageBean {

    String Reqid;
    String Name;
    String Phoneno;
    String location;
    String crop;
    String requestDate;
    String requestUser;
    String cropId;
    String message;
    String messageType;
    String msgDate;
    String user;
    String image;
    String image2;
    String audioPath;

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }


    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReqid() {
        return Reqid;
    }

    public void setReqid(String reqid) {
        Reqid = reqid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneno() {
        return Phoneno;
    }

    public void setPhoneno(String phoneno) {
        Phoneno = phoneno;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }
}
