package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getCompany.CompanyResponse;
import com.example.demotry.getCompany.DataItem;
import com.example.demotry.getCustomer.CustomerResponse;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceData;
import com.example.demotry.getCustomerInvoiceData.CustomerInvoiceResponse;
import com.example.demotry.getPlant.PlantResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class receiveables extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private RadioGroup radiogrp;
    private RadioButton radioButton;
    private String token;
    //to store code
    private List<String> sl_cmycode = new ArrayList<>();
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_custcode= new ArrayList<>();
    //to display value in spinner
    private ArrayList<String> al_company = new ArrayList<>();
    private ArrayList<String> al_plant= new ArrayList<>();
    private ArrayList<String> al_customer= new ArrayList<>();
    //array adapters
    private ArrayAdapter company_adapter,plant_adapter,customer_adapter;
    //spinners
    private MaterialSpinner sp_company,sp_plant,sp_customer;
    //textview to store selected values n pass to next activity
    private String companyCode;
    private String plantcode;
    private String Customer_code;
    //shared preferences data store
    private String company_Code,plant_Code;
    //Button
    private Button btn_receivableDetails;
    private ProgressDialog mProgressDialog;
    private List<CustomerInvoiceData> dataItem;
    private String customer_code;
    private String customer_name;
    //shared preferences
    private SharedPreferences receiveprefs;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveables);

      Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            token = bundle.getString("token");
        }

        receiveprefs = getSharedPreferences("ReceivableSettings", Context.MODE_PRIVATE);
        editor = receiveprefs.edit();
         getAllSharedPreferences();

        if(token != null) {
            getcompany(token);
        }


        btn_receivableDetails = findViewById(R.id.btn_receivableDetails);
        sp_company = findViewById(R.id.sp_company);
        sp_plant = findViewById(R.id.sp_plant);
        sp_customer = findViewById(R.id.sp_customer);

       radiogrp = findViewById(R.id.radio);


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

        btn_receivableDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveableList();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        sp_company.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    // Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_black_24dp);
                    // errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
                    // spinner1.setErrorIconDrawable(errorIcon);
                    sp_company.setError("Please select required field");
                    sp_company.setHintEnabled(false);
                    return;
                } else {
                    sp_company.setError(null);
                    sp_company.setHintEnabled(true);

                    String item = materialSpinner.getSelectedItem().toString();
                    // String companycode = spinnerCmpCode.get(i);
                    long code = materialSpinner.getSelectedItemId();


                    //  Toast.makeText(adapterView.getContext(), "Setected : " + item, Toast.LENGTH_SHORT).show();
                    editor.putInt("company_pos", i).apply();
                    int pos_select = receiveprefs.getInt("company_pos", 0);

                    companyCode = sl_cmycode.get(pos_select);
                    editor.putString("Company_code", companyCode).apply();
                    getplant(token, companyCode);
                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });
        sp_plant.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0) {
                    sp_plant.setError("Please select required field");
                    sp_plant.setHintEnabled(false);
                    return;
                }
                else{
                    sp_plant.setError(null);
                    sp_plant.setHintEnabled(true);

                    editor.putInt("plant_pos", i).apply();
                    int _pos = receiveprefs.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(_pos);
                    editor.putString("Plant_code", plantcode).apply();

                        getCustomer(token, companyCode, plantcode);


                    }
                }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });
        sp_customer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0)
                {
                    sp_customer.setError("Please select required field");
                    sp_customer.setHintEnabled(false);
                    return;
                }
                else{
                    sp_customer.setError(null);
                    sp_customer.setHintEnabled(true);

                    String custvalue = materialSpinner.getSelectedItem().toString();
                    Customer_code = sl_custcode.get(i);
                    //txt3 = custcode;
                    //Log.d("Customer value","Customer"+ custcode);
                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "error unknown Raw : Nothing Selected");
            }
        });
    }

    private void getAllSharedPreferences() {
        company_Code = receiveprefs.getString("Company_code", "");
        plant_Code = receiveprefs.getString("Plant_code", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllSharedPreferences();
    }

    private void getcompany(final String token) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);


        Call<CompanyResponse> callcompany = jsonPlaceHolderApi.company(token);
        callcompany.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {

                    CompanyResponse companyResponse = response.body();
                    DataItem companydata = companyResponse.getData().get(0);
                   // String flag = companyResponse.getFlag();
                    String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();
                    final String companyCode = company_code;

                    al_company.clear();
                    sl_cmycode.clear();
                    al_company.add(0,"--SELECT COMPANY--");
                    sl_cmycode.add(0," ");
                    sl_cmycode.add(company_code);
                    al_company.add(Name);

                    Log.d("TAG", "onResponse: " + companydata.toString());
                            updateCompanySpinner();
                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(receiveables.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(receiveables.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void updateCompanySpinner() {
        int company_pos = 1;
        if(company_Code.trim().length()>0)
        {
            company_pos = receiveprefs.getInt("company_pos",0);
        }
        company_adapter = new ArrayAdapter<String>(receiveables.this, android.R.layout.simple_spinner_dropdown_item, al_company);
        company_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_company.setAdapter(company_adapter);
        sp_company.setSelection(company_pos);
    }

    private void getplant(final String token, final String companyCode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> CompanyId = new HashMap<>();
        CompanyId.put("Company_Code",companyCode);
        Call<PlantResponse> plant = jsonPlaceHolderApi.plant( token,CompanyId);
        plant.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {

                if (response.isSuccessful()) {
                    PlantResponse plantResponse = response.body();
                    //String flag = plantResponse.getFlag();

                    //Log.d("TAG","PlantResponse"+ plantResponse.toString());
                    al_plant.clear();
                    sl_plantcode.clear();
                    al_plant.add(0,"--SELECT PLANT--");
                    sl_plantcode.add(0," ");
                    for(com.example.demotry.getPlant.DataItem size: plantResponse.getData()) {
                        String plant_code = size.getPlant_code();
                        String plantname = size.getDesc();
                        al_plant.add(plantname);
                        sl_plantcode.add(plant_code);
                       // System.out.println(size.toString());
                    }
                    updatePlantSpinner();

               }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(receiveables.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(receiveables.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });

    }

    private void updatePlantSpinner() {
        int plantPos_Click= 0 ;
        if (plant_Code.trim().length() > 0) {
            plantPos_Click = receiveprefs.getInt("plant_pos", 0);
        }

        plant_adapter = new ArrayAdapter<String>(receiveables.this, android.R.layout.simple_spinner_dropdown_item, al_plant);
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_plant.setAdapter(plant_adapter);
        sp_plant.setSelection(plantPos_Click);
    }

    private void getCustomer(String token, String companyCode, String plantcode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> Cust= new HashMap<>();
        Cust.put("Company_Code",companyCode);
        Cust.put("Plant_Code",plantcode);
        Call<CustomerResponse> customer = jsonPlaceHolderApi.customer(token,Cust);
        customer.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, final Response<CustomerResponse> response) {
                if(response.isSuccessful())
                {
                    CustomerResponse customerResponse = response.body();
                    String flag = customerResponse.getFlag();
                  //  Log.d("Customer TAG","Customer : "+ customerResponse.toString());
                   // Log.d("Customer TAG","Customer : "+ customerResponse.getData());


                    al_customer.clear();
                    sl_custcode.clear();

                    sl_custcode.add(0," ");
                    al_customer.add(0,"--SELECT CUSTOMER--");

                 for(com.example.demotry.getCustomer.DataItem cust : customerResponse.getData()) {
                        customer_code = cust.getCustomer_no();
                        customer_name = cust.getCustname();
                       al_customer.add(customer_name);
                       sl_custcode.add(customer_code);
                        // System.out.println(size.toString());
                    }

                    updateCustomerSpinner();
                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(receiveables.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
         }


            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Toast.makeText(receiveables.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });


    }

    private void updateCustomerSpinner() {
        customer_adapter = new ArrayAdapter<String>(receiveables.this, android.R.layout.simple_spinner_dropdown_item, al_customer);
        customer_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_customer.setAdapter(customer_adapter);
    }

    private void getCustomerInvoicedata(final String token, final String companyCode, String plantcode, String Customer_code, String valueOf) {
        mProgressDialog.setMessage("Fetching...");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> Invoice = new HashMap<>();
        Invoice.put("Company_Code",companyCode);
        Invoice.put("Plant_Code",plantcode);
        Invoice.put("CustomerCode",Customer_code);
        Invoice.put("Option",valueOf);
       /* Log.d("TAG","Radio buttond select :"+ companyCode);
        Log.d("TAG","Radio buttond select :"+ plantcode);
        Log.d("TAG","Radio buttond select :"+ Customer_code);
        Log.d("TAG","Radio buttond select :"+ valueOf);*/
        Call<CustomerInvoiceResponse> invoicedata = jsonPlaceHolderApi.cinvoice(token,Invoice);
        invoicedata.enqueue(new Callback<CustomerInvoiceResponse>() {
            @Override
            public void onResponse(Call<CustomerInvoiceResponse> call, Response<CustomerInvoiceResponse> response) {

                if(response.isSuccessful()) {

                    if(response.body().getFlag().equals("False"))
                    {
                        mProgressDialog.hide();
                        Toast.makeText(receiveables.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mProgressDialog.hide();
                        CustomerInvoiceResponse customerInvoiceResponse = response.body();
                       // Log.d("TAG", "Customer Invoice Data " + customerInvoiceResponse);
                        dataItem = customerInvoiceResponse.getData();
                        //Log.d("TAG", "Customer Invoice Data " + dataItem);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) dataItem);
                        bundle.putString("token",token);
                        Intent list = new Intent(receiveables.this, ReceiveableList.class);
                        list.putExtras(bundle);
                        startActivity(list);

                    }
                }
                else
                {
                    mProgressDialog.hide();
                    Log.d("TAG", "Failure Error:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<CustomerInvoiceResponse> call, Throwable t) {
                mProgressDialog.hide();
                Log.d("TAG", "Failure Error:" + t.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token",token);
            Intent intent = new Intent(this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void receiveableList() {
        if(sp_company.getSelection() > 0 && sp_plant.getSelection() > 0 && sp_customer.getSelection() > 0) {
            int radioId = radiogrp.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            //Log.d("Radio","Radio Selected " + radioId);

            int position = radiogrp.indexOfChild(radioButton);
           // Log.d("Radio","Radio Selected " + position);
            getCustomerInvoicedata(token,companyCode,plantcode,Customer_code,String.valueOf(position));
        }
        else if(sp_company.getSelectedItem() == null)
        {
            sp_company.setError("Please select required field");
            sp_company.setHintEnabled(false);
            return;
        }
        else if(sp_plant.getSelectedItem() == null)
        {
            sp_plant.setError("Please select required field");
            sp_plant.setHintEnabled(false);
            return;
        }
        else if(sp_customer.getSelectedItem() == null)
        {
            sp_customer.setError("Please select required field");
            sp_customer.setHintEnabled(false);
            return;
        }
        /*else{
            spinner1.setError("Please select required field");
            spinner1.setHintEnabled(false);
            spinner2.setError("Please select required field");
            spinner2.setHintEnabled(false);
            spinner3.setError("Please select required field");
            spinner3.setHintEnabled(false);

            //radioButton.setError("Please select Types");

        }*/
    }

    public void checkradio(View view) {
        int radioId = radiogrp.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
       Log.d("Radio","Radio Selected " + radioId);

        int position = radiogrp.indexOfChild(radioButton);
        Log.d("Radio","Radio Selected " + position);

        String val = radioButton.getText().toString();
        Log.d("Radio","Radio Selected String " + val);

    }
}
