package com.example.demotry.AppConfig;

import com.example.demotry.getCompany.CompanyResponse;
import com.example.demotry.getCustomer.CustomerResponse;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceResponse;
import com.example.demotry.getMaterial.MaterialResponse;
import com.example.demotry.getMaterialGroup.MaterialGroupResponse;
import com.example.demotry.getPOType.POTypeResponse;
import com.example.demotry.getPRdata.PRData;
import com.example.demotry.getPayableData.VendorInvoiceResponse;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getPoOrderDetails.OrderDetailResponse;
import com.example.demotry.getPrmDetails.PrmDetails;
import com.example.demotry.getProductData.ProductDataResponse;
import com.example.demotry.getPurchaseData.PurchaseDataResponse;
import com.example.demotry.getPurchaseInvoiceData.PurchaseInvoiceDataResponse;
import com.example.demotry.getSOType.SOTypeResponse;
import com.example.demotry.getSalesInvoiceData.SalesInvoiceDataResponse;
import com.example.demotry.getSavePOResponse.SavePOResponse;
import com.example.demotry.getSavePRDetails.SavePRDetails;
import com.example.demotry.getVendor.VendorResponse;
import com.example.demotry.loginResponse.LoginResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @POST("api/Login/GetLogin")
    Call<LoginResponse> login(@Body HashMap<String, String> post);

    @POST("api/Master/GetCompany")
    Call<CompanyResponse> company(@Header("Authorization") String token);

    @POST("api/Master/GetPlant")
    Call<PlantResponse> plant(@Header("Authorization") String token, @Body HashMap<String, String> company);

    @POST("api/Master/GetMaterialGroup")
    Call<MaterialGroupResponse> groupResponse(@Header("Authorization") String token, @Body HashMap<String, String> company);

    @POST("api/Master/GetMaterial")
    Call<MaterialResponse> MaterialResponse(@Header("Authorization") String token, @Body HashMap<String, String> material);

    @POST("api/Master/GetProductData")
    Call<ProductDataResponse> product(@Header("Authorization") String token, @Body HashMap<String, String> dataproduct);

    @POST("api/Master/GetPOType")
    Call<POTypeResponse> potype(@Header("Authorization") String token);

    @POST("api/Master/GetCustomerRecData")
    Call<CustomerResponse> customer(@Header("Authorization") String token, @Body HashMap<String, String> customerdata);

    @POST("api/Master/GetVendorPayData")
    Call<VendorResponse> vendor(@Header("Authorization") String token, @Body HashMap<String, String> vendordata);

    @POST("api/Master/GetSODocType")
    Call<SOTypeResponse> sotype(@Header("Authorization") String token, @Body HashMap<String, String> SOdata);

    @POST("api/Master/GetCustomerInvoiceData")
    Call<CustomerInvoiceResponse> cinvoice(@Header("Authorization") String token, @Body HashMap<String,String> Invoice);

    @POST("api/Master/GetVendorInvoiceData")
    Call<VendorInvoiceResponse> vinvoice(@Header("Authorization") String token, @Body HashMap<String,String> vendorinvoice);

    @POST("api/Master/GetSalesInvoiceData")
    Call<SalesInvoiceDataResponse> sinvoice(@Header("Authorization") String token, @Body HashMap<String,String> saleinvoice);

    @POST("api/Master/GetPurchaseInvoiceData")
    Call<PurchaseInvoiceDataResponse> pchinvoice(@Header("Authorization") String token, @Body HashMap<String,String> purchaseinvoice);

    @POST("api/Master/GetPurchaseData")
    Call<PurchaseDataResponse> purchasedata(@Header("Authorization") String token, @Body HashMap<String,String> pdata);

    @POST("api/Master/SavePOApproval")
    Call<SavePOResponse> savedata(@Header("Authorization") String token, @Body HashMap<String,String> savepodata);

    @POST("api/Master/GetPurchaseItemData")
    Call<OrderDetailResponse> poorderDetails(@Header("Authorization") String token, @Body HashMap<String,String> podata);

    @POST("api/Master/GetPRType")
    Call<PRData> pr_data(@Header("Authorization") String token);

    @POST("api/Master/GetPRLineItemDetail")
    Call<PrmDetails> prmDetails(@Header("Authorization") String token, @Body HashMap<String,String> prmdata);

    @POST("api/Master/SavePRApproval")
    Call<SavePRDetails> saveDetails(@Header("Authorization") String token, @Body HashMap<String,String> savedata);

}
