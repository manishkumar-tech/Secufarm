package com.weather.risk.mfi.myfarminfo.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yogendra Singh on 19-10-2015.
 */
public class FarmAdvisoryDataSet implements Parcelable{

    String nutrient;
    String content;
    String soilApplication;

    public FarmAdvisoryDataSet(String nutrient, String content, String soilApplication) {
        this.nutrient = nutrient;
        this.content = content;
        this.soilApplication = soilApplication;
    }

    protected FarmAdvisoryDataSet(Parcel in) {
        nutrient = in.readString();
        content = in.readString();
        soilApplication = in.readString();
    }

    public static final Creator<FarmAdvisoryDataSet> CREATOR = new Creator<FarmAdvisoryDataSet>() {
        @Override
        public FarmAdvisoryDataSet createFromParcel(Parcel in) {
            return new FarmAdvisoryDataSet(in);
        }

        @Override
        public FarmAdvisoryDataSet[] newArray(int size) {
            return new FarmAdvisoryDataSet[size];
        }
    };

    public String getNutrient() {
        return nutrient;
    }

    public void setNutrient(String nutrient) {
        this.nutrient = nutrient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSoilApplication() {
        return soilApplication;
    }

    public void setSoilApplication(String soilApplication) {
        this.soilApplication = soilApplication;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nutrient);
        dest.writeString(content);
        dest.writeString(soilApplication);
    }
}
