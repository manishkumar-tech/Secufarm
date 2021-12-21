package com.weather.risk.mfi.myfarminfo.entities;

/**
 * Created by Yogendra Singh on 09-10-2015.
 */
public class CropVariety {

    public String crops;
    public String variety;

    public CropVariety(String crops, String variety) {
        this.crops = crops;
        this.variety = variety;
    }

    public String getCrops() {
        return crops;
    }

    public void setCrops(String crops) {
        this.crops = crops;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }
}
