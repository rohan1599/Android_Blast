package com.example.demotry.getCustomer;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("CUSTOMER_NO")
    private  String customer_no;
    @SerializedName("CustName")
    private String custname;

    public String getCustomer_no() {
        return customer_no;
    }

    public void setCustomer_no(String customer_no) {
        this.customer_no = customer_no;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }
}
