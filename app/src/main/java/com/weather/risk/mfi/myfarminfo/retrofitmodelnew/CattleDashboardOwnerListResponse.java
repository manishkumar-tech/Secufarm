package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CattleDashboardOwnerListResponse {
    @SerializedName("CattleID")
    @Expose
    private Integer cattleID;
    @SerializedName("OwnerID")
    @Expose
    private Integer ownerID;
    @SerializedName("MainCattleID")
    @Expose
    private Integer mainCattleID;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("Breed")
    @Expose
    private String breed;
    @SerializedName("CattleShedId")
    @Expose
    private Integer cattleShedId;
    @SerializedName("CattleShedName")
    @Expose
    private String cattleShedName;
    @SerializedName("Contour")
    @Expose
    private String contour;
    @SerializedName("Lat")
    @Expose
    private Double lat;
    @SerializedName("Lon")
    @Expose
    private Double lon;
    @SerializedName("CattleName")
    @Expose
    private String cattleName;
    @SerializedName("CattleType")
    @Expose
    private String cattleType;
    @SerializedName("Gender1")
    @Expose
    private String gender1;
    @SerializedName("Breed1")
    @Expose
    private String breed1;
    @SerializedName("Lifestage")
    @Expose
    private String lifestage;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FarmerPhno")
    @Expose
    private String farmerPhno;
    @SerializedName("FatherName")
    @Expose
    private String fatherName;

    public Integer getCattleID() {
        return cattleID;
    }

    public void setCattleID(Integer cattleID) {
        this.cattleID = cattleID;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getMainCattleID() {
        return mainCattleID;
    }

    public void setMainCattleID(Integer mainCattleID) {
        this.mainCattleID = mainCattleID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getCattleShedId() {
        return cattleShedId;
    }

    public void setCattleShedId(Integer cattleShedId) {
        this.cattleShedId = cattleShedId;
    }

    public String getCattleShedName() {
        return cattleShedName;
    }

    public void setCattleShedName(String cattleShedName) {
        this.cattleShedName = cattleShedName;
    }

    public String getContour() {
        return contour;
    }

    public void setContour(String contour) {
        this.contour = contour;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getCattleName() {
        return cattleName;
    }

    public void setCattleName(String cattleName) {
        this.cattleName = cattleName;
    }

    public String getCattleType() {
        return cattleType;
    }

    public void setCattleType(String cattleType) {
        this.cattleType = cattleType;
    }

    public String getGender1() {
        return gender1;
    }

    public void setGender1(String gender1) {
        this.gender1 = gender1;
    }

    public String getBreed1() {
        return breed1;
    }

    public void setBreed1(String breed1) {
        this.breed1 = breed1;
    }

    public String getLifestage() {
        return lifestage;
    }

    public void setLifestage(String lifestage) {
        this.lifestage = lifestage;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerPhno() {
        return farmerPhno;
    }

    public void setFarmerPhno(String farmerPhno) {
        this.farmerPhno = farmerPhno;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

}