package com.weather.risk.mfi.myfarminfo.cattledashboard;

public class CattleObject {

    private String id;
    private String name;

    public CattleObject(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CattleObject){
            CattleObject c = (CattleObject)obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }

}