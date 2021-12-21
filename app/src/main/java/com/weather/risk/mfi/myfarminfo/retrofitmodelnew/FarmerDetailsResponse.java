package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmerDetailsResponse {

    @SerializedName("FarmerpersonalData")
    @Expose
    private List<FarmerpersonalData> farmerpersonalData = null;
    @SerializedName("infoDataDT")
    @Expose
    private List<InfoDataDT> infoDataDT = null;

    public List<FarmerpersonalData> getFarmerpersonalData() {
        return farmerpersonalData;
    }

    public void setFarmerpersonalData(List<FarmerpersonalData> farmerpersonalData) {
        this.farmerpersonalData = farmerpersonalData;
    }

    public List<InfoDataDT> getInfoDataDT() {
        return infoDataDT;
    }

    public void setInfoDataDT(List<InfoDataDT> infoDataDT) {
        this.infoDataDT = infoDataDT;
    }
}
