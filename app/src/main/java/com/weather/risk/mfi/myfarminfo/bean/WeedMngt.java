package com.weather.risk.mfi.myfarminfo.bean;

public class WeedMngt {
    String imageName;
    String DiseaseInsectName;
    String ScientificName;
    String DamageIntensity;
    String MngtPreEmergence;
    String MngtPostEmergence;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDiseaseInsectName() {
        return DiseaseInsectName;
    }

    public void setDiseaseInsectName(String diseaseInsectName) {
        DiseaseInsectName = diseaseInsectName;
    }

    public String getScientificName() {
        return ScientificName;
    }

    public void setScientificName(String symptoms) {
        ScientificName = symptoms;
    }

    public String getDamageIntensity() {
        return DamageIntensity;
    }

    public void setDamageIntensity(String favCond) {
        DamageIntensity = favCond;
    }

    public String getMngtPreEmergence() {
        return MngtPreEmergence;
    }

    public void setMngtPreEmergence(String conMeas) {
        MngtPreEmergence = conMeas;
    }

    public String getMngtPostEmergence() {
        return MngtPostEmergence;
    }

    public void setMngtPostEmergence(String conMeas) {
        MngtPostEmergence = conMeas;
    }
}
