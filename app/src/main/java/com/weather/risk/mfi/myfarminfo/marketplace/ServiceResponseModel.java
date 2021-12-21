package com.weather.risk.mfi.myfarminfo.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceResponseModel {

    @SerializedName("CategoryMasterName")
    @Expose
    private String CategoryMasterName;
    @SerializedName("CategoryMasterID")
    @Expose
    private int CategoryMasterID;

    public String getCategoryMasterName() {
        return CategoryMasterName;
    }

    public void setCategoryMasterName(String categoryMasterName) {
        CategoryMasterName = categoryMasterName;
    }

    public int getCategoryMasterID() {
        return CategoryMasterID;
    }

    public void setCategoryMasterID(int categoryMasterID) {
        CategoryMasterID = categoryMasterID;
    }
}
