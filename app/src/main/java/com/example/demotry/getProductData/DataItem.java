package com.example.demotry.getProductData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataItem implements Serializable {
    @SerializedName("MAT_CODE")
    private String mat_code;

    @SerializedName("DESCRIPTION")
    private String desc;

    @SerializedName("Qty")
    private Float qty;

    @SerializedName("Amt")
    private Float amt;

    @SerializedName("MAP_PRICE")
    private Float map_price;

    public String getMat_code() {
        return mat_code;
    }

    public void setMat_code(String mat_code) {
        this.mat_code = mat_code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public Float getAmt() {
        return amt;
    }

    public void setAmt(Float amt) {
        this.amt = amt;
    }

    public Float getMap_price() {
        return map_price;
    }

    public void setMap_price(Float map_price) {
        this.map_price = map_price;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "mat_code='" + mat_code + '\'' +
                ", desc='" + desc + '\'' +
                ", qty=" + qty +
                ", amt=" + amt +
                ", map_price=" + map_price +
                '}';
    }


}
