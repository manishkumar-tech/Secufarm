package com.weather.risk.mfi.myfarminfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NutritionMngtResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("imagefile")
    @Expose
    private String imagefile;
    @SerializedName("cropid")
    @Expose
    private Integer cropid;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("ImageTitle")
    @Expose
    private String imageTitle;
    @SerializedName("cropname")
    @Expose
    private String cropname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagefile() {
        return imagefile;
    }

    public void setImagefile(String imagefile) {
        this.imagefile = imagefile;
    }

    public Integer getCropid() {
        return cropid;
    }

    public void setCropid(Integer cropid) {
        this.cropid = cropid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

}
