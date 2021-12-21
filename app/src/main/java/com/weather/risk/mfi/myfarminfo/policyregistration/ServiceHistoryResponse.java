package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceHistoryResponse {

    @SerializedName("FarmerID")
    @Expose
    private Integer FarmerId;
    @SerializedName("TotalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("CollectedAmount")
    @Expose
    private Double collectedAmount;
    @SerializedName("PendingAmount")
    @Expose
    private Double pendingAmount;

    public Integer getFarmerId() {
        return FarmerId;
    }

    public void setFarmerId(Integer farmerID) {
        this.FarmerId = farmerID;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(Double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public Double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(Double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;
    @SerializedName("farmerID")
    @Expose
    private Integer farmerID;
    @SerializedName("orderdate")
    @Expose
    private String orderdate;
    @SerializedName("OrderAmount")
    @Expose
    private Double orderAmount;
    //    @SerializedName("CollectedAmount")
//    @Expose
//    private Object collectedAmount;
    @SerializedName("OrderStatus")
    @Expose
    private String orderStatus;
    @SerializedName("DeliveryStatus")
    @Expose
    private String deliveryStatus;
    @SerializedName("PaymentType")
    @Expose
    private String paymentType;
    @SerializedName("TransactionPaymentType")
    @Expose
    private Object transactionPaymentType;
    @SerializedName("TransactionPaymentStatus")
    @Expose
    private Object transactionPaymentStatus;
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
    @SerializedName("FarmerID1")
    @Expose
    private Integer farmerID1;
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
//    @SerializedName("PendingAmount")
//    @Expose
//    private String pendingAmount;

    @SerializedName("Area")
    @Expose
    private Double area;

    //New Fields
    @SerializedName("ServiceStatus")
    @Expose
    private String ServiceStatus ;
    @SerializedName("PolicyVerificatioStatus")
    @Expose
    private String PolicyVerificatioStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getFarmerID() {
        return farmerID;
    }

    public void setFarmerID(Integer farmerID) {
        this.farmerID = farmerID;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

//    public Object getCollectedAmount() {
//        return collectedAmount;
//    }
//
//    public void setCollectedAmount(Object collectedAmount) {
//        this.collectedAmount = collectedAmount;
//    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Object getTransactionPaymentType() {
        return transactionPaymentType;
    }

    public void setTransactionPaymentType(Object transactionPaymentType) {
        this.transactionPaymentType = transactionPaymentType;
    }

    public Object getTransactionPaymentStatus() {
        return transactionPaymentStatus;
    }

    public void setTransactionPaymentStatus(Object transactionPaymentStatus) {
        this.transactionPaymentStatus = transactionPaymentStatus;
    }

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

    public Integer getFarmerID1() {
        return farmerID1;
    }

    public void setFarmerID1(Integer farmerID1) {
        this.farmerID1 = farmerID1;
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

//    public String getPendingAmount() {
//        return pendingAmount;
//    }
//
//    public void setPendingAmount(String pendingAmount) {
//        this.pendingAmount = pendingAmount;
//    }

    //Image

    //    @SerializedName("Type")
//    @Expose
//    private String type;
//    @SerializedName("ImageName")
//    @Expose
//    private String imageName;
//    @SerializedName("Latitude")
//    @Expose
//    private String latitude;
//    @SerializedName("Longitude")
//    @Expose
//    private String longitude;
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getImageName() {
//        return imageName;
//    }
//
//    public void setImageName(String imageName) {
//        this.imageName = imageName;
//    }
//
//    public String getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(String latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(String longitude) {
//        this.longitude = longitude;
//    }
    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getServiceStatus() {
        return ServiceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        ServiceStatus = serviceStatus;
    }

    public String getPolicyVerificatioStatus() {
        return PolicyVerificatioStatus;
    }

    public void setPolicyVerificatioStatus(String policyVerificatioStatus) {
        PolicyVerificatioStatus = policyVerificatioStatus;
    }
}
