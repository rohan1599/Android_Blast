package com.example.demotry.getSOType;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataItem implements Serializable {
    @SerializedName("DOCTYPE_CODE")
    private String doctypecode;
    @SerializedName("DESCRIPTION")
    private String desc;

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
}
