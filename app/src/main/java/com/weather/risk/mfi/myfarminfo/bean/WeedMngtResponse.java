package com.weather.risk.mfi.myfarminfo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeedMngtResponse {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ScientificName")
    @Expose
    private String scientificName;
    @SerializedName("DamageIntensity")
    @Expose
    private String damageIntensity;
    @SerializedName("PreManagement")
    @Expose
    private String preManagement;
    @SerializedName("PostManagement")
    @Expose
    private String postManagement;
    @SerializedName("imagefile")
    @Expose
    private String imagefile;
    @SerializedName("cropid")
    @Expose
    private Integer cropid;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("cropname")
    @Expose
    private String cropname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getDamageIntensity() {
        return damageIntensity;
    }

    public void setDamageIntensity(String damageIntensity) {
        this.damageIntensity = damageIntensity;
    }

    public String getPreManagement() {
        return preManagement;
    }

    public void setPreManagement(String preManagement) {
        this.preManagement = preManagement;
    }

    public String getPostManagement() {
        return postManagement;
    }

    public void setPostManagement(String postManagement) {
        this.postManagement = postManagement;
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

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

}