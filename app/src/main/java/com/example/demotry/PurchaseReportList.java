package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.demotry.Adapters.PurchaseAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getPurchaseInvoiceData.InvoiceData;
import com.example.demotry.getPurchaseInvoiceData.PurchaseInvoiceDataResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseReportList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String Company_code,Plant_code,Vendor_code,txt4,txt5,txt6;
    RecyclerView recyclerView;
    private EditText et_search;
    private List<InvoiceData> invoiceData;
    private PurchaseAdapter purchaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_reportlist);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        et_search = findViewById(R.id.et_search);
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            token = bundle.getString("token");
            invoiceData = (List<InvoiceData>) bundle.getSerializable("data");
           /* Plant_code = bundle.getString("Plant_code");
            txt4 = bundle.getString("date1");
            txt5 = bundle.getString("date2");
            Vendor_code = bundle.getString("Vendor_code");*/


        }
        init();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    filter(s.toString());
            }
        });

        //getPurchaseInvoceData(token,Company_code,Plant_code,Vendor_code,txt4,txt5);
    }

    private void getPurchaseInvoceData(String token, String Company_code, String Plant_code, String Vendor_code, String txt4, String txt5) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_code);
        productdata.put("VendorCode",Vendor_code);
        productdata.put("FromDate",txt4);
        productdata.put("ToDate",txt5);
        Call<PurchaseInvoiceDataResponse> pchinvoice  = jsonPlaceHolderApi.pchinvoice(token,productdata);
        pchinvoice.enqueue(new Callback<PurchaseInvoiceDataResponse>() {
            @Override
            public void onResponse(Call<PurchaseInvoiceDataResponse> call, Response<PurchaseInvoiceDataResponse> response) {
                if(response.isSuccessful())
                {
                    PurchaseInvoiceDataResponse dataResponse = response.body();
                    invoiceData = dataResponse.getData();
                    Log.d("TAG","Purchase Invoice Data : "+ invoiceData);

                    init();
                }
            }

            @Override
            public void onFailure(Call<PurchaseInvoiceDataResponse> call, Throwable t) {

            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PurchaseReportList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        purchaseAdapter = new PurchaseAdapter(invoiceData,getApplicationContext());
        purchaseAdapter.setInvoiceData(invoiceData);
        recyclerView.setAdapter(purchaseAdapter);
    }

    private void filter(String text) {
        ArrayList<InvoiceData> filterdNames = new ArrayList<>();

        for(InvoiceData s : invoiceData){//if the existing elements contains the search input
            if (s.getVendName().toLowerCase().contains(text.toLowerCase()) || s.getInvoiceNo().toLowerCase().contains(text.toLowerCase())
                        || s.getVENDOR_NO().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        purchaseAdapter.filterList(filterdNames);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PurchaseReportList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(PurchaseReportList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
