package com.weather.risk.mfi.myfarminfo.entities;

import com.weather.risk.mfi.myfarminfo.utils.AppManager;

/**
 *
 */
public class FarmInformationData {
    private String besalDoseApply;
    private String userID;
    private String userName;
    private String farmName;
    private String farmerName;
    private String crop;
    private String cropID;
    private String variety;
    private  String sowPeriodForm;
    private String sowPeriodTo;
    private String basalDoseN;
    private String otherNutrition;
    private String farmId;
    private String state;
    private String allLatLngPoint;
    private String basalDoseP;
    private String basalDoseK;
    private  String yourCencern;
    private String area;

    private String farmerNumber;
    private String actualFarmArea;


    //Add new fields
    //StateName,DistrictID,Block,VillageID,VillageStr,ProjectID,TaggingApp,FatherName,
    // AadharNo,Aadhar_Other,MobileType,NoOfBags,createaccflag
    private String StateName;
    private String DistrictID;
    private String Block;
    private String VillageID;
    private String VillageStr;
    private String ProjectID;
    private String TaggingApp;
    private String FatherName;
    private String AadharNo;
    private String Aadhar_Other;
    private String MobileType;
    private String NoOfBags;
    private String createaccflag;

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String districtID) {
        DistrictID = districtID;
    }

    public String getBlock() {
        return Block;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public String getVillageID() {
        return VillageID;
    }

    public void setVillageID(String villageID) {
        VillageID = villageID;
    }

    public String getVillageStr() {
        return VillageStr;
    }

    public void setVillageStr(String villageStr) {
        VillageStr = villageStr;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getTaggingApp() {
        return TaggingApp;
    }

    public void setTaggingApp(String taggingApp) {
        TaggingApp = taggingApp;
    }

    public String getFatherName() {
        return FatherName;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public String getAadharNo() {
        return AadharNo;
    }

    public void setAadharNo(String aadharNo) {
        AadharNo = aadharNo;
    }

    public String getAadhar_Other() {
        return Aadhar_Other;
    }

    public void setAadhar_Other(String aadhar_Other) {
        Aadhar_Other = aadhar_Other;
    }

    public String getMobileType() {
        return MobileType;
    }

    public void setMobileType(String mobileType) {
        MobileType = mobileType;
    }

    public String getNoOfBags() {
        return NoOfBags;
    }

    public void setNoOfBags(String noOfBags) {
        NoOfBags = noOfBags;
    }

    public String getCreateaccflag() {
        return createaccflag;
    }

    public void setCreateaccflag(String createaccflag) {
        this.createaccflag = createaccflag;
    }

    public String getActualFarmArea() {
        return actualFarmArea;
    }

    public void setActualFarmArea(String actualFarmArea) {
        this.actualFarmArea = actualFarmArea;
    }

    public String getFarmerNumber() {
        return farmerNumber;
    }

    public void setFarmerNumber(String farmerNumber) {
        this.farmerNumber = farmerNumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAllLatLngPoint() {
        return allLatLngPoint;
    }

    public void setAllLatLngPoint(String allLatLngPoint) {
        this.allLatLngPoint = allLatLngPoint;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }


    public String getOtherNutrition() {
        return otherNutrition;
    }

    public void setOtherNutrition(String otherNutrition) {
        this.otherNutrition = otherNutrition;
    }

    public String getBesalDoseApply() {
        return besalDoseApply;
    }

    public void setBesalDoseApply(String besalDoseApply) {
        this.besalDoseApply = besalDoseApply;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getCropID() {
        return cropID;
    }

    public void setCropID(String cropID) {
        this.cropID = cropID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getVariety() {

        return AppManager.getInstance().removeSpaceForUrl(AppManager.getInstance().removeShaleshFromVariety(variety));
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getSowPeriodForm() {
        return sowPeriodForm;
    }

    public void setSowPeriodForm(String sowPeriodForm) {
        this.sowPeriodForm = sowPeriodForm;
    }

    public String getSowPeriodTo() {
        return sowPeriodTo;
    }

    public void setSowPeriodTo(String sowPeriodTo) {
        this.sowPeriodTo = sowPeriodTo;
    }

    public String getBasalDoseN() {
        return basalDoseN;
    }

    public void setBasalDoseN(String basalDoseN) {
        this.basalDoseN = basalDoseN;
    }

    public String getBasalDoseP() {
        return basalDoseP;
    }

    public void setBasalDoseP(String basalDoseP) {
        this.basalDoseP = basalDoseP;
    }

    public String getBasalDoseK() {
        return basalDoseK;
    }

    public void setBasalDoseK(String basalDoseK) {
        this.basalDoseK = basalDoseK;
    }

    public String getYourCencern() {
        return yourCencern;
    }

    public void setYourCencern(String yourCencern) {
        this.yourCencern = yourCencern;
    }



//    public void farmInfo(FarmInformationData dataFarmInfo)
//
//    {
//
//
//        //use get method to fetch all data from server and first store in database and disply list view
//    }
}
