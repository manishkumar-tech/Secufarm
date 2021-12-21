package com.weather.risk.mfi.myfarminfo.bean;

/**
 */
public class NDBI_Bean {

    String date;
    String image;
    String startDate;
    String villageId;
    String districtId;
    String id;

    String Final_soilImg,Village_mean,Date,Start_Date,Farm_Data,Captcha_Img;


    public String getFinal_soilImg() {
        return Final_soilImg;
    }

    public void setFinal_soilImg(String final_soilImg) {
        Final_soilImg = final_soilImg;
    }

    public String getVillage_mean() {
        return Village_mean;
    }

    public void setVillage_mean(String village_mean) {
        Village_mean = village_mean;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getFarm_Data() {
        return Farm_Data;
    }

    public void setFarm_Data(String farm_Data) {
        Farm_Data = farm_Data;
    }

    public String getCaptcha_Img() {
        return Captcha_Img;
    }

    public void setCaptcha_Img(String captcha_Img) {
        Captcha_Img = captcha_Img;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }
}
