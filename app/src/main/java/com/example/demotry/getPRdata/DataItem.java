package com.example.demotry.getPRdata;

import com.google.gson.annotations.SerializedName;

public class DataItem {
    @SerializedName("DOCTYPECODE")
    private  String doctypecode;
    @SerializedName("DESCRIPTION")
    private  String desc;

    public String getDoctypecode() {
        return doctypecode;
    }

    public void setDoctypecode(String doctypecode) {
        this.doctypecode = doctypecode;
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
                "doctypecode='" + doctypecode + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
