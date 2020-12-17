package com.example.demotry.getPayableData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VendorInvoiceResponse implements Serializable {

    @SerializedName("data")
    private List<InvoiceData> data;
    @SerializedName("flag")
    private String flag;
    @SerializedName("msg")
    private String msg;

    public List<InvoiceData> getData() {
        return data;
    }

    public void setData(List<InvoiceData> data) {
        this.data = data;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "VendorInvoiceResponse{" +
                "data=" + data +
                ", flag='" + flag + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
