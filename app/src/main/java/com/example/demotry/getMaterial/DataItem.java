package com.example.demotry.getMaterial;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("Mat_Code")
    private String mat_code;

    @SerializedName("Description")
    private String desc;

    public String getMat_code() {
        return mat_code;
    }

    public void setMat_code(String mat_code) {
        this.mat_code = mat_code;
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
                "mat_code='" + mat_code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
