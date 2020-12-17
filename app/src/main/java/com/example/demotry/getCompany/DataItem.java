package com.example.demotry.getCompany;

import com.google.gson.annotations.SerializedName;

public class DataItem {

    @SerializedName("COMPANY_CODE")
    private String company_code;

    @SerializedName("COMPANY_NAME")
    private String company_name;

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "company_code='" + company_code + '\'' +
                ", company_name='" + company_name + '\'' +
                '}';
    }
}
