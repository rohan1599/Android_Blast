package com.example.demotry.getPlant;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("Plant_Code")
    private String plant_code;

    @SerializedName("DESCRIPTION")
    private String desc;

    public String getPlant_code() {
        return plant_code;
    }

    public void setPlant_code(String plant_code) {
        this.plant_code = plant_code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "plant_code='" + plant_code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
