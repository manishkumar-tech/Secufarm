package com.weather.risk.mfi.myfarminfo.multiplelanguages;

public class SingleGlobal {
    private static SingleGlobal instance;

    // Global variable
    private String dataLang;

    // Restrict the constructor from being instantiated
    private SingleGlobal(){}

    public void setData(String d){
        this.dataLang=d;
    }
    public String getData(){
        return this.dataLang;
    }

    public static synchronized SingleGlobal getInstance(){
        if(instance==null){
            instance=new SingleGlobal();
        }
        return instance;
    }

}

