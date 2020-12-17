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
import com.example.demotry.getPayableData.InvoiceData;
import com.example.demotry.getPayableData.VendorInvoiceResponse;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getVendor.VendorResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
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


public class Payables extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    //to store code
    private List<String> sl_companycode = new ArrayList<>();
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_vendorcode = new ArrayList<>();
    //to display value in spinner
    private ArrayList<String> al_company = new ArrayList<>();
    private ArrayList<String> al_plant = new ArrayList<>();
    private ArrayList<String> al_vendor = new ArrayList<>();
    //array adapters
    private ArrayAdapter company_adapter, plant_adapter, vendor_adapter;
    //spinners
    private MaterialSpinner sp_company, sp_plant, sp_vendor;
    //textview to store selected values n pass to next activity
    private String companycode;
    private String plantcode;
    private String Vendor_code;
    private List<InvoiceData> dataList;
    private int radioselected;
    //Radio Buttons
    private RadioGroup radiogrp;
    private RadioButton radioButton, radioButton2;
    //Button
    private Button btnpay;
    //Radio Button Error
    private TextInputLayout radioerror;
    private ProgressDialog mprogressdialog;
    private SharedPreferences payableprefs;
    private SharedPreferences.Editor editor;
    private String company_Code,plant_Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payables);

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
        if (bundle != null) {
            token = bundle.getString("token");
        }

        payableprefs = getSharedPreferences("PayableSettings", Context.MODE_PRIVATE);
        editor = payableprefs.edit();


        getAllSharedPreferences();

        if (token != null) {
            getcompany(token);
        }

        sp_company = findViewById(R.id.sp_company);
        sp_plant = findViewById(R.id.sp_plant);
        sp_vendor = findViewById(R.id.sp_vendor);
        btnpay = findViewById(R.id.btn_payableDetails);
        radiogrp = findViewById(R.id.radio);

        setTitle("Payables");


        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payablesList();
            }
        });


        mprogressdialog = new ProgressDialog(this);
        mprogressdialog.setIndeterminate(true);

        sp_company.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    sp_company.setError("Please select required field");
                    sp_company.setHintEnabled(false);
                    return;
                } else {
                    sp_company.setError(null);
                    sp_company.setHintEnabled(true);
                    //String id = spinnerArrayList1.get(i);

                    String item = materialSpinner.getSelectedItem().toString();
                   /* final String companycode = sl_companycode.get(i);
                    long code = materialSpinner.getSelectedItemId();
                    Company_code = companycode;*/

                    editor.putInt("company_pos", i).apply();
                    int pos_select = payableprefs.getInt("company_pos", 0);

                    companycode = sl_companycode.get(pos_select);
                    editor.putString("Company_code", companycode).apply();

                     getplant(token, companycode);

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
                if (materialSpinner.getSelection() == 0) {
                    sp_plant.setError("Please select required field");
                    sp_plant.setHintEnabled(false);
                    return;
                } else {
                    sp_plant.setError(null);
                    sp_plant.setHintEnabled(true);
                   /* String plantitem = materialSpinner.getSelectedItem().toString();
                    String plantcode = sl_plantcode.get(i);*/



                    editor.putInt("plant_pos", i).apply();
                    int pos_select = payableprefs.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(pos_select);
                    editor.putString("Plant_code", plantcode).apply();

                    getVendor(token, companycode, plantcode);
                               /* if (sp_company != null && sp_plant != null) {
                                    getVendor(token, companycode, plantcode);

                                }*/
                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });
        sp_vendor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    sp_vendor.setError("Please select required field");
                    sp_vendor.setHintEnabled(false);
                    return;
                } else {
                    sp_vendor.setError(null);
                    sp_vendor.setHintEnabled(true);
                    String vendor = materialSpinner.getSelectedItem().toString();
                    String vcode = sl_vendorcode.get(i);
                    Vendor_code = vcode;
                    Log.d("Vendor Code", "vendor select" + vcode);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });
    }

    private void getAllSharedPreferences() {
        company_Code = payableprefs.getString("Company_code","");
        plant_Code = payableprefs.getString("Plant_code","");
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
                    String flag = companyResponse.getFlag();
                    final String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();
                    sl_companycode.add(0, " ");
                    sl_companycode.add(company_code);
                    al_company.add(0, "--SELECT COMPANY--");
                    al_company.add(Name);

                   /* Log.d("TAG", "onResponse: " + companydata.toString());
                    Log.d("TAG", "onResponse: Company_Code : " + companydata.getCompany_code());
                    Log.d("TAG", "onResponse: Company_Name : " + companydata.getCompany_name());
                    Log.d("TAG", "flag :" + flag);


                    String s = response.body().toString();
                    Log.d("TAG", "Response :" + s);*/

                    updateComapnySpinner();

                      } else {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(Payables.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(Payables.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateComapnySpinner() {
        int company_pos= 1 ;
        if (company_Code.trim().length() > 0) {
            company_pos = payableprefs.getInt("company_pos", 0);
        }
        company_adapter = new ArrayAdapter<String>(Payables.this, android.R.layout.simple_spinner_dropdown_item, al_company);
        company_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_company.setAdapter(company_adapter);
        sp_company.setSelection(company_pos);
    }

    private void getplant(final String token, final String companycode) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> CompanyId = new HashMap<>();
        CompanyId.put("Company_Code", companycode);
        Call<PlantResponse> plant = jsonPlaceHolderApi.plant(token, CompanyId);
        plant.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                if (response.isSuccessful()) {
                    PlantResponse plantResponse = response.body();
                    String flag = plantResponse.getFlag();

                    //  Log.d("TAG","PlantResponse"+ plantResponse.toString());

                    al_plant.clear();
                    sl_plantcode.clear();
                    al_plant.add(0, "--SELECT PLANT--");
                    sl_plantcode.add(0, " ");
                    for (com.example.demotry.getPlant.DataItem size : plantResponse.getData()) {
                        String plant_code = size.getPlant_code();
                        String plantname = size.getDesc();
                        al_plant.add(plantname);
                        sl_plantcode.add(plant_code);
                        //System.out.println(size.toString());
                    }

                    updatePlantSpinner();

                } else {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(Payables.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(Payables.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatePlantSpinner() {
        int plant_pos = 0;
        if(plant_Code.trim().length() > 0){
            plant_pos = payableprefs.getInt("plant_pos",0);
        }
        plant_adapter = new ArrayAdapter<String>(Payables.this, android.R.layout.simple_list_item_1, al_plant);
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_plant.setAdapter(plant_adapter);
        sp_plant.setSelection(plant_pos);
    }

    private void getVendor(String token, String companycode, String plantcode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> vend = new HashMap<>();
        vend.put("Company_Code", companycode);
        vend.put("Plant_Code", plantcode);
        Call<VendorResponse> vendorcall = jsonPlaceHolderApi.vendor(token, vend);
        vendorcall.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                if (response.isSuccessful()) {

                    VendorResponse vendorResponse = response.body();
                    String flag = vendorResponse.getFlag();
                    Log.d("TAG", "vendor" + vendorResponse.getData());

                    al_vendor.clear();
                    sl_vendorcode.clear();
                    al_vendor.add(0, "--SELECT VENDOR--");
                    sl_vendorcode.add(0, " ");
                    for (com.example.demotry.getVendor.DataItem vendordata : vendorResponse.getData()) {
                        String vendor_no = vendordata.getVendor_no();
                        String vendor_name = vendordata.getVendor_name();
                        sl_vendorcode.add(vendor_no);
                        al_vendor.add(vendor_name);
                        // Log.d("TAG","Vendor"+ vendor_name);
                    }

                    updateVendorSpinner();

                }

            }


            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                Log.d("TAG","Failure"+ t.getMessage());
                Toast.makeText(Payables.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void updateVendorSpinner() {
        vendor_adapter = new ArrayAdapter<String>(Payables.this, android.R.layout.simple_list_item_1, al_vendor);
        vendor_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_vendor.setAdapter(vendor_adapter);
    }

    private void getVendorInvoiceData(final String token, String companycode, String plantcode, final String Vendor_code, final String radioselected) {
        mprogressdialog.setMessage("Fetching...");
        mprogressdialog.show();

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);
        //api/Master/GetVendorInvoiceData
        //{"Company_Code":"1000","Plant_Code":"1101","VendorCode":"600033","Option":"0"}
        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code", companycode);
        productdata.put("Plant_Code", plantcode);
        productdata.put("VendorCode", Vendor_code);
        productdata.put("Option", radioselected);
       /* Log.d("TAG","Vendor Invoice Response "+ Company_code);
        Log.d("TAG","Vendor Invoice Response "+ Plant_Code);
        Log.d("TAG","Vendor Invoice Response "+ Vendor_code);
        Log.d("TAG","Vendor Invoice Response "+ radioselected);
        Log.d("TAG","Vendor Invoice Response "+ token);*/
        Call<VendorInvoiceResponse> vendorcall = jsonPlaceHolderApi.vinvoice(token, productdata);
        vendorcall.enqueue(new Callback<VendorInvoiceResponse>() {
            @Override
            public void onResponse(Call<VendorInvoiceResponse> call, Response<VendorInvoiceResponse> response) {
                Log.d("TAG", "Vendor Invoice Response " + response.body());

                if (response.isSuccessful()) {
                    if (response.body().getFlag().equals("False")) {
                        mprogressdialog.hide();
                        Toast.makeText(Payables.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    } else {
                        mprogressdialog.hide();
                        VendorInvoiceResponse vendorInvoiceResponse = response.body();
                        dataList = vendorInvoiceResponse.getData();

                        Log.d("TAG", "Vendor Invoice Response " + dataList);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) dataList);
                        bundle.putString("token", token);
                        Intent list = new Intent(Payables.this, PayablesList.class);
                        list.putExtras(bundle);
                        startActivity(list);
                    }
                } else {
                    mprogressdialog.hide();
                    Log.d("TAG", "Vendor Invoice Response Failed" + response.code());
                }

            }

            @Override
            public void onFailure(Call<VendorInvoiceResponse> call, Throwable t) {
                mprogressdialog.hide();
                Log.d("TAG", " Failed  " + t.getMessage());
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    public void payablesList() {

        if (sp_company.getSelection() > 0 && sp_plant.getSelection() > 0 && sp_vendor.getSelection() > 0) {

            if (radiogrp.getCheckedRadioButtonId() == -1) {
                radioButton.setError("please select items");
                Toast.makeText(this, "please select radiob buttons", Toast.LENGTH_SHORT).show();
                return;
            } else {
                int select = radiogrp.getCheckedRadioButtonId();
                radioButton = findViewById(select);
                radioselected = radiogrp.indexOfChild(radioButton);

                getVendorInvoiceData(token, companycode, plantcode, Vendor_code, String.valueOf(radioselected));

            }
        } else if (sp_company.getSelectedItem() == null) {
            sp_company.setError("Please select required field");
            sp_company.setHintEnabled(false);
            return;
        } else if (sp_plant.getSelectedItem() == null) {
            sp_plant.setError("Please select required field");
            sp_plant.setHintEnabled(false);
            return;
        } else if (sp_vendor.getSelectedItem() == null) {
            sp_vendor.setError("Please select required field");
            sp_vendor.setHintEnabled(false);
            return;
        }
       /* else{
            spinner1.setError("Please select Company field");
            spinner1.setHintEnabled(false);
            spinner2.setError("Please select Plant");
            spinner2.setHintEnabled(false);
            spinner3.setError("Please select vendor");
            spinner3.setHintEnabled(false);
            return;

        }*/
    }

/*    public void checkButton() {

        int code = radiogrp.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(code);
        Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_black_24dp);
        radioButton.setError("Please Select types of Payables",errorIcon);
        String str = (String) radioButton.getText();
        txt4 =  str;
        Log.d("Radio Button","button click"+ str);

    }*/
}
