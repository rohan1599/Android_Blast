package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.demotry.Adapters.SalesAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getSalesInvoiceData.InvoiceData;
import com.example.demotry.getSalesInvoiceData.SalesInvoiceDataResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String Company_code,Plant_code,SOtype_code,txt4,txt5,Cust_code;
    RecyclerView recyclerView;
    List<InvoiceData> data ;
    private EditText et_search;
    private SalesAdapter salesAdapter;
    private ProgressDialog mprogressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

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
        if (bundle != null) {
            data = (List<InvoiceData>) bundle.getSerializable("data");
            /*Plant_code = bundle.getString("Plant_code");
            txt4 = bundle.getString("date1");
            txt5 = bundle.getString("date2");
            Cust_code = bundle.getString("Cust_code");
            SOtype_code = bundle.getString("SOtype_code");*/
            token = bundle.getString("token");


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

      //  getSalesInvoiceData(token,Company_code,Plant_code,SOtype_code,Cust_code,txt4,txt5);
    }

    private void getSalesInvoiceData(String token, String Company_code, String Plant_code, String SOtype_code,String Cust_code, String txt4, String txt5) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_code);
        productdata.put("SOType",SOtype_code);
        productdata.put("CUSTOMER_NO",Cust_code);
        productdata.put("FromDate",txt5);
        productdata.put("ToDate",txt4);
        /*Log.d("TAG","Message " + Company_code);
        Log.d("TAG","Message " + Plant_code);
        Log.d("TAG","Message " + SOtype_code);
        Log.d("TAG","Message " + Cust_code);
        Log.d("TAG","Message " + txt4);
        Log.d("TAG","Message " + txt5);*/
        Call<SalesInvoiceDataResponse> saleinvoice = jsonPlaceHolderApi.sinvoice(token,productdata);
        saleinvoice.enqueue(new Callback<SalesInvoiceDataResponse>() {
            @Override
            public void onResponse(Call<SalesInvoiceDataResponse> call, Response<SalesInvoiceDataResponse> response) {
                if(response.isSuccessful())
                {
                    SalesInvoiceDataResponse saleinvoiceData = response.body();
                    Log.d("TAG","SalesInvoice Data "+ saleinvoiceData);
                    data = saleinvoiceData.getData();
                    Log.d("TAG","SalesInvoice Data"+data);
                    init();
                }
                else
                {
                    Log.d("TAG","Failed"+ response.code());
                }
            }

            @Override
            public void onFailure(Call<SalesInvoiceDataResponse> call, Throwable t) {
                Log.d("TAG","Server Failed"+ t.getMessage());
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SalesList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        salesAdapter = new SalesAdapter(data,getApplicationContext());
        salesAdapter.setInvoiceData(data);
        recyclerView.setAdapter(salesAdapter);
    }

    private void filter(String text) {
       ArrayList<InvoiceData> filterdNames = new ArrayList<>();
       // final String[] itemNames= new String[data.size()];

        for(InvoiceData s : data) {//if the existing elements contains the search input
            if (s.getCustName().contains(text.toLowerCase()) || s.getInvoiceNo().contains(text.toLowerCase()) ){
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        salesAdapter.filterList(filterdNames);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(SalesList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(SalesList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
