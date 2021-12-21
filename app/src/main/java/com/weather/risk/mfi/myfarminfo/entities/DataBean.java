package com.weather.risk.mfi.myfarminfo.entities;

import com.weather.risk.mfi.myfarminfo.bean.BlockBean;
import com.weather.risk.mfi.myfarminfo.bean.GridBean;
import com.weather.risk.mfi.myfarminfo.bean.VodafoneBean;
import com.weather.risk.mfi.myfarminfo.pest_disease.CropBean;
import com.weather.risk.mfi.myfarminfo.pest_disease.DisBean;


import java.util.ArrayList;

/**
 * Created by Admin on 25-07-2017.
 */
public class DataBean {

    public ArrayList<VillageBean> villageList;

    public ArrayList<BlockBean> blockList;

    public ArrayList<BlockBean> getBlockList() {
        return blockList;
    }

    public void setBlockList(ArrayList<BlockBean> blockList) {
        this.blockList = blockList;
    }

    public ArrayList<VillageBean> getCityList() {
        return villageList;
    }

    public void setCityList(ArrayList<VillageBean> cuisineList) {
        this.villageList = cuisineList;
    }



    public ArrayList<CropBean> cropList;
    public ArrayList<DisBean> diseaseList;

    public ArrayList<GridBean> gridList;
    public ArrayList<VodafoneBean> vodaList;






    public ArrayList<CropBean> getCropList() {
        return cropList;
    }

    public void setCropList(ArrayList<CropBean> cList) {
        this.cropList = cList;
    }


    public ArrayList<DisBean> getDiseaseList() {
        return diseaseList;
    }

    public void setDiseaseList(ArrayList<DisBean> dList) {
        this.diseaseList = dList;
    }

    public void setGridList(ArrayList<GridBean> gList) {
        this.gridList = gList;
    }

    public ArrayList<GridBean> getGridList() {
        return gridList;
    }
    public ArrayList<VodafoneBean> getVodaList() {
        return vodaList;
    }

    public void setVodaList(ArrayList<VodafoneBean> cuisineList) {
        this.vodaList = cuisineList;
    }

}