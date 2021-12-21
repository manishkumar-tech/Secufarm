package com.weather.risk.mfi.myfarminfo.retrofitmodelnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard_FarmeLstnextPopDT {

    @SerializedName("StageName")
    @Expose
    private String stageName;
    @SerializedName("StageName_Regeional")
    @Expose
    private String stageNameRegeional;
    @SerializedName("WorkName")
    @Expose
    private String workName;
    @SerializedName("WorkName_Regeional")
    @Expose
    private String workNameRegeional;
    @SerializedName("Work")
    @Expose
    private String work;
    @SerializedName("Work_Regeional")
    @Expose
    private String workRegeional;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Message_Regeional")
    @Expose
    private String messageRegeional;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageNameRegeional() {
        return stageNameRegeional;
    }

    public void setStageNameRegeional(String stageNameRegeional) {
        this.stageNameRegeional = stageNameRegeional;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkNameRegeional() {
        return workNameRegeional;
    }

    public void setWorkNameRegeional(String workNameRegeional) {
        this.workNameRegeional = workNameRegeional;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getWorkRegeional() {
        return workRegeional;
    }

    public void setWorkRegeional(String workRegeional) {
        this.workRegeional = workRegeional;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageRegeional() {
        return messageRegeional;
    }

    public void setMessageRegeional(String messageRegeional) {
        this.messageRegeional = messageRegeional;
    }
}
