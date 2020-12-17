package com.example.demotry.getMaterialGroup;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("MATGROUP_CODE")
    private String matgroup_code;

    @SerializedName("DESCRIPTION")
    private String desc;

    public String getMatgroup_code() {
        return matgroup_code;
    }

    public void setMatgroup_code(String matgroup_code) {
        this.matgroup_code = matgroup_code;
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
                "matgroup_code='" + matgroup_code + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
