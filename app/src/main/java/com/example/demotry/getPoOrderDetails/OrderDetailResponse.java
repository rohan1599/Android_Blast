package com.example.demotry.getPoOrderDetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailResponse {
    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("flag")
    private String flag;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "msg='" + msg + '\'' +
                ", data=" + data +
                ", flag='" + flag + '\'' +
                '}';
    }
}
