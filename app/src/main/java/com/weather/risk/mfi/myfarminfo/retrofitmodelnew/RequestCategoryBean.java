package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCategoryBean {


    @SerializedName("projectID")
    @Expose
    private int projectID;

    @SerializedName("DistrictID")
    @Expose
    private int DistrictID;

    @SerializedName("CategoryMasterID")
    @Expose
    private int CategoryMasterID;



    @SerializedName("farmID")
    @Expose
    private int farmID;

    @SerializedName("CropID")
    @Expose
    private int CropID;

    @SerializedName("StateID")
    @Expose
    private int StateID;



    @SerializedName("categoryID")
    @Expose
    private int categoryID;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getCropID() {
        return CropID;
    }

    public void setCropID(int cropID) {
        CropID = cropID;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public int getCategoryMasterID() {
        return CategoryMasterID;
    }

    public void setCategoryMasterID(int categoryMasterID) {
        CategoryMasterID = categoryMasterID;
    }
}
