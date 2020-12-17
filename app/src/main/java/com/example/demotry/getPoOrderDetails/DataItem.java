package com.example.demotry.getPoOrderDetails;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("DocNo")
    private String DocNo;

    @SerializedName("DocDate")
    private String DocDate;

    @SerializedName("VendorName")
    private String VendorName;

    @SerializedName("Material")
    private String Material;

    @SerializedName("MatDesc")
    private String MatDesc;

    @SerializedName("Qty")
    private String Qty;

    @SerializedName("Price")
    private String Price;

    @SerializedName("Amount")
    private String Amount;

    public String getDocNo() {
        return DocNo;
    }

    public void setDocNo(String docNo) {
        DocNo = docNo;
    }

    public String getDocDate() {
        return DocDate;
    }

    public void setDocDate(String docDate) {
        DocDate = docDate;
    }

    public String getVendorName() {
        return VendorName;
    }

    public void setVendorName(String vendorName) {
        VendorName = vendorName;
    }

    public String getMaterial() {
        return Material;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public String getMatDesc() {
        return MatDesc;
    }

    public void setMatDesc(String matDesc) {
        MatDesc = matDesc;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "DocNo='" + DocNo + '\'' +
                ", DocDate='" + DocDate + '\'' +
                ", VendorName='" + VendorName + '\'' +
                ", Material='" + Material + '\'' +
                ", MatDesc='" + MatDesc + '\'' +
                ", Qty='" + Qty + '\'' +
                ", Price='" + Price + '\'' +
                ", Amount='" + Amount + '\'' +
                '}';
    }
}
