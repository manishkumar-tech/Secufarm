package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeLstPestDiseaseDatum {
    @SerializedName("pestname")
    @Expose
    private String pestname;
    @SerializedName("pestdescription")
    @Expose
    private List<String> pestdescription = null;
    @SerializedName("Imagelst")
    @Expose
    private List<String> imagelst = null;
    @SerializedName("likelihood")
    @Expose
    private Boolean likelihood;
    @SerializedName("lstmanagement")
    @Expose
    private List<String> lstmanagement = null;
    @SerializedName("NextStep")
    @Expose
    private String nextStep;

    public String getPestname() {
        return pestname;
    }

    public void setPestname(String pestname) {
        this.pestname = pestname;
    }

    public List<String> getPestdescription() {
        return pestdescription;
    }

    public void setPestdescription(List<String> pestdescription) {
        this.pestdescription = pestdescription;
    }

    public List<String> getImagelst() {
        return imagelst;
    }

    public void setImagelst(List<String> imagelst) {
        this.imagelst = imagelst;
    }

    public Boolean getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(Boolean likelihood) {
        this.likelihood = likelihood;
    }

    public List<String> getLstmanagement() {
        return lstmanagement;
    }

    public void setLstmanagement(List<String> lstmanagement) {
        this.lstmanagement = lstmanagement;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }
}
