package com.weather.risk.mfi.myfarminfo.bean;

import java.io.Serializable;

public class PopBean implements Serializable {
    String title;
    String value;
    String id;
    String image;


    public PopBean(){

    }

    public PopBean(String title,String value,String id,String image) {
        //super(null);
        this.title=title;
        this.value=value;
        this.id=id;
        this.image=image;
        // TODO Auto-generated constructor stub
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
