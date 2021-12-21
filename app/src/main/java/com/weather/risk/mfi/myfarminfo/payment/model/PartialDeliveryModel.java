package com.weather.risk.mfi.myfarminfo.payment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PartialDeliveryModel {

    @SerializedName("serviceID")
    @Expose
    private int serviceID;


    @SerializedName("noofUnit")
    @Expose
    private int noofUnit;

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getNoofUnit() {
        return noofUnit;
    }

    public void setNoofUnit(int noofUnit) {
        this.noofUnit = noofUnit;
    }
}
