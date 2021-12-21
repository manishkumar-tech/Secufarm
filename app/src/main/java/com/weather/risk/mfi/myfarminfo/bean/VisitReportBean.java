package com.weather.risk.mfi.myfarminfo.bean;

public class VisitReportBean {

     /*"farmername": "kunalbhai kantibhai patel",
             "farmname": "Plot 1",
             "FarmLatLon": "23.51,72.97",
             "Village": "Amodra",
             "sub_district": "Prantij",
             "Visitor_Name": "Piyush Padhiyar",
             "Visit_Date": "2018-12-11T15:16:35",
             "Visit Latitude": "23.08",
             "Visit Longitude": "72.55",
             "Images": "Image-1#https:\/\/myfarminfo.com\/Tools\/Img\/Ais\/Uploads\/181185\/84059313-d7c4-4f23-8149-7ed40d249097.jpg",
             "DisFromFarm(Km)": "64.58"*/

     String farmername,farmname,FarmLatLon,Village,sub_district,Visitor_Name,Visit_Date,Visit_Latitude,Visit_Longitude,Images,DisFromFarm;

    public String getFarmername() {
        return farmername;
    }

    public void setFarmername(String farmername) {
        this.farmername = farmername;
    }

    public String getFarmname() {
        return farmname;
    }

    public void setFarmname(String farmname) {
        this.farmname = farmname;
    }

    public String getFarmLatLon() {
        return FarmLatLon;
    }

    public void setFarmLatLon(String farmLatLon) {
        FarmLatLon = farmLatLon;
    }

    public String getVillage() {
        return Village;
    }

    public void setVillage(String village) {
        Village = village;
    }

    public String getSub_district() {
        return sub_district;
    }

    public void setSub_district(String sub_district) {
        this.sub_district = sub_district;
    }

    public String getVisitor_Name() {
        return Visitor_Name;
    }

    public void setVisitor_Name(String visitor_Name) {
        Visitor_Name = visitor_Name;
    }

    public String getVisit_Date() {
        return Visit_Date;
    }

    public void setVisit_Date(String visit_Date) {
        Visit_Date = visit_Date;
    }

    public String getVisit_Latitude() {
        return Visit_Latitude;
    }

    public void setVisit_Latitude(String visit_Latitude) {
        Visit_Latitude = visit_Latitude;
    }

    public String getVisit_Longitude() {
        return Visit_Longitude;
    }

    public void setVisit_Longitude(String visit_Longitude) {
        Visit_Longitude = visit_Longitude;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public String getDisFromFarm() {
        return DisFromFarm;
    }

    public void setDisFromFarm(String disFromFarm) {
        DisFromFarm = disFromFarm;
    }
}
