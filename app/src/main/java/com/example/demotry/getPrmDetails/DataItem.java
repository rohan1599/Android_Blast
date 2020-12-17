package com.example.demotry.getPrmDetails;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataItem implements Serializable {
     @SerializedName("DOC_NO")
     private String doc_no;
     @SerializedName("DOC_ITEM_NO")
     private String DOC_ITEM_NO;
     @SerializedName("MAT_CODE")
     private String MAT_CODE;
     @SerializedName("MAT_DESC")
     private String MAT_DESC;
     @SerializedName("QTY")
     private String QTY;
     @SerializedName("UOM")
     private String UOM;
     @SerializedName("TRACK_NO")
     private String TRACK_NO;
     @SerializedName("DEL_DATE")
     private String DEL_DATE;
     @SerializedName("REQUISITIONAR")
     private String REQUISITIONAR;
     @SerializedName("REMARKS")
     private String REMARKS;
     @SerializedName("RELEASED_IND")
     private String RELEASED_IND;
     @SerializedName("CANCELLED_IND")
     private String CANCELLED_IND;
     @SerializedName("APPROVER")
     private String APPROVER;
     @SerializedName("LEVEL_NO")
     private String LEVEL_NO;
     @SerializedName("FYEAR")
     private String FYEAR;
     @SerializedName("PLANT_CODE")
     private String PLANT_CODE;
     @SerializedName("OBJ_ID")
     private String OBJ_ID;
     @SerializedName("ITEM_VALUE")
     private String ITEM_VALUE;
     @SerializedName("CREATEDON")
     private String CREATEDON;
     @SerializedName("CREATEDBY")
     private String CREATEDBY;
     @SerializedName("MODIFIEDON")
     private String MODIFIEDON;
     @SerializedName("MODIFIEDBY")
     private String MODIFIEDBY;

    public String getDoc_no() {
        return doc_no;
    }

    public void setDoc_no(String doc_no) {
        this.doc_no = doc_no;
    }

    public String getDOC_ITEM_NO() {
        return DOC_ITEM_NO;
    }

    public void setDOC_ITEM_NO(String DOC_ITEM_NO) {
        this.DOC_ITEM_NO = DOC_ITEM_NO;
    }

    public String getMAT_CODE() {
        return MAT_CODE;
    }

    public void setMAT_CODE(String MAT_CODE) {
        this.MAT_CODE = MAT_CODE;
    }

    public String getMAT_DESC() {
        return MAT_DESC;
    }

    public void setMAT_DESC(String MAT_DESC) {
        this.MAT_DESC = MAT_DESC;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getTRACK_NO() {
        return TRACK_NO;
    }

    public void setTRACK_NO(String TRACK_NO) {
        this.TRACK_NO = TRACK_NO;
    }

    public String getDEL_DATE() {
        return DEL_DATE;
    }

    public void setDEL_DATE(String DEL_DATE) {
        this.DEL_DATE = DEL_DATE;
    }

    public String getREQUISITIONAR() {
        return REQUISITIONAR;
    }

    public void setREQUISITIONAR(String REQUISITIONAR) {
        this.REQUISITIONAR = REQUISITIONAR;
    }

    public String getREMARKS() {
        return REMARKS;
    }

    public void setREMARKS(String REMARKS) {
        this.REMARKS = REMARKS;
    }

    public String getRELEASED_IND() {
        return RELEASED_IND;
    }

    public void setRELEASED_IND(String RELEASED_IND) {
        this.RELEASED_IND = RELEASED_IND;
    }

    public String getCANCELLED_IND() {
        return CANCELLED_IND;
    }

    public void setCANCELLED_IND(String CANCELLED_IND) {
        this.CANCELLED_IND = CANCELLED_IND;
    }

    public String getAPPROVER() {
        return APPROVER;
    }

    public void setAPPROVER(String APPROVER) {
        this.APPROVER = APPROVER;
    }

    public String getLEVEL_NO() {
        return LEVEL_NO;
    }

    public void setLEVEL_NO(String LEVEL_NO) {
        this.LEVEL_NO = LEVEL_NO;
    }

    public String getFYEAR() {
        return FYEAR;
    }

    public void setFYEAR(String FYEAR) {
        this.FYEAR = FYEAR;
    }

    public String getPLANT_CODE() {
        return PLANT_CODE;
    }

    public void setPLANT_CODE(String PLANT_CODE) {
        this.PLANT_CODE = PLANT_CODE;
    }

    public String getOBJ_ID() {
        return OBJ_ID;
    }

    public void setOBJ_ID(String OBJ_ID) {
        this.OBJ_ID = OBJ_ID;
    }

    public String getITEM_VALUE() {
        return ITEM_VALUE;
    }

    public void setITEM_VALUE(String ITEM_VALUE) {
        this.ITEM_VALUE = ITEM_VALUE;
    }

    public String getCREATEDON() {
        return CREATEDON;
    }

    public void setCREATEDON(String CREATEDON) {
        this.CREATEDON = CREATEDON;
    }

    public String getCREATEDBY() {
        return CREATEDBY;
    }

    public void setCREATEDBY(String CREATEDBY) {
        this.CREATEDBY = CREATEDBY;
    }

    public String getMODIFIEDON() {
        return MODIFIEDON;
    }

    public void setMODIFIEDON(String MODIFIEDON) {
        this.MODIFIEDON = MODIFIEDON;
    }

    public String getMODIFIEDBY() {
        return MODIFIEDBY;
    }

    public void setMODIFIEDBY(String MODIFIEDBY) {
        this.MODIFIEDBY = MODIFIEDBY;
    }
}
