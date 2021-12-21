package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmePestDiseaseAlert {
    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("lstPestDiseaseData")
    @Expose
    private List<Dashboard_FarmeLstPestDiseaseDatum> lstPestDiseaseData = null;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Dashboard_FarmeLstPestDiseaseDatum> getLstPestDiseaseData() {
        return lstPestDiseaseData;
    }

    public void setLstPestDiseaseData(List<Dashboard_FarmeLstPestDiseaseDatum> lstPestDiseaseData) {
        this.lstPestDiseaseData = lstPestDiseaseData;
    }
}
