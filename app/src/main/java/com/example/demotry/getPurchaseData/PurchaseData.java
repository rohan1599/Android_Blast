package com.example.demotry.getPurchaseData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurchaseData implements Serializable {

     @SerializedName("DocNo")
     private String DocNo;
     @SerializedName("DocDate")
     private String DocDate;
     @SerializedName("VendorName")
     private String VendorName;
     @SerializedName("Amt")
     private String Amt;

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

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    @Override
    public String toString() {
        return "PurchaseData{" +
                "DocNo='" + DocNo + '\'' +
                ", DocDate='" + DocDate + '\'' +
                ", VendorName='" + VendorName + '\'' +
                ", Amt='" + Amt + '\'' +
                '}';
    }
}
