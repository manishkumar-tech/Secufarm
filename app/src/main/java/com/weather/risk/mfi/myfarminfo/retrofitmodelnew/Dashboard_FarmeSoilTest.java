package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard_FarmeSoilTest {
    @SerializedName("Available")
    @Expose
    private Boolean available;
    @SerializedName("DataTable")
    @Expose
    private List<Object> dataTable = null;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<Object> getDataTable() {
        return dataTable;
    }

    public void setDataTable(List<Object> dataTable) {
        this.dataTable = dataTable;
    }
}
