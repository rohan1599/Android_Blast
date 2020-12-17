package com.example.demotry.getPRdata;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PRData {

    @SerializedName("msg")
    private String msg;

    @SerializedName("flag")
    private String flag;

    @SerializedName("data")
    private List<DataItem> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }
}
