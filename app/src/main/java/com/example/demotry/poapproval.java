package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
import com.example.demotry.getPOType.POTypeResponse;
import com.example.demotry.getPRdata.PRData;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getPrmDetails.PrmDetails;
import com.example.demotry.getPurchaseData.PurchaseData;
import com.example.demotry.getPurchaseData.PurchaseDataResponse;
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


public class poapproval extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    //to store code
    private List<String> sl_cmycode = new ArrayList<>();
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_po_code= new ArrayList<>();

    private List<String> sl_prmcmycode = new ArrayList<>();
    private List<String> sl_prmplantcode = new ArrayList<>();
    private List<String> sl_prmpo_code= new ArrayList<>();
    //to display value in spinner
    private ArrayList<String> al_company = new ArrayList<>();
    private ArrayList<String> al_plant= new ArrayList<>();
    private ArrayList<String> al_potype= new ArrayList<>();

    private ArrayList<String> al_prmcompany = new ArrayList<>();
    private ArrayList<String> al_prmplant= new ArrayList<>();
    private ArrayList<String> al_prmpotype= new ArrayList<>();
    //array adapters
    private ArrayAdapter company_adapter,plant_adapter,potype_adapter,prcomy_adapter,prplant_adapter,prtype_adapter;
    //spinners
    private MaterialSpinner sp_company,sp_plant,sp_potype,sp_prmcompany,sp_prmplant,sp_prmpotype;
    //textview to store selected values n pass to next activity
    private String companycode,plantcode,POType_code,prmcompanycode,prmplantcode,PRType;
    //shared preference to store code
    private String company_Code,plant_Code,po_Code,prmcompany_Code,prmplant_Code,pr_Code;
    //Button
    private Button btn_poApprovalDetails,btn_poApprovalPrmDetails;
    private ProgressDialog mProgressDialog;
    //
    private List<PurchaseData> purchaseData;
    private List<com.example.demotry.getPrmDetails.DataItem> prmData;
    //shared preferences
    private SharedPreferences poapprovalprefs;
    private SharedPreferences.Editor editor;

    private SharedPreferences poapprovalPRMprefs;
    private SharedPreferences.Editor prmeditor;
    //radio
    private RadioGroup radioGroup;
    private RadioButton poType,prmTym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poapproval);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            token = bundle.getString("token");
        }


        poapprovalprefs = getSharedPreferences("PoapprovalSettings", Context.MODE_PRIVATE);
        editor = poapprovalprefs.edit();

         poapprovalPRMprefs = getSharedPreferences("PoapprovalPRMSettings", Context.MODE_PRIVATE);
         prmeditor = poapprovalPRMprefs.edit();

        getAllSharedPreferences();
        getPRMAllSharedPreferences();

        if(token != null) {
           getcompany(token);
           getprmcompany(token);
        }

        final CardView view1 = (CardView)findViewById(R.id.poTypecontainer);
        final CardView view2 = (CardView)findViewById(R.id.prmTypecontainer);
        radioGroup = (RadioGroup)findViewById(R.id.radiogrp);
        poType = findViewById(R.id.poType);
        prmTym = findViewById(R.id.prmType);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (poType.isChecked() == true) {
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                }else{
                    view2.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.GONE);
                }
            }
        });

        sp_company = findViewById(R.id.sp_company);
        sp_plant = findViewById(R.id.sp_plant);
        sp_potype = findViewById(R.id.sp_potype);
        btn_poApprovalDetails = findViewById(R.id.btn_poApprovalDetails);

        sp_prmcompany = findViewById(R.id.sp_prmcompany);
        sp_prmplant = findViewById(R.id.sp_prmplant);
        sp_prmpotype = findViewById(R.id.sp_prmpotype);
        btn_poApprovalPrmDetails = findViewById(R.id.btn_poApprovalPrmDetails);


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


        btn_poApprovalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                poapprovallist();
            }
        });
        btn_poApprovalPrmDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PRlist();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);

        sp_company.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0)
                {
                    sp_company.setError("Please select required field");
                    sp_company.setHintEnabled(false);
                    return;
                }
                else {
                    sp_company.setError(null);
                    sp_company.setHintEnabled(true);
                    //String id = spinnerArrayList1.get(i);

                    String item = materialSpinner.getSelectedItem().toString();
                   /* final String companycode = sl_cmycode.get(i);
                    long code = materialSpinner.getSelectedItemId();
                    companycode = companycode;*/


                    editor.putInt("company_pos", i).apply();
                    int pos_select = poapprovalprefs.getInt("company_pos", 0);

                    companycode = sl_cmycode.get(pos_select);
                    editor.putString("Company_code", companycode).apply();

                    getplant(token, companycode);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
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

                    editor.putInt("plant_pos", i).apply();
                    int _pos = poapprovalprefs.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(_pos);
                    editor.putString("Plant_code", plantcode).apply();

                    getPoType(token);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });
        sp_potype.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0)
                {
                    sp_potype.setError("Please select required field");
                    sp_potype.setHintEnabled(false);
                    return;
                }
                else
                {
                    sp_potype.setError(null);
                    sp_potype.setHintEnabled(true);
                    String povalue = materialSpinner.getSelectedItem().toString();
                    editor.putInt("PO_pos", i).apply();
                    int _pos = poapprovalprefs.getInt("PO_pos", 0);

                    POType_code = sl_po_code.get(_pos);
                    editor.putString("PO_code", POType_code).apply();
                   // POType_code = sl_po_code.get(i);
                  //  POType_code = pocode;
                    Log.d("TAG","PO-TYPE : "+ POType_code);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });

        sp_prmcompany.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0)
                {
                    sp_prmcompany.setError("Please select required field");
                    sp_prmcompany.setHintEnabled(false);
                    return;
                }
                else {
                    sp_prmcompany.setError(null);
                    sp_prmcompany.setHintEnabled(true);
                    //String id = spinnerArrayList1.get(i);

                    String item = materialSpinner.getSelectedItem().toString();
                   /* final String companycode = sl_cmycode.get(i);
                    long code = materialSpinner.getSelectedItemId();
                    companycode = companycode;*/


                    prmeditor.putInt("company_pos", i).apply();
                    int pos_select = poapprovalPRMprefs.getInt("company_pos", 0);

                    prmcompanycode = sl_prmcmycode.get(pos_select);
                    prmeditor.putString("Company_code", prmcompanycode).apply();

                    getprmplant(token, prmcompanycode);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });
        sp_prmplant.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    sp_prmplant.setError("Please select required field");
                    sp_prmplant.setHintEnabled(false);
                    return;
                } else {
                    sp_prmplant.setError(null);
                    sp_prmplant.setHintEnabled(true);

                    prmeditor.putInt("plant_pos", i).apply();
                    int _pos = poapprovalPRMprefs.getInt("plant_pos", 0);

                    prmplantcode = sl_prmplantcode.get(_pos);
                    prmeditor.putString("Plant_code", prmplantcode).apply();

                    getprmPoType(token);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });
        sp_prmpotype.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if(materialSpinner.getSelection() == 0)
                {
                    sp_prmpotype.setError("Please select required field");
                    sp_prmpotype.setHintEnabled(false);
                    return;
                }
                else
                {
                    sp_prmpotype.setError(null);
                    sp_prmpotype.setHintEnabled(true);
                  String povalue = materialSpinner.getSelectedItem().toString();
               prmeditor.putInt("PR_pos", i).apply();
                int pos_select = poapprovalPRMprefs.getInt("PR_pos", 0);

                PRType = sl_prmpo_code.get(pos_select);
                prmeditor.putString("PR_code", PRType).apply();
                     //PRType = sl_prmpo_code.get(i);

                    Log.d("TAG","PR-TYPE : "+ PRType);

                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });

    }

    private void PRlist() {
        if(sp_prmcompany.getSelection() > 0 && sp_prmplant.getSelection() > 0 ) {

            getPRMData(token,companycode,plantcode,PRType);

        }
        else if(sp_prmcompany.getSelectedItem() == null )
        {
            sp_prmcompany.setError("Please select required field");
            sp_prmcompany.setHintEnabled(false);
            return;
        }
        else if(sp_prmplant.getSelectedItem() == null )
        {
            sp_prmplant.setError("Please select required field");
            sp_prmplant.setHintEnabled(false);
            return;
        }
    }

    private void getAllSharedPreferences() {
        company_Code = poapprovalprefs.getString("Company_code","");
        plant_Code = poapprovalprefs.getString("Plant_code","");
        po_Code = poapprovalprefs.getString("PO_code","");
    }

    private void getcompany(final String token)  {

     jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        Call<CompanyResponse> callcompany = jsonPlaceHolderApi.company(token);
        callcompany.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {
                    CompanyResponse companyResponse = response.body();
                    DataItem companydata = companyResponse.getData().get(0);
                    String flag = companyResponse.getFlag();
                    String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();
                    final String companyCode = company_code;
                    sl_cmycode.add(0," ");
                      sl_cmycode.add(company_code);
                     al_company.add(0,"--SELECT COMPANY--");
                    al_company.add(Name);

                    Log.d("TAG", "onResponse: " + companydata.toString());

                    updateComapnySpinner();

                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(poapproval.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(poapproval.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateComapnySpinner() {
        int company_pos= 1 ;
        if (company_Code.trim().length() > 0) {
            company_pos = poapprovalprefs.getInt("company_pos", 0);
        }
        company_adapter = new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_company);
        company_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_company.setAdapter(company_adapter);
        sp_company.setSelection(company_pos);
    }

    private void getplant(final String token, String companyCode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> CompanyId = new HashMap<>();
        CompanyId.put("Company_Code",companyCode);
        Call<PlantResponse> plant = jsonPlaceHolderApi.plant( token,CompanyId);
        plant.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {

                if (response.isSuccessful()) {
                    PlantResponse plantResponse = response.body();
                    String flag = plantResponse.getFlag();

                    Log.d("TAG", "PlantResponse" + plantResponse.toString());


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
                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(poapproval.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(poapproval.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatePlantSpinner() {
        int plantPos_Click= 0 ;
        if (plant_Code.trim().length() > 0) {
            plantPos_Click = poapprovalprefs.getInt("plant_pos", 0);
        }
        plant_adapter = new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_plant);
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_plant.setAdapter(plant_adapter);
        sp_plant.setSelection(plantPos_Click);
    }

    private void getprmcompany(final String token)  {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        Call<CompanyResponse> callcompany = jsonPlaceHolderApi.company(token);
        callcompany.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {
                    CompanyResponse companyResponse = response.body();
                    DataItem companydata = companyResponse.getData().get(0);
                    String flag = companyResponse.getFlag();
                    String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();
                    final String companyCode = company_code;
                    sl_prmcmycode.add(0," ");
                    sl_prmcmycode.add(company_code);
                    al_prmcompany.add(0,"--SELECT COMPANY--");
                    al_prmcompany.add(Name);

                    Log.d("TAG", "onResponse: " + companydata.toString());

                    updatePrmComapnySpinner();

                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(poapproval.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(poapproval.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updatePrmComapnySpinner() {
        int company_pos= 1 ;
        if (prmcompany_Code.trim().length() > 0) {
            company_pos = poapprovalPRMprefs.getInt("company_pos", 0);
        }
        prcomy_adapter = new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_prmcompany);
        prcomy_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_prmcompany.setAdapter(prcomy_adapter);
        sp_prmcompany.setSelection(company_pos);
    }

    private void getprmplant(final String token, String prmcompanycode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> CompanyId = new HashMap<>();
        CompanyId.put("Company_Code",prmcompanycode);
        Call<PlantResponse> plant = jsonPlaceHolderApi.plant( token,CompanyId);
        plant.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {

                if (response.isSuccessful()) {
                    PlantResponse plantResponse = response.body();
                    String flag = plantResponse.getFlag();

                    Log.d("TAG", "PlantResponse" + plantResponse.toString());


                    al_prmplant.clear();
                    sl_prmplantcode.clear();
                    al_prmplant.add(0, "--SELECT PLANT--");
                    sl_prmplantcode.add(0, " ");

                    for (com.example.demotry.getPlant.DataItem size : plantResponse.getData()) {
                        String plant_code = size.getPlant_code();
                        String plantname = size.getDesc();
                        al_prmplant.add(plantname);
                        sl_prmplantcode.add(plant_code);
                        //System.out.println(size.toString());
                    }

                    updatePrmPlantSpinner();
                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(poapproval.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(poapproval.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatePrmPlantSpinner() {
        int plantPos_Click= 0 ;
        if (prmplant_Code.trim().length() > 0) {
            plantPos_Click = poapprovalPRMprefs.getInt("plant_pos", 0);
        }
        prplant_adapter = new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_prmplant);
        prplant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_prmplant.setAdapter(prplant_adapter);
        sp_prmplant.setSelection(plantPos_Click);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllSharedPreferences();
        getPRMAllSharedPreferences();
    }

    private void getPRMAllSharedPreferences() {
        prmcompany_Code = poapprovalPRMprefs.getString("Company_code","");
        prmplant_Code = poapprovalPRMprefs.getString("Plant_code","");
        pr_Code = poapprovalPRMprefs.getString("PR_code","");
    }

    private void getPoType(String token) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        Call<POTypeResponse> potypecall = jsonPlaceHolderApi.potype(token);
        potypecall.enqueue(new Callback<POTypeResponse>() {
            @Override
            public void onResponse(Call<POTypeResponse> call, Response<POTypeResponse> response) {

                if(response.isSuccessful()) {

                    POTypeResponse potype = response.body();
                    //Log.d("TAG","POTYPR "+ potype.toString());
                    String flag = potype.getFlag();

                    al_potype.clear();
                    sl_po_code.clear();
                    al_potype.add(0,"--SELECT PO-TYPE--");
                    sl_po_code.add(0," ");
                    for (com.example.demotry.getPOType.DataItem podata : potype.getData()) {
                        String potypecode = podata.getDoctypecode();
                        String potypedesc = podata.getDesc();
                        sl_po_code.add(potypecode);
                        al_potype.add(potypedesc);
                    }

                    updatePoTypeSpinner();

                }
                else{
                    Log.d("TAG", "error unknown Raw :" + response.code());
                }

            }

            @Override
            public void onFailure(Call<POTypeResponse> call, Throwable t) {
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });


    }

    private void updatePoTypeSpinner() {
        int poPos_Click= 0 ;
        if (po_Code.trim().length() > 0) {
            poPos_Click = poapprovalprefs.getInt("PO_pos", 0);
        }
        potype_adapter= new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_potype);
        potype_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_potype.setAdapter(potype_adapter);
        sp_potype.setSelection(poPos_Click);
    }

    private void getprmPoType(String token) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        Call<PRData> prcall = jsonPlaceHolderApi.pr_data(token);
        prcall.enqueue(new Callback<PRData>() {
            @Override
            public void onResponse(Call<PRData> call, Response<PRData> response) {

                if(response.isSuccessful()) {

                    PRData prtype = response.body();
                   // Log.d("TAG","POTYPR "+ prtype.toString());
                    String flag = prtype.getFlag();

                    al_prmpotype.clear();
                    sl_prmpo_code.clear();
                    al_prmpotype.add(0,"--SELECT PR-TYPE--");
                    sl_prmpo_code.add(0," ");
                    for (com.example.demotry.getPRdata.DataItem podata : prtype.getData()) {
                        String potypecode = podata.getDoctypecode();
                        String potypedesc = podata.getDesc();
                        sl_prmpo_code.add(potypecode);
                        al_prmpotype.add(potypedesc);
                    }

                    updatePrmPoTypeSpinner();

                }
                else{
                    Log.d("TAG", "error unknown Raw :" + response.code());
                }

            }

            @Override
            public void onFailure(Call<PRData> call, Throwable t) {
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });


    }

    private void updatePrmPoTypeSpinner() {
        int prPos_Click= 0 ;
        if (pr_Code.trim().length() > 0) {
            prPos_Click = poapprovalPRMprefs.getInt("PR_pos", 0);
        }
        prtype_adapter= new ArrayAdapter<String>(poapproval.this, android.R.layout.simple_list_item_1, al_prmpotype);
        prtype_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_prmpotype.setAdapter(prtype_adapter);
        sp_prmpotype.setSelection(prPos_Click);
    }

    private void getPurchaseData(final String token, final String Company_code, final String Plant_code, final String POType_code) {
        mProgressDialog.setMessage("Fetching...");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_code);
        productdata.put("POType",POType_code);
        /*Log.d("TAG","Purchase Response" + Company_code);
        Log.d("TAG","Purchase Response" + Plant_code);
        Log.d("TAG","Purchase Response" + POType_code);*/
        Call<PurchaseDataResponse> pdata = jsonPlaceHolderApi.purchasedata(token,productdata);
        pdata.enqueue(new Callback<PurchaseDataResponse>() {
            @Override
            public void onResponse(Call<PurchaseDataResponse> call, Response<PurchaseDataResponse> response) {
                if(response.isSuccessful())
                {

                    if(response.body().getFlag().equals("False"))
                    {
                        mProgressDialog.hide();
                        Toast.makeText(poapproval.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mProgressDialog.hide();
                        PurchaseDataResponse dataResponse = response.body();
                        purchaseData = dataResponse.getData();
                        Log.d("TAG", "Purchase Response" + purchaseData);
                        Bundle bundle = new Bundle();
                        bundle.putString("token",token);
                        bundle.putString("Company_Code",Company_code);
                        bundle.putString("Plant_Code",Plant_code);
                        bundle.putString("POType",POType_code);
                        bundle.putSerializable("data", (Serializable) purchaseData);
                        Intent list = new Intent(poapproval.this, PoapprovalList.class);
                        list.putExtras(bundle);
                        startActivity(list);
                    }
                }
                else{
                    mProgressDialog.hide();
                    Log.d("TAG","Purchase Response Failed" + response.code());
                }
            }

            @Override
            public void onFailure(Call<PurchaseDataResponse> call, Throwable t) {
                mProgressDialog.hide();
                Log.d("TAG","Failed " + t.getMessage());
            }
        });
    }

    private void getPRMData(final String token, final String Company_code, final String Plant_code, final String PRType) {
        mProgressDialog.setMessage("Fetching...");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> prm = new HashMap<>();
        prm.put("Company_Code",Company_code);
        prm.put("Plant_Code",Plant_code);
        prm.put("PRType",PRType);
        Log.d("TAG","Purchase Response" + Company_code);
        Log.d("TAG","Purchase Response" + Plant_code);
        Log.d("TAG","Purchase Response" + PRType);
        Call<PrmDetails> pdata = jsonPlaceHolderApi.prmDetails(token,prm);
        pdata.enqueue(new Callback<PrmDetails>() {
            @Override
            public void onResponse(Call<PrmDetails> call, Response<PrmDetails> response) {
                if(response.isSuccessful())
                {

                    if(response.body().getFlag().equals("False"))
                    {
                        mProgressDialog.hide();
                        Toast.makeText(poapproval.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mProgressDialog.hide();
                        PrmDetails dataResponse = response.body();
                        prmData = dataResponse.getData();
                        Log.d("TAG", "Purchase Response" + prmData);
                        Bundle bundle = new Bundle();
                        bundle.putString("token",token);
                        bundle.putString("Company_Code",Company_code);
                        bundle.putString("Plant_Code",Plant_code);
                        bundle.putString("PRType",PRType);
                        bundle.putSerializable("data", (Serializable) prmData);
                        Intent list = new Intent(poapproval.this, prmpoApproval.class);
                        list.putExtras(bundle);
                        startActivity(list);
                    }
                }
                else{
                    mProgressDialog.hide();
                    Log.d("TAG","PR Response Failed" + response.code());
                }
            }

            @Override
            public void onFailure(Call<PrmDetails> call, Throwable t) {
                mProgressDialog.hide();
                Log.d("TAG","Failed " + t.getMessage());
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


   public void poapprovallist() {
        if(sp_company.getSelection() > 0 && sp_plant.getSelection() > 0 && sp_potype.getSelection() > 0) {

            getPurchaseData(token,companycode,plantcode,POType_code);

        }
        else if(sp_company.getSelectedItem() == null )
        {
            sp_company.setError("Please select required field");
            sp_company.setHintEnabled(false);
            return;
        }
        else if(sp_plant.getSelectedItem() == null )
        {
            sp_plant.setError("Please select required field");
            sp_plant.setHintEnabled(false);
            return;
        }
        else if(sp_potype.getSelectedItem() == null)
        {
            sp_potype.setError("Please select required field");
            sp_potype.setHintEnabled(false);
            return;
        }
       /* else
        {
            spinner1.setError(null);
            spinner1.setHintEnabled(true);

        }*/
    }
}
