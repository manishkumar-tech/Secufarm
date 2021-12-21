package com.weather.risk.mfi.myfarminfo.policyregistration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataDT {
    @SerializedName("ServiceID")
    @Expose
    private Integer serviceID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("ProductPrice")
    @Expose
    private Double productPrice;
    @SerializedName("ProductUnit")
    @Expose
    private String productUnit;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("ProductCategory")
    @Expose
    private String productCategory;
    @SerializedName("QuantityPurchased")
    @Expose
    private Integer quantityPurchased;
    @SerializedName("PolicyID")
    @Expose
    private Integer policyID;
    @SerializedName("NoOfUnitSold")
    @Expose
    private Object noOfUnitSold;
    @SerializedName("RemainingUnitToSold")
    @Expose
    private Object remainingUnitToSold;

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getQuantityPurchased() {
        return quantityPurchased;
    }

    public void setQuantityPurchased(Integer quantityPurchased) {
        this.quantityPurchased = quantityPurchased;
    }

    public Integer getPolicyID() {
        return policyID;
    }

    public void setPolicyID(Integer policyID) {
        this.policyID = policyID;
    }

    public Object getNoOfUnitSold() {
        return noOfUnitSold;
    }

    public void setNoOfUnitSold(Object noOfUnitSold) {
        this.noOfUnitSold = noOfUnitSold;
    }

    public Object getRemainingUnitToSold() {
        return remainingUnitToSold;
    }

    public void setRemainingUnitToSold(Object remainingUnitToSold) {
        this.remainingUnitToSold = remainingUnitToSold;
    }

}