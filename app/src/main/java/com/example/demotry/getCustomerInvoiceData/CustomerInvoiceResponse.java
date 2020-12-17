package com.example.demotry.getCustomerInvoiceData;

import com.example.demotry.getCustomer.DataItem;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomerInvoiceResponse implements Serializable {


    @SerializedName("data")
    private List<CustomerInvoiceData> data;
    @SerializedName("msg")
    private String msg;
    @SerializedName("flag")
    private String flag;

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

    public List<CustomerInvoiceData> getData() {
        return data;
    }

    public void setData(List<CustomerInvoiceData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CustomerInvoiceResponse{" +
                "msg='" + msg + '\'' +
                ", flag='" + flag + '\'' +
                ", data=" + data +
                '}';
    }
}
