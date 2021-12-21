package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DTPolicy {

    @SerializedName("PolicyID")
    @Expose
    private Integer policyID;
    @SerializedName("policycode")
    @Expose
    private String policycode;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("CloseDate")
    @Expose
    private String closeDate;
    @SerializedName("CurrentStatus")
    @Expose
    private String currentStatus;
    @SerializedName("TransplantingDate")
    @Expose
    private String transplantingDate;
    @SerializedName("ExpectedHarvestingDate")
    @Expose
    private String expectedHarvestingDate;
    @SerializedName("FarmID")
    @Expose
    private Integer farmID;
    @SerializedName("FarmerID")
    @Expose
    private Integer farmerID;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("CompensationID")
    @Expose
    private Object compensationID;
    @SerializedName("PolicyMasterId")
    @Expose
    private Integer policyMasterId;
    @SerializedName("ValueAssured")
    @Expose
    private Double valueAssured;
    @SerializedName("Fee")
    @Expose
    private Double fee;
//    @SerializedName("AadhaarPhotoID")
//    @Expose
//    private String aadhaarPhotoID;
//    @SerializedName("LandDocumentPhotoID")
//    @Expose
//    private String landDocumentPhotoID;
    @SerializedName("AccountName")
    @Expose
    private String accountName;
    @SerializedName("AccountNo")
    @Expose
    private String accountNo;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankID")
    @Expose
    private Integer bankID;
    @SerializedName("AadhaarNo")
    @Expose
    private String aadhaarNo;
    @SerializedName("CreationDate")
    @Expose
    private String creationDate;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("PolicyName")
    @Expose
    private String policyName;
    @SerializedName("BenchmarkYield")
    @Expose
    private Double benchmarkYield;
    @SerializedName("DocumentPath")
    @Expose
    private String documentPath;
    @SerializedName("BankName")
    @Expose
    private String bankName;
    @SerializedName("FarmerName")
    @Expose
    private String farmerName;
    @SerializedName("cropid")
    @Expose
    private Integer cropid;
    @SerializedName("cropname")
    @Expose
    private String cropname;

    public Integer getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Integer policyID) {
        this.policyID = policyID;
    }

    public String getPolicycode() {
        return policycode;
    }

    public void setPolicycode(String policycode) {
        this.policycode = policycode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getTransplantingDate() {
        return transplantingDate;
    }

    public void setTransplantingDate(String transplantingDate) {
        this.transplantingDate = transplantingDate;
    }

    public String getExpectedHarvestingDate() {
        return expectedHarvestingDate;
    }

    public void setExpectedHarvestingDate(String expectedHarvestingDate) {
        this.expectedHarvestingDate = expectedHarvestingDate;
    }

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }

    public Integer getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(Integer farmerID) {
        this.farmerID = farmerID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getCompensationID() {
        return compensationID;
    }

    public void setCompensationID(Object compensationID) {
        this.compensationID = compensationID;
    }

    public Integer getPolicyMasterId() {
        return policyMasterId;
    }

    public void setPolicyMasterId(Integer policyMasterId) {
        this.policyMasterId = policyMasterId;
    }

    public Double getValueAssured() {
        return valueAssured;
    }

    public void setValueAssured(Double valueAssured) {
        this.valueAssured = valueAssured;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

//    public String getAadhaarPhotoID() {
//        return aadhaarPhotoID;
//    }
//
//    public void setAadhaarPhotoID(String aadhaarPhotoID) {
//        this.aadhaarPhotoID = aadhaarPhotoID;
//    }
//
//    public String getLandDocumentPhotoID() {
//        return landDocumentPhotoID;
//    }
//
//    public void setLandDocumentPhotoID(String landDocumentPhotoID) {
//        this.landDocumentPhotoID = landDocumentPhotoID;
//    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIFSCCode() {
        return iFSCCode;
    }

    public void setIFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public Double getBenchmarkYield() {
        return benchmarkYield;
    }

    public void setBenchmarkYield(Double benchmarkYield) {
        this.benchmarkYield = benchmarkYield;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public Integer getCropid() {
        return cropid;
    }

    public void setCropid(Integer cropid) {
        this.cropid = cropid;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

}
