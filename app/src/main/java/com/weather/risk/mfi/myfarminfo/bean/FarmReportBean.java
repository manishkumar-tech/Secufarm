package com.weather.risk.mfi.myfarminfo.bean;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Admin on 26-12-2017.
 */
public class FarmReportBean {

    String FarmID,FarmName,FarmerName,FarmArea,PhoneNo,UserName,Contour,State,StateName,CropID,
            Variety,CropName,N,P,K,SowDate,CropFrom,CropTo,OtherNutrient,BasalDoseApply,Soil,Irrigation,
            Nutrition,Concern,CenterLat,CenterLon,Area,RegDate,District;
    String userId;

    Marker marker;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getFarmID() {
        return FarmID;
    }

    public void setFarmID(String farmID) {
        FarmID = farmID;
    }

    public String getFarmName() {
        return FarmName;
    }

    public void setFarmName(String farmName) {
        FarmName = farmName;
    }

    public String getFarmerName() {
        return FarmerName;
    }

    public void setFarmerName(String farmerName) {
        FarmerName = farmerName;
    }

    public String getFarmArea() {
        return FarmArea;
    }

    public void setFarmArea(String farmArea) {
        FarmArea = farmArea;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getContour() {
        return Contour;
    }

    public void setContour(String contour) {
        Contour = contour;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getCropID() {
        return CropID;
    }

    public void setCropID(String cropID) {
        CropID = cropID;
    }

    public String getVariety() {
        return Variety;
    }

    public void setVariety(String variety) {
        Variety = variety;
    }

    public String getCropName() {
        return CropName;
    }

    public void setCropName(String cropName) {
        CropName = cropName;
    }

    public String getN() {
        return N;
    }

    public void setN(String n) {
        N = n;
    }

    public String getP() {
        return P;
    }

    public void setP(String p) {
        P = p;
    }

    public String getK() {
        return K;
    }

    public void setK(String k) {
        K = k;
    }

    public String getSowDate() {
        return SowDate;
    }

    public void setSowDate(String sowDate) {
        SowDate = sowDate;
    }

    public String getCropFrom() {
        return CropFrom;
    }

    public void setCropFrom(String cropFrom) {
        CropFrom = cropFrom;
    }

    public String getCropTo() {
        return CropTo;
    }

    public void setCropTo(String cropTo) {
        CropTo = cropTo;
    }

    public String getOtherNutrient() {
        return OtherNutrient;
    }

    public void setOtherNutrient(String otherNutrient) {
        OtherNutrient = otherNutrient;
    }

    public String getBasalDoseApply() {
        return BasalDoseApply;
    }

    public void setBasalDoseApply(String basalDoseApply) {
        BasalDoseApply = basalDoseApply;
    }

    public String getSoil() {
        return Soil;
    }

    public void setSoil(String soil) {
        Soil = soil;
    }

    public String getIrrigation() {
        return Irrigation;
    }

    public void setIrrigation(String irrigation) {
        Irrigation = irrigation;
    }

    public String getNutrition() {
        return Nutrition;
    }

    public void setNutrition(String nutrition) {
        Nutrition = nutrition;
    }

    public String getConcern() {
        return Concern;
    }

    public void setConcern(String concern) {
        Concern = concern;
    }

    public String getCenterLat() {
        return CenterLat;
    }

    public void setCenterLat(String centerLat) {
        CenterLat = centerLat;
    }

    public String getCenterLon() {
        return CenterLon;
    }

    public void setCenterLon(String centerLon) {
        CenterLon = centerLon;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }
}
