package com.example.demotry.getPurchaseInvoiceData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PurchaseInvoiceDataResponse implements Serializable {
    @SerializedName("msg")
    private String msg;
    @SerializedName("flag")
    private String flag;
    @SerializedName("data")
    private List<InvoiceData> data;

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

    public List<InvoiceData> getData() {
        return data;
    }

    public void setData(List<InvoiceData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PurchaseInvoiceData{" +
                "msg='" + msg + '\'' +
                ", flag='" + flag + '\'' +
                ", data=" + data +
                '}';
    }
}
