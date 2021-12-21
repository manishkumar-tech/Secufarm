package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeNDVIAdvisory {

    @SerializedName("Recomondation")
    @Expose
    private List<Object> recomondation = null;
    @SerializedName("message")
    @Expose
    private String message;

    public List<Object> getRecomondation() {
        return recomondation;
    }

    public void setRecomondation(List<Object> recomondation) {
        this.recomondation = recomondation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
