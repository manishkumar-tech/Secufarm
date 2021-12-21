package com.weather.risk.mfi.myfarminfo.pest_disease;

/**
 * Created by Admin on 30-12-2017.
 */
public class CropBean {

    String cropId;
    String cropName;
    String popMaster;

    public String getPopMaster() {
        return popMaster;
    }

    public void setPopMaster(String popMaster) {
        this.popMaster = popMaster;
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }
}
