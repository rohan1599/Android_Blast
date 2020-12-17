package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demotry.Adapters.StockAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getCompany.CompanyResponse;
import com.example.demotry.getCompany.DataItem;
import com.example.demotry.getMaterial.MaterialResponse;
import com.example.demotry.getMaterialGroup.MaterialGroupResponse;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getProductData.ProductDataResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
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

public class Stock extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    List<com.example.demotry.getProductData.DataItem> data;
    //to store codes
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_cmycode = new ArrayList<>();
    private List<String> sl_matcode = new ArrayList<>();
    //stores values
    private ArrayList<String> al_cmyvalue = new ArrayList<>();
    private ArrayList<String> al_plantvalue = new ArrayList<>();
    private ArrayList<String> al_matvalue = new ArrayList<>();
    //adapters
    private ArrayAdapter<String> company_adapter;
    private ArrayAdapter<String> plant_adapter;
    private ArrayAdapter<String> material_adapter;
    //material spinner
    private MaterialSpinner Company_sp, Plant_sp, Material_sp;
    //Editext for material grp
    private TextInputEditText Material_grp_edt;
    private TextInputLayout tl_material_grp_error;
    //store code to pass in nxt method
    private String companycode, plantcode, MatGrpcode, Material_code;
    //filter
    private Button btn_showStockDetails;
    //material grp response
    private MaterialGroupResponse group;
    //progress bar
    private ProgressDialog mProgressDialog;
    //shared preferences
    private SharedPreferences Stockprefs;
    private SharedPreferences.Editor editor;
    //shared preferences to store value of plant n cmy
    private String company_Code, plant_Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        setTitle("Stock");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //from login page we retrieve login token
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            token = bundle.getString("token");
        }

        Stockprefs = getSharedPreferences("StockSettings", Context.MODE_PRIVATE);
        editor = Stockprefs.edit();


        getAllSharedPreferences();

        Company_sp = findViewById(R.id.sp_company);
        Plant_sp = findViewById(R.id.sp_plant);
        Material_sp = findViewById(R.id.sp_material);
        tl_material_grp_error = findViewById(R.id.tl_materialgrp_error);
        Material_grp_edt = findViewById(R.id.edt_material_grp);
        btn_showStockDetails = findViewById(R.id.btn_stockDetails);

        if (token != null) {
            getcompany(token);
        }


        // materialSpinner = findViewById(R.id.spinner_btn);
        btn_showStockDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatespinner();
            }
        });
        // getData();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);

       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mProgressDialog.dismiss();
            }
        }, 2000);*/

        Company_sp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {

                if (materialSpinner.getSelection() == 0) {

                    Company_sp.setError("Please select item ");
                    Company_sp.setHintEnabled(false);
                    return;
                } else {
                    Company_sp.setError(null);
                    Company_sp.setHintEnabled(true);
                    //String id = spinnerArrayList1.get(i);

                    String item = materialSpinner.getSelectedItem().toString();

                    editor.putInt("company_pos", i).apply();
                    int pos_select = Stockprefs.getInt("company_pos", 0);

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

        Plant_sp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    Plant_sp.setError("Please select plant item");
                    Plant_sp.setHintEnabled(false);
                    return;
                } else {
                    Plant_sp.setError(null);
                    Plant_sp.setHintEnabled(true);
                    String plantselected = materialSpinner.getSelectedItem().toString();
                    long plantpos = materialSpinner.getSelectedItemId();

                    editor.putInt("plant_pos", i).apply();
                    int _pos = Stockprefs.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(_pos);
                    editor.putString("Plant_code", plantcode).apply();

                   // Log.d("TAG", "Plant string" + plantselected);
                   // Log.d("TAG", "Plant Code" + plantcode);

                    getMaterialgroup(token, companycode, plantcode);


                }
            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });

        Material_sp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                String material = materialSpinner.getSelectedItem().toString();
                long code = materialSpinner.getSelectedItemId();
                Material_code = sl_matcode.get(i);

                //Log.d("TAG", "Material code" + Material_code);

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG","Nothing Selected");
            }
        });


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
                    String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();

                    sl_cmycode.add(0, " ");
                    sl_cmycode.add(company_code);
                    al_cmyvalue.add(0, "--SELECT COMPANY--");
                    al_cmyvalue.add(Name);
                   // Log.d("TAG", "onResponse: " + companydata.toString());

                    updateComapnySpinner();


                } else {
                    Log.d("TAG", "error unknown Raw :" + response.code());

                    //Toast.makeText(Stock.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Log.d("TAG", "Server Error : Unknown" + t.getMessage());

            }

        });

    }

    private void updateComapnySpinner() {
        int pos_Click = 1;
        if (company_Code.trim().length() > 0) {
            pos_Click = Stockprefs.getInt("company_pos", 0);
        }

        company_adapter = new ArrayAdapter<String>(Stock.this, android.R.layout.simple_spinner_dropdown_item, al_cmyvalue);
        company_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Company_sp.setAdapter(company_adapter);
        Company_sp.setSelection(pos_Click);

    }


    private void getAllSharedPreferences() {
        company_Code = Stockprefs.getString("Company_code", "");
        plant_Code = Stockprefs.getString("Plant_code", "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllSharedPreferences();
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

                    // Log.d("TAG","PlantResponse"+ plantResponse.toString());
                    al_plantvalue.clear();
                    al_plantvalue.add(0, "--SELECT PLANT--");
                    sl_plantcode.add(0, " ");
                    for (com.example.demotry.getPlant.DataItem size : plantResponse.getData()) {
                        String plant_code = size.getPlant_code();
                        String plantname = size.getDesc();
                        al_plantvalue.add(plantname);
                        sl_plantcode.add(plant_code);
                        System.out.println(size.toString());
                    }

                    updatePlantSpinner();


                } else {
                    Log.d("TAG", "error unknown Raw :" + response.code());

                }

            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(Stock.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatePlantSpinner() {
        int plantPos_Click = 0;
        if (plant_Code.trim().length() > 0) {
            plantPos_Click = Stockprefs.getInt("plant_pos", 0);
        }

        plant_adapter = new ArrayAdapter<String>(Stock.this, android.R.layout.simple_list_item_1, al_plantvalue);
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Plant_sp.setAdapter(plant_adapter);
        Plant_sp.setSelection(plantPos_Click);
    }

    private void getMaterialgroup(final String token, final String companycode, final String plantcode) {


        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> Response = new HashMap<>();
        Response.put("Company_Code", companycode);
        Response.put("Plant_Code", plantcode);

        Call<MaterialGroupResponse> groupResponseCall = jsonPlaceHolderApi.groupResponse(token, Response);
        groupResponseCall.enqueue(new Callback<MaterialGroupResponse>() {
            @Override
            public void onResponse(Call<MaterialGroupResponse> call, final retrofit2.Response<MaterialGroupResponse> response) {

                group = response.body();

                Material_grp_edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Company_sp != null && Plant_sp != null) {
                            popup();
                        }

                    }
                });


            }

            @Override
            public void onFailure(Call<MaterialGroupResponse> call, Throwable t) {
                Toast.makeText(Stock.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });

    }

    private void popup() {
        int _arraysize = 0;
        _arraysize = group.getData().size() + 1;

        final String[] itemNames = new String[_arraysize];
        final String[] itemCode = new String[_arraysize];

        itemNames[0] = "Select Material Group ";
        itemCode[0] = " ";
        for (int i = 0; i < group.getData().size(); i++) {
            itemNames[i + 1] = group.getData().get(i).getDesc();
            itemCode[i + 1] = group.getData().get(i).getMatgroup_code();

        }


        final boolean[] itemsChecked = new boolean[_arraysize];
        final ArrayList<String> selected_item = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(Stock.this);
        builder.setTitle("--SELECT MATERIAL GROUP--");
        builder.setMultiChoiceItems(itemNames, itemsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if (isChecked) {
                    if (!selected_item.contains((String) String.valueOf(which))) {
                        selected_item.add(String.valueOf(which));
                        itemsChecked[which] = true;
                    }
                } else if (selected_item.contains((String) String.valueOf(which))) {
                    selected_item.remove((String) String.valueOf(which));
                    itemsChecked[which] = false;
                }
            }
        });


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                List<String> selectedStrings = new ArrayList<>();
                List<String> selectedId = new ArrayList<>();

                for (int j = 0; j < selected_item.size(); j++) {
                    if (Integer.parseInt(selected_item.get(j)) == 0) {
                        Material_grp_edt.setText(" " + itemNames[Integer.parseInt(selected_item.get(0))]);
                    } else {
                        selectedStrings.add(itemNames[Integer.parseInt(selected_item.get(j))]);
                        selectedId.add(itemCode[Integer.parseInt(selected_item.get(j))]);


                    }

                }
                Material_grp_edt.setText(selectedStrings.toString().substring(1).replaceFirst("]", ""));
                MatGrpcode = selectedId.toString().substring(1).replaceFirst("]", "");
                getMaterial(token, companycode, plantcode, MatGrpcode);
               // Log.d("TAG", "SElected TEXT ID :" + MatGrpcode);


            }

        });



        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        ListView listView = dialog.getListView();
        listView.setFooterDividersEnabled(false);
        listView.addFooterView(new View(this));
        //listView.setFooterDividersEnabled(true);
        //listView.setDivider(new ColorDrawable(Color.LTGRAY)); // set color
        //listView.setDividerHeight(2);

        dialog.show();



    }

    private void getMaterial(final String token, final String companycode, final String plantcode, final String MatGrpcode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> material = new HashMap<>();
        material.put("Company_Code", companycode);
        material.put("Plant_Code", plantcode);
        material.put("Material_Group", MatGrpcode);
        Log.d("TAG", "GRoup" + companycode);
        Log.d("TAG", "GRoup" + plantcode);
        Log.d("TAG", "GRoup" + MatGrpcode);
        Call<MaterialResponse> materialresponse = jsonPlaceHolderApi.MaterialResponse(token, material);
        materialresponse.enqueue(new Callback<MaterialResponse>() {
            @Override
            public void onResponse(final Call<MaterialResponse> call, retrofit2.Response<MaterialResponse> response) {

                if (response.isSuccessful()) {
                    final MaterialResponse material = response.body();
                    String flag = material.getFlag();
                   // Log.d("TAG", "Material" + material);

                    al_matvalue.clear();
                    sl_matcode.clear();
                   al_matvalue.add(0, "--SELECT MATERIAL --");
                   sl_matcode.add(0, " ");
                    for (com.example.demotry.getMaterial.DataItem materialdata : material.getData()) {
                        String matcode = materialdata.getMat_code();
                        String matdesc = materialdata.getDesc();
                        sl_matcode.add(matcode);
                        al_matvalue.add(matdesc);
                    }

                  updateMaterialSpinner();

                }
            }

            @Override
            public void onFailure(Call<MaterialResponse> call, Throwable t) {
                Toast.makeText(Stock.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });


    }

    private void updateMaterialSpinner() {
        material_adapter = new ArrayAdapter<>(Stock.this, android.R.layout.simple_list_item_1, al_matvalue);
        material_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        Material_sp.setAdapter(material_adapter);

    }

    private void getproductdata(final String token, String companycode, String Plant_code, String MatGrpcode, String Material_code) {

        mProgressDialog.setMessage("Fetching...");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code", companycode);
        productdata.put("Plant_Code", Plant_code);
        productdata.put("Material_Group", MatGrpcode);
        productdata.put("Mat_Code",Material_code);
        /*Log.d("TAG","ProductData"+ companycode );
        Log.d("TAG","ProductData"+ Plant_code );
        Log.d("TAG","ProductData"+ MatGrpcode );
        Log.d("TAG","ProductData"+ Material_code );*/
        Call<ProductDataResponse> product = jsonPlaceHolderApi.product(token, productdata);
        product.enqueue(new Callback<ProductDataResponse>() {
            @Override
            public void onResponse(Call<ProductDataResponse> call, Response<ProductDataResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    if (response.body().getFlag().equals("False")) {
                        mProgressDialog.hide();
                        Toast.makeText(Stock.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.hide();
                        data = response.body().getData();

                        Log.d("TAG", "msg Dataitem" + data);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) data);
                        bundle.putString("token", token);

                        Intent intent = new Intent(Stock.this, StockList.class);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }


                } else {
                    mProgressDialog.hide();
                    Log.d("TAG", "error unknown Raw :" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ProductDataResponse> call, Throwable t) {
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
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(Stock.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(Stock.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }


    private void validatespinner() {

        if (Company_sp.getSelection() == 0) {
            Company_sp.setError("Please Select required field");
            Company_sp.setHintEnabled(false);
            return;
        } else if (Plant_sp.getSelection() == 0) {
            Plant_sp.setError("Please Select required field");
            Plant_sp.setHintEnabled(false);
            return;
        } else if (Company_sp.getSelection() > 0 && Plant_sp.getSelection() > 0) {

            Company_sp.setError(null);
            Company_sp.setHintEnabled(true);
            Plant_sp.setError(null);
            Plant_sp.setHintEnabled(true);

            getproductdata(token, companycode, plantcode, MatGrpcode, Material_code);

        }
        /* else {
            spinner1.setError("Please fill required field");
            spinner1.setHintEnabled(false);
            spinner2.setError("Please fill required field");
            spinner2.setHintEnabled(false);
            return;//Toast.makeText(Stock.this,"Please fill all required fields",Toast.LENGTH_SHORT).show();
        }*/
    }
}
