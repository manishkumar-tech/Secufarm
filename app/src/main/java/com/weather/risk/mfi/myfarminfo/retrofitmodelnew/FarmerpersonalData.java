package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerpersonalData {

    @SerializedName("DistrictID")
    @Expose
    private String DistrictID;

    @SerializedName("StateID")
    @Expose
    private String StateID;

    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("FatherName")
    @Expose
    private String fatherName;
    @SerializedName("District")
    @Expose
    private String district;
    @SerializedName("Block")
    @Expose
    private String block;
    @SerializedName("Village")
    @Expose
    private String village;
    @SerializedName("FarmerPhno")
    @Expose
    private String farmerPhno;
    @SerializedName("FarmerImage")
    @Expose
    private String farmerImage;
    @SerializedName("AccountNo")
    @Expose
    private String accountNo;
    @SerializedName("PassBookImage")
    @Expose
    private String passBookImage;
    @SerializedName("BalanceAmount")
    @Expose
    private String balanceAmount;

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getFarmerPhno() {
        return farmerPhno;
    }

    public void setFarmerPhno(String farmerPhno) {
        this.farmerPhno = farmerPhno;
    }

    public String getFarmerImage() {
        return farmerImage;
    }

    public void setFarmerImage(String farmerImage) {
        this.farmerImage = farmerImage;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPassBookImage() {
        return passBookImage;
    }

    public void setPassBookImage(String passBookImage) {
        this.passBookImage = passBookImage;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(String districtID) {
        DistrictID = districtID;
    }
}
