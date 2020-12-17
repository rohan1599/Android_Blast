package com.example.demotry.getPurchaseData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PurchaseDataResponse implements Serializable {
    @SerializedName("msg")
    private String msg;
    @SerializedName("flag")
    private String flag;
    @SerializedName("data")
    private List<PurchaseData> data;

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

    public List<PurchaseData> getData() {
        return data;
    }

    public void setData(List<PurchaseData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PurchaseDataResponse{" +
                "msg='" + msg + '\'' +
                ", flag='" + flag + '\'' +
                ", data=" + data +
                '}';
    }
}
