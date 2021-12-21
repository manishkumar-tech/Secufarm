package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDetailResponse implements Parcelable {

    @SerializedName("ProductDescription")
    @Expose
    private String ProductDescription;
    @SerializedName("ImagePath")
    @Expose
    private String ImagePath;
    @SerializedName("BrandName")
    @Expose
    private String BrandName;
    @SerializedName("ProductUnit")
    @Expose
    private String ProductUnit;
    @SerializedName("Quantity")
    @Expose
    private Double quantityUnit;

    @SerializedName("ServiceID")
    @Expose
    private String serviceID;

    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("EndDate")
    @Expose
    private String endDate;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("ServiceName")
    @Expose
    private String service;
    @SerializedName("Quantityy")
    @Expose
    private int quantity;

    //Add New Fields for policy insde Cetagories

    @SerializedName("PolicyMasterID")
    @Expose
    private int policyMasterID;
    @SerializedName("DocumentPath")
    @Expose
    private String documentPath;
    @SerializedName("PolicyStartDate")
    @Expose
    private String policyStartDate;
    @SerializedName("PolicyCloseDate")
    @Expose
    private String policyCloseDate;
    @SerializedName("ValueAssured")
    @Expose
    private int valueAssured;
    @SerializedName("SettlementLevel")
    @Expose
    private String settlementLevel;

    @SerializedName("BenchmarkYield")
    @Expose
    private int benchmarkYield;

    //AssismentName and AssismentType 20210824

    @SerializedName("AssismentName")
    @Expose
    private String AssismentName;
    @SerializedName("AssismentType")
    @Expose
    private String AssismentType;

    public int getPolicyMasterID() {
        return policyMasterID;
    }

    public void setPolicyMasterID(int policyMasterID) {
        this.policyMasterID = policyMasterID;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(String policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public String getPolicyCloseDate() {
        return policyCloseDate;
    }

    public void setPolicyCloseDate(String policyCloseDate) {
        this.policyCloseDate = policyCloseDate;
    }

    public int getValueAssured() {
        return valueAssured;
    }

    public void setValueAssured(int valueAssured) {
        this.valueAssured = valueAssured;
    }

    public String getSettlementLevel() {
        return settlementLevel;
    }

    public void setSettlementLevel(String settlementLevel) {
        this.settlementLevel = settlementLevel;
    }

    public int getBenchmarkYield() {
        return benchmarkYield;
    }

    public void setBenchmarkYield(int benchmarkYield) {
        this.benchmarkYield = benchmarkYield;
    }

    public final static Creator<CategoryDetailResponse> CREATOR = new Creator<CategoryDetailResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CategoryDetailResponse createFromParcel(Parcel in) {
            return new CategoryDetailResponse(in);
        }

        public CategoryDetailResponse[] newArray(int size) {
            return (new CategoryDetailResponse[size]);
        }

    };

    protected CategoryDetailResponse(Parcel in) {
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((Double) in.readValue((Double.class.getClassLoader())));
        this.service = ((String) in.readValue((String.class.getClassLoader())));
        this.serviceID = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((int) in.readValue((Double.class.getClassLoader())));
        this.ImagePath = ((String) in.readValue((String.class.getClassLoader())));
        this.ProductUnit = ((String) in.readValue((String.class.getClassLoader())));
        this.ProductDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.BrandName = ((String) in.readValue((String.class.getClassLoader())));
        this.quantityUnit = ((Double) in.readValue((Double.class.getClassLoader())));
        //
        this.policyMasterID = ((int) in.readValue((Double.class.getClassLoader())));
        this.documentPath = ((String) in.readValue((String.class.getClassLoader())));
        this.policyStartDate = ((String) in.readValue((String.class.getClassLoader())));
        this.policyCloseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.valueAssured = ((int) in.readValue((Double.class.getClassLoader())));
        this.settlementLevel = ((String) in.readValue((Double.class.getClassLoader())));
        this.benchmarkYield = ((int) in.readValue((Double.class.getClassLoader())));


        this.AssismentName = ((String) in.readValue((Double.class.getClassLoader())));
        this.AssismentType = ((String) in.readValue((Double.class.getClassLoader())));

    }

    public String getAssismentName() {
        return AssismentName;
    }

    public void setAssismentName(String assismentName) {
        AssismentName = assismentName;
    }

    public String getAssismentType() {
        return AssismentType;
    }

    public void setAssismentType(String assismentType) {
        AssismentType = assismentType;
    }

    public CategoryDetailResponse() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getProductUnit() {
        return ProductUnit;
    }

    public void setProductUnit(String productUnit) {
        ProductUnit = productUnit;
    }


    public Double getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(Double quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(price);
        dest.writeValue(service);
        dest.writeValue(serviceID);
        dest.writeValue(quantity);
        dest.writeValue(ImagePath);
        dest.writeValue(ProductUnit);
        dest.writeValue(ProductDescription);
        dest.writeValue(BrandName);
        dest.writeValue(quantityUnit);
        //Policy Reg

        dest.writeValue(policyMasterID);
        dest.writeValue(documentPath);
        dest.writeValue(policyStartDate);
        dest.writeValue(policyCloseDate);
        dest.writeValue(valueAssured);
        dest.writeValue(settlementLevel);
        dest.writeValue(benchmarkYield);

        dest.writeValue(AssismentName);
        dest.writeValue(AssismentType);

    }

    public int describeContents() {
        return 0;
    }

}