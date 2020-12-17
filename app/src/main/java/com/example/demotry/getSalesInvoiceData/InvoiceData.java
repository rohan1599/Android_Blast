package com.example.demotry.getSalesInvoiceData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceData implements Serializable {
    @SerializedName("InvoiceNo")
    private String InvoiceNo;
    @SerializedName("InvoiceDate")
    private String InvoiceDate;
    @SerializedName("INVOICE_AMOUNT")
    private String INVOICE_AMOUNT;
    @SerializedName("BILLTOPARTY")
    private String BILLTOPARTY;
    @SerializedName("CustName")
    private String CustName;

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

    public String getBILLTOPARTY() {
        return BILLTOPARTY;
    }

    public void setBILLTOPARTY(String BILLTOPARTY) {
        this.BILLTOPARTY = BILLTOPARTY;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    @Override
    public String toString() {
        return "InvoiceData{" +
                "InvoiceNo='" + InvoiceNo + '\'' +
                ", InvoiceDate='" + InvoiceDate + '\'' +
                ", INVOICE_AMOUNT='" + INVOICE_AMOUNT + '\'' +
                ", BILLTOPARTY='" + BILLTOPARTY + '\'' +
                ", CustName='" + CustName + '\'' +
                '}';
    }
}
