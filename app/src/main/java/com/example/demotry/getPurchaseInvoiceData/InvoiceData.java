package com.example.demotry.getPurchaseInvoiceData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceData implements Serializable {
    @SerializedName("InvoiceNo")
    private String InvoiceNo;
    @SerializedName("InvoiceDate")
    private String InvoiceDate;
    @SerializedName("INVOICE_AMOUNT")
    private String INVOICE_AMOUNT;
    @SerializedName("VENDOR_NO")
    private String VENDOR_NO;
    @SerializedName("VendName")
    private String VendName;

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public String getINVOICE_AMOUNT() {
        return INVOICE_AMOUNT;
    }

    public void setINVOICE_AMOUNT(String INVOICE_AMOUNT) {
        this.INVOICE_AMOUNT = INVOICE_AMOUNT;
    }

    public String getVENDOR_NO() {
        return VENDOR_NO;
    }

    public void setVENDOR_NO(String VENDOR_NO) {
        this.VENDOR_NO = VENDOR_NO;
    }

    public String getVendName() {
        return VendName;
    }

    public void setVendName(String vendName) {
        VendName = vendName;
    }

    @Override
    public String toString() {
        return "InvoiceData{" +
                "InvoiceNo='" + InvoiceNo + '\'' +
                ", InvoiceDate='" + InvoiceDate + '\'' +
                ", INVOICE_AMOUNT='" + INVOICE_AMOUNT + '\'' +
                ", VENDOR_NO='" + VENDOR_NO + '\'' +
                ", VendName='" + VendName + '\'' +
                '}';
    }
}
