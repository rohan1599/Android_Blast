package com.example.demotry.getCustomerInvoiceData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerInvoiceData implements Serializable {
    @SerializedName("Doc_NO")
    private String Doc_NO;
    @SerializedName("CustName")
    private String CustName;
    @SerializedName("Doc_Date")
    private String Doc_Date;
    @SerializedName("BASE_AMT")
    private String BASE_AMT;
    @SerializedName("BalanceAmt")
    private String BalanceAmt;

    public String getDoc_NO() {
        return Doc_NO;
    }

    public void setDoc_NO(String doc_NO) {
        Doc_NO = doc_NO;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getDoc_Date() {
        return Doc_Date;
    }

    public void setDoc_Date(String doc_Date) {
        Doc_Date = doc_Date;
    }

    public String getBASE_AMT() {
        return BASE_AMT;
    }

    public void setBASE_AMT(String BASE_AMT) {
        this.BASE_AMT = BASE_AMT;
    }

    public String getBalanceAmt() {
        return BalanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        BalanceAmt = balanceAmt;
    }

    @Override
    public String toString() {
        return "CustomerInvoiceData{" +
                "Doc_NO='" + Doc_NO + '\'' +
                ", CustName='" + CustName + '\'' +
                ", Doc_Date='" + Doc_Date + '\'' +
                ", BASE_AMT='" + BASE_AMT + '\'' +
                ", BalanceAmt='" + BalanceAmt + '\'' +
                '}';
    }
}
