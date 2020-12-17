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

import com.example.demotry.Adapters.ReceivableAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceData;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveableList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String Company_code,Plant_code,Customer_code,radioselected;
    private EditText et_search;
    private List<CustomerInvoiceData> dataItem;
    private RecyclerView recyclerView;
    private ReceivableAdapter receivableAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveable_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        et_search = findViewById(R.id.et_search);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            dataItem = (List<CustomerInvoiceData>) bundle.getSerializable("data");

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

      //  getCustomerInvoicedata(token,Company_code,Plant_code,Customer_code,radioselected);

    }

        private void filter(String text) {
        ArrayList<CustomerInvoiceData> filterdNames = new ArrayList<>();
        final String[] itemNames= new String[dataItem.size()];

        for(CustomerInvoiceData s : dataItem) {//if the existing elements contains the search input
            if (s.getDoc_NO().toLowerCase().contains(text.toLowerCase()) || s.getCustName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
            receivableAdapter.filterList(filterdNames);
    }

    private void getCustomerInvoicedata(String token, String Company_code, String Plant_code, String Customer_code, String radioselected) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> Invoice = new HashMap<>();
        Invoice.put("Company_Code",Company_code);
        Invoice.put("Plant_Code",Plant_code);
        Invoice.put("CustomerCode",Customer_code);
        Invoice.put("Option",radioselected);
       /* Log.d("TAG","Radio buttond select :"+ radioselected);
        Log.d("TAG","Radio buttond select :"+ token);
        Log.d("TAG","Radio buttond select :"+ Invoice);*/
        Call<CustomerInvoiceResponse> invoicedata = jsonPlaceHolderApi.cinvoice(token,Invoice);
        invoicedata.enqueue(new Callback<CustomerInvoiceResponse>() {
            @Override
            public void onResponse(Call<CustomerInvoiceResponse> call, Response<CustomerInvoiceResponse> response) {
                if(response.isSuccessful()) {
                    CustomerInvoiceResponse customerInvoiceResponse = response.body();
                    Log.d("TAG", "Customer Invoice Data " + customerInvoiceResponse);
                   dataItem = customerInvoiceResponse.getData();
                    Log.d("TAG", "Customer Invoice Data " + dataItem);
                    init();

                }
                else
                {
                    Log.d("TAG", "Failure Error:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<CustomerInvoiceResponse> call, Throwable t) {
                Log.d("TAG", "Failure Error:" + t.getMessage());
            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReceiveableList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        receivableAdapter = new ReceivableAdapter(dataItem,getApplicationContext());
        receivableAdapter.setDataInvoice(dataItem);
        recyclerView.setAdapter(receivableAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(ReceiveableList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(ReceiveableList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
