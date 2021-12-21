package com.weather.risk.mfi.myfarminfo.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataDT {

    @SerializedName("deliverQty")
    @Expose
    private int deliverQty;

    @SerializedName("NoOfUnitSold")
    @Expose
    private Integer NoOfUnitSold;

    @SerializedName("RemainingUnitToSold")
    @Expose
    private Integer RemainingUnitToSold;



    @SerializedName("ServiceID")
    @Expose
    private Integer ServiceID;

    @SerializedName("Image")
    @Expose
    private String ImagePath;

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
    @SerializedName("ProductCategory")
    @Expose
    private String productCategory;
    @SerializedName("QuantityPurchased")
    @Expose
    private Integer quantityPurchased;

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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }


    public Integer getServiceID() {
        return ServiceID;
    }

    public void setServiceID(Integer serviceID) {
        ServiceID = serviceID;
    }

    public Integer getNoOfUnitSold() {
        return NoOfUnitSold;
    }

    public void setNoOfUnitSold(Integer noOfUnitSold) {
        NoOfUnitSold = noOfUnitSold;
    }

    public Integer getRemainingUnitToSold() {
        return RemainingUnitToSold;
    }

    public void setRemainingUnitToSold(Integer remainingUnitToSold) {
        RemainingUnitToSold = remainingUnitToSold;
    }

    public int getDeliverQty() {
        return deliverQty;
    }

    public void setDeliverQty(int deliverQty) {
        this.deliverQty = deliverQty;
    }
}
