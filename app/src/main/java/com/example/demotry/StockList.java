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

import com.example.demotry.Adapters.StockAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.getProductData.DataItem;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class StockList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String Company_code,Plant_code,Material_grp_code,Material_code;
    RecyclerView recyclerView;
    StockAdapter stockAdapter;
    List<DataItem> data  ;
    private EditText et_search;
    private  String search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);

        et_search = findViewById(R.id.et_search);
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
             data = (List<DataItem>) bundle.getSerializable("data");
            //Log.d("TAGparaC","comcode"+ data);
           /*  Plant_code = bundle.getString("Plant_Code");
             Material_grp_code = bundle.getString("Material_grp_code");
             Material_code = bundle.getString("Material_code");*/
             token = bundle.getString("token");

        }

       init();
        Log.d("TAGC","comcode"+ Company_code);
      et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });



    }

   private void filter(String text) {
        ArrayList<DataItem> filterdNames = new ArrayList<>();
        final String[] itemNames= new String[data.size()];

           for(DataItem s : data){//if the existing elements contains the search input
           if (s.getDesc().toLowerCase().contains(text.toLowerCase()) || s.getMat_code().toLowerCase().contains(text.toLowerCase())) {
               //adding the element to filtered list
               filterdNames.add(s);
           }
       }

       //calling a method of the adapter class and passing the filtered list
        stockAdapter.filterList(filterdNames);
    }


  /*  private void getproductdata(String token,String Company_code, String Plant_code, String Material_grp_code, String Material_code) {


        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_code);
        productdata.put("Material_Group",Material_grp_code);
        Call<ProductDataResponse> product = jsonPlaceHolderApi.product(token,productdata);
        product.enqueue(new Callback<ProductDataResponse>() {
            @Override
            public void onResponse(Call<ProductDataResponse> call, Response<ProductDataResponse> response) {

                if(response.isSuccessful()) {



                    assert response.body() != null;
                    data = response.body().getData();

                    init();

                    Log.d("TAG","StockList Data" + response.body());
                    Log.d("TAG", "StockList Data" + response.body().getFlag());
                   /* if(response.body().getFlag().equals("False"))
                    {
                        Toast.makeText(StockList.this,"No Record Found",Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Log.d("TAG", "msg Dataitem" + data);
                    }*/


                    //  recyclerView.setItemAnimator(new DefaultItemAnimator());

                    /*ProductDataResponse productDataResponse = response.body();
                    DataItem dataItem = productDataResponse.getData().get(0);
                    String mat_code = dataItem.getMat_code();
                    String mat_desc = dataItem.getDesc();
                    String Qty = dataItem.getQty();
                    String amt = dataItem.getAmt();
                    String price = dataItem.getMap_price();

                    Log.d("TAG", "onResponse :" + productDataResponse.toString());*/
                    //Log.d("TAG", "onResponse Company_Code : " + dataItem.getMat_code());
                    //Log.d("TAG", "onResponse Company_Name : " + mat_desc);
                    // Log.d("TAG", "Quantity :" + Qty);
                    // Log.d("TAG", "Amount :" + amt);
                    // Log.d("TAG", "Price :" + price);*/


                /*}
                else{
                    Log.d("TAG", "error unknown Raw :" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ProductDataResponse> call, Throwable t) {
                Log.d("TAG", "Failure Error:" + t.getMessage());
            }
        });
    }*/

    private void init() {

        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StockList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setItemAnimator(new DefaultItemAnimator());
       //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        stockAdapter = new StockAdapter(data,getApplicationContext());
        stockAdapter.setDataItem(data);
        recyclerView.setAdapter(stockAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(StockList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(StockList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

}
