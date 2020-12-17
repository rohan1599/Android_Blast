package com.example.demotry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.demotry.Adapters.Po_OrderDetailsAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getPoOrderDetails.DataItem;
import com.example.demotry.getPoOrderDetails.OrderDetailResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetails extends AppCompatActivity {
private String orderNo,token,companycode,plantcode,poType;
private JsonPlaceHolderApi jsonPlaceHolderApi;
    RecyclerView recyclerView;
    private Po_OrderDetailsAdapter po_orderDetailsAdapter;
    private List<DataItem> orderDetail;
    private Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent intent= getIntent();
        orderNo = intent.getStringExtra("doc_no");
        Log.d("Tag","Order no : "+orderNo);

        close_btn = findViewById(R.id.close_button);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* Bundle bundle1 = new Bundle();
        token = bundle1.getString("token");
        companycode = bundle1.getString("Company_Code");
        plantcode = bundle1.getString("Plant_Code");
        poType = bundle1.getString("POType");*/

        SharedPreferences mPrefs = getSharedPreferences("orderDetails", 0);
        token = mPrefs.getString("token","");
        companycode = mPrefs.getString("Company_code","");
        plantcode = mPrefs.getString("Plant_code","");
        poType = mPrefs.getString("POType","");

        Log.d("TAG","Token "+ token);
        getOrderDetails(token,companycode,plantcode,poType,orderNo);
    }

    private void getOrderDetails(final String token, String companycode, String plantcode, String poType, final String orderNo) {


        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);
        //api/Master/GetVendorInvoiceData
        //{"Company_Code":"1000","Plant_Code":"1101","VendorCode":"600033","Option":"0"}
        HashMap<String, String> poorderDetail = new HashMap<>();
        poorderDetail.put("Company_Code", companycode);
        poorderDetail.put("Plant_Code", plantcode);
        poorderDetail.put("POType", poType);
        poorderDetail.put("Doc_No", orderNo);
       /* Log.d("TAG","Vendor Invoice Response "+ Company_code);
        Log.d("TAG","Vendor Invoice Response "+ Plant_Code);
        Log.d("TAG","Vendor Invoice Response "+ Vendor_code);
        Log.d("TAG","Vendor Invoice Response "+ radioselected);
        Log.d("TAG","Vendor Invoice Response "+ token);*/
        Call<OrderDetailResponse> poorderDetailcall = jsonPlaceHolderApi.poorderDetails(token, poorderDetail);
        poorderDetailcall.enqueue(new Callback<OrderDetailResponse>() {
            @Override
            public void onResponse(Call<OrderDetailResponse> call, Response<OrderDetailResponse> response) {
                if (response.isSuccessful()) {
                    OrderDetailResponse orderDetailResponse = response.body();
                    orderDetail = orderDetailResponse.getData();
                    Log.d("TAG", "response" + orderDetailResponse);
                    init();
                } else {
                    Log.d("TAG", "response" + response.raw());
                }
            }

            @Override
            public void onFailure(Call<OrderDetailResponse> call, Throwable t) {
                Log.d("TAG", "Failed" + t.getMessage());
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderDetails.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        po_orderDetailsAdapter = new Po_OrderDetailsAdapter(orderDetail, getApplicationContext());
        po_orderDetailsAdapter.getOrderDetails(orderDetail);
        po_orderDetailsAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(po_orderDetailsAdapter);
    }
}
