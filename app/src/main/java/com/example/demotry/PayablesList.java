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

import com.example.demotry.Adapters.PayableAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.getPayableData.InvoiceData;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class PayablesList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String Company_code,Plant_Code,Vendor_code,radioselected;
    private String token;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private RecyclerView recyclerView;
    private EditText editText;
    private List<InvoiceData> dataList;
    private PayableAdapter payableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payables_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            dataList = (List<InvoiceData>) bundle.getSerializable("data");
            Log.d("TAG","comcode"+ dataList);
           /* Plant_Code = bundle.getString("Plant_code");
            Vendor_code = bundle.getString("Vendor_code");
            radioselected = bundle.getString("radio1");*/
            token = bundle.getString("token");
        }

        init();
       editText = findViewById(R.id.et_search);

       editText.addTextChangedListener(new TextWatcher() {
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


       // getVendorInvoiceData(token,Company_code,Plant_Code,Vendor_code,radioselected);
    }

   /* private void getVendorInvoiceData(String token, String Company_code, String Plant_Code, String Vendor_code, String radioselected) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);
        //api/Master/GetVendorInvoiceData
        //{"Company_Code":"1000","Plant_Code":"1101","VendorCode":"600033","Option":"0"}
        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_Code);
        productdata.put("VendorCode",Vendor_code);
        productdata.put("Option",radioselected);
       /* Log.d("TAG","Vendor Invoice Response "+ Company_code);
        Log.d("TAG","Vendor Invoice Response "+ Plant_Code);
        Log.d("TAG","Vendor Invoice Response "+ Vendor_code);
        Log.d("TAG","Vendor Invoice Response "+ radioselected);
        Log.d("TAG","Vendor Invoice Response "+ token);*/
      /*  Call<VendorInvoiceResponse> vendorcall = jsonPlaceHolderApi.vinvoice(token,productdata);
        vendorcall.enqueue(new Callback<VendorInvoiceResponse>() {
            @Override
            public void onResponse(Call<VendorInvoiceResponse> call, Response<VendorInvoiceResponse> response) {
                Log.d("TAG","Vendor Invoice Response "+ response.body());
                if(response.isSuccessful())
                {
                    VendorInvoiceResponse vendorInvoiceResponse = response.body();
                    dataList = vendorInvoiceResponse.getData();
                    init();

                    Log.d("TAG","Vendor Invoice Response "+ dataList);
                }
                else
                {
                    Log.d("TAG","Vendor Invoice Response Failed"+ response.code());
                }

            }

            @Override
            public void onFailure(Call<VendorInvoiceResponse> call, Throwable t) {
                Log.d("TAG"," Failed  "+ t.getMessage());
            }
        });
    }*/

    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PayablesList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        payableAdapter = new PayableAdapter(dataList,getApplicationContext());
        payableAdapter.setDataInvoice(dataList);
        recyclerView.setAdapter(payableAdapter);
    }

    private void filter(String text) {
        ArrayList<InvoiceData> filterdNames = new ArrayList<>();
       // final String[] itemNames = new String[dataList.size()];

        for (InvoiceData s : dataList) {//if the existing elements contains the search input
            if (s.getDoc_NO().toLowerCase().contains(text.toLowerCase()) || s.getCustName().toLowerCase().contains(text.toLowerCase()) ) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        payableAdapter.filterList(filterdNames);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(PayablesList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(PayablesList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
