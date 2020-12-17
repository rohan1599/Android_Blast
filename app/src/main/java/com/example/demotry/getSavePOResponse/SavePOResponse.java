package com.example.demotry.getSavePOResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SavePOResponse implements Serializable {
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

    @Override
    public String toString() {
        return "SavePOResponse{" +
                "msg='" + msg + '\'' +
                ", flag='" + flag + '\'' +
                ", data=" + data +
                '}';
    }
}
