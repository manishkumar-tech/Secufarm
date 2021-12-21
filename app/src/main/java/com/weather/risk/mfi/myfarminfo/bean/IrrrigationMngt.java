package com.weather.risk.mfi.myfarminfo.bean;

public class IrrrigationMngt {
    int Id;
    int cropid;
    String Language;
    String cropname;
    String DAS;
    String Notification;
    String soiltype;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCropid() {
        return cropid;
    }

    public void setCropid(int cropid) {
        this.cropid = cropid;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getDAS() {
        return DAS;
    }

    public void setDAS(String DAS) {
        this.DAS = DAS;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype;
    }
}
