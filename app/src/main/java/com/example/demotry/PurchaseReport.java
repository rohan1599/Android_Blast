package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getCompany.CompanyResponse;
import com.example.demotry.getCompany.DataItem;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getPurchaseInvoiceData.InvoiceData;
import com.example.demotry.getPurchaseInvoiceData.PurchaseInvoiceDataResponse;
import com.example.demotry.getVendor.VendorResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tiper.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseReport extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    DrawerLayout drawerLayout;

    private ImageView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextInputEditText edt_frmDate,edt_toDate;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    //to store code
    private List<String> sl_cmycode = new ArrayList<>();
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_vendorcode= new ArrayList<>();
    private List<String> sl_productcode = new ArrayList<>();
    //to display value in spinner
    private ArrayList<String> al_cmy = new ArrayList<>();
    private ArrayList<String> al_plant = new ArrayList<>();
    private ArrayList<String> spinnerVendorValue= new ArrayList<>();
    private ArrayList<String> spinnerProdValue= new ArrayList<>();
    //array adapters
    private ArrayAdapter cmy_adapter,plant_adapter,vend_adapter,adapter4;
    //spinners
    private MaterialSpinner company_sp,plant_sp,spinner4;
    //textview to store selected values n pass to next activity
    private String companyCode,plantcode,Vendor_code,FromDate,ToDate;
    //Vendor dialog onclick
    private TextInputEditText editText;
    private TextInputLayout ed_error,tl1,tl2;
    //vendor response
    private  VendorResponse vendorResponse;
    private Button btnshowreport;
    private ImageView displayDate;
    private DatePickerDialog.OnDateSetListener aDateListener;
    private List<InvoiceData> invoiceData;
    //
    private String selectedvalue,selectedcode;
    private ProgressDialog mProgressDialog;
    //
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    //share prefs storage
    private String company_Code,plant_Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_report);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            token = bundle.getString("token");
        }

        if(token != null) {
           getcompany(token);
        }

        preferences = getSharedPreferences("PurchaseSettings", Context.MODE_PRIVATE);
        editor = preferences.edit();


        getAllSharedPreferences();
        tl1 = findViewById(R.id.tl1);
        tl2 = findViewById(R.id.tl2);

        edt_frmDate = findViewById(R.id.edt_frmdate);
        mDisplayDate = findViewById(R.id.imageView1);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(PurchaseReport.this,android.R.style.Theme_Holo_Light,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                edt_frmDate.setText(date);
                FromDate = date;
                tl1.setError(null);
                tl1.setHintEnabled(true);

            }
        };


        edt_toDate = findViewById(R.id.edt_todate);
        displayDate = findViewById(R.id.image2);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dlg = new DatePickerDialog(PurchaseReport.this,android.R.style.Theme_Holo_Light,aDateListener,y,m,d);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg.show();
            }
        });

        aDateListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                edt_toDate.setText(date);
                ToDate = date;
                tl2.setError(null);
                tl2.setHintEnabled(true);
            }
        };


       company_sp = findViewById(R.id.sp_company);
        plant_sp = findViewById(R.id.sp_plant);
        editText = findViewById(R.id.edt_material_grp);
        btnshowreport = findViewById(R.id.btn_payableDetails);
        ed_error = findViewById(R.id.sp_vendor);
      //   spinner4 = findViewById(R.id.spinner3);

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

        btnshowreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseList();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);


        company_sp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {
                if (materialSpinner.getSelection() == 0) {
                    company_sp.setError("Please select required field");
                    company_sp.setHintEnabled(false);
                    return;
                } else {
                    company_sp.setError(null);
                    company_sp.setHintEnabled(true);

                    String item = materialSpinner.getSelectedItem().toString();

                    editor.putInt("Pcompany_pos", i).apply();
                    int pos_select = preferences.getInt("Pcompany_pos", 0);

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
        plant_sp.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {

                    editor.putInt("plant_pos", i).apply();
                    int _pos = preferences.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(_pos);
                    editor.putString("Plant_code", plantcode).apply();
                    getVendor(token, companyCode, plantcode);

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "Nothing Selected" );
            }
        });
    }

    private void getAllSharedPreferences() {
        company_Code = preferences.getString("Company_code","");
        plant_Code = preferences.getString("Plant_code","");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllSharedPreferences();
    }

    private void getcompany(final String token) {
      //  Log.d("TAG", "getcompanyToken: " + token);

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);


        Call<CompanyResponse> callcompany = jsonPlaceHolderApi.company(token);
        callcompany.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                if (response.isSuccessful()) {

                    CompanyResponse companyResponse = response.body();
                    DataItem companydata = companyResponse.getData().get(0);
                  //  String flag = companyResponse.getFlag();
                    String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();
                    final String companyCode = company_code;

                    sl_cmycode.add(0," ");
                    sl_cmycode.add(company_code);
                    al_cmy.add(0,"--SELECT COMPANY--");
                    al_cmy.add(Name);

                   Log.d("TAG", "onResponse: " + companydata.toString());
                   // Log.d("TAG", "onResponse: Company_Code : " + companydata.getCompany_code());
                  //  Log.d("TAG", "onResponse: Company_Name : " + companydata.getCompany_name());
                  //  Log.d("TAG", "flag :" + flag);


                   // String s = response.body().toString();
                 //   Log.d("TAG", "Response :" + s);
                    updatedCompSpinner();


                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(PurchaseReport.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(PurchaseReport.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updatedCompSpinner() {
        int cmy_pos = 1;
        if(company_Code.trim().length() > 0)
        {
            cmy_pos = preferences.getInt("Pcompany_pos",0);
        }
        cmy_adapter = new ArrayAdapter<String>(PurchaseReport.this, android.R.layout.simple_spinner_dropdown_item, al_cmy);
        cmy_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        company_sp.setAdapter(cmy_adapter);
        company_sp.setSelection(cmy_pos);
    }

    private void getplant(final String token, final String companyCode) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> CompanyId = new HashMap<>();
        CompanyId.put("Company_Code",companyCode);
        Call<PlantResponse> plant = jsonPlaceHolderApi.plant(token,CompanyId);
        plant.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                if (response.isSuccessful()) {
                    PlantResponse plantResponse = response.body();
                    String flag = plantResponse.getFlag();

                   // Log.d("TAG","PlantResponse"+ plantResponse.toString());

                    al_plant .clear();
                    sl_plantcode.clear();
                    al_plant .add(0,"--SELECT PLANT--");
                    sl_plantcode.add(0," ");
                    for(com.example.demotry.getPlant.DataItem size: plantResponse.getData()) {
                        String plant_code = size.getPlant_code();
                        String plantname = size.getDesc();
                        al_plant .add(plantname);
                        sl_plantcode.add(plant_code);
                        //System.out.println(size.toString());
                    }

                    updatedplantSpinner();


                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                     Toast.makeText(PurchaseReport.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(PurchaseReport.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatedplantSpinner() {
        int plantpos = 0;
        if(plant_Code.trim().length() > 0){
            plantpos = preferences.getInt("plant_pos",0);
        }
        plant_adapter = new ArrayAdapter<String>(PurchaseReport.this, android.R.layout.simple_spinner_dropdown_item, al_plant );
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        plant_sp.setAdapter(plant_adapter);
        plant_sp.setSelection(plantpos);
    }

    private void getVendor(String token, String companyCode, String plantcode) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> vend = new HashMap<>();
        vend.put("Company_Code", companyCode);
        vend.put("Plant_Code", plantcode);
        Call<VendorResponse> vendorcall = jsonPlaceHolderApi.vendor(token, vend);
        vendorcall.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                if (response.isSuccessful()) {
                   vendorResponse = response.body();
                    String flag = vendorResponse.getFlag();
                    Log.d("TAG","vendor"+ vendorResponse);

                    editText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(company_sp.getSelectedItem()!=null && plant_sp.getSelectedItem()!=null) {
                                popup();
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                Toast.makeText(PurchaseReport.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void popup() {
        int _arraysize = 0;
        _arraysize = vendorResponse.getData().size()+1;
       final String[] itemNames = new String[_arraysize];
        final String[] itemCode = new String[_arraysize];

        itemNames[0] = "Select Vendor";
        itemCode[0] = " ";
        for (int i = 0; i < vendorResponse.getData().size(); i++) {

            itemNames[i+1] = vendorResponse.getData().get(i).getVendor_name();
            itemCode[i+1] = vendorResponse.getData().get(i).getVendor_no();

        }

        final boolean[] itemsChecked = new boolean[_arraysize];
        final ArrayList<String> selected_item = new ArrayList<String>();


        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseReport.this);
        builder.setTitle("--SELECT VENDOR--");
        builder.setSingleChoiceItems(itemNames, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedvalue = itemNames[which];
                        selectedcode = itemCode[which];
                        Vendor_code = selectedcode;
                        Log.d("TAG","selected txt3" + Vendor_code);
                        Log.d("TAG","selected which" + which);
                        Log.d("TAG","selected Rdio" + selectedcode);
                        //Toast.makeText(PurchaseReport.this,"you selected "+ selectedvalue,Toast.LENGTH_SHORT).show();
                    }

                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Tag","selected"+ i);
                        if(selectedvalue == null) {
                            selectedvalue = itemNames[0];
                            editText.setText("" + selectedvalue);
                            ed_error.setError(null);
                        }
                        else
                        {
                            editText.setText("" + selectedvalue);
                            ed_error.setError(null);
                        }
                    }

               });


        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        ListView listView=dialog.getListView();
        listView.setFooterDividersEnabled(false);
        listView.addFooterView(new View(this));
        //listView.setFooterDividersEnabled(true);
        //listView.setDivider(new ColorDrawable(Color.LTGRAY)); // set color
        //listView.setDividerHeight(2);

        dialog.show();
        dialog.getWindow().setLayout(1000, 1000);
    }

    private void getPurchaseInvoceData(final String token, final String Company_code, final String Plant_code, final String Vendor_code, String FromDate, String ToDate) {
        mProgressDialog.setMessage("Fetching...");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",Company_code);
        productdata.put("Plant_Code",Plant_code);
        productdata.put("VendorCode",Vendor_code);
        productdata.put("FromDate",FromDate);
        productdata.put("ToDate",ToDate);
        Call<PurchaseInvoiceDataResponse> pchinvoice  = jsonPlaceHolderApi.pchinvoice(token,productdata);
        pchinvoice.enqueue(new Callback<PurchaseInvoiceDataResponse>() {
            @Override
            public void onResponse(Call<PurchaseInvoiceDataResponse> call, Response<PurchaseInvoiceDataResponse> response) {

                if(response.isSuccessful()) {
                    if (response.body().getFlag().equals("False")) {
                        mProgressDialog.hide();
                        Toast.makeText(PurchaseReport.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    } else {
                        mProgressDialog.hide();
                        PurchaseInvoiceDataResponse dataResponse = response.body();
                        invoiceData = dataResponse.getData();
                        Log.d("TAG", "Purchase Invoice Data : " + invoiceData);

                        Bundle bundle = new Bundle();
                        bundle.putString("token", token);
                        bundle.putSerializable("data", (Serializable) invoiceData);
                        Intent list = new Intent(PurchaseReport.this, PurchaseReportList.class);
                        list.putExtras(bundle);
                        startActivity(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<PurchaseInvoiceDataResponse> call, Throwable t) {
                mProgressDialog.hide();
                Log.d("TAG","Failure : "+ t.getMessage());
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

    public void purchaseList() {
        if(company_sp.getSelection() > 0 && FromDate!= null && ToDate!=null) {
            getPurchaseInvoceData(token,companyCode,plantcode,Vendor_code,FromDate,ToDate);
           /* Bundle bundle = new Bundle();
            bundle.putString("token",token);
            bundle.putString("Company_code", Company_code);
            bundle.putString("Plant_code", Plant_code);
            bundle.putString("date1", FromDate);
            bundle.putString("date2", ToDate);
            bundle.putString("Vendor_code", Vendor_code);
            Intent list = new Intent(PurchaseReport.this, PurchaseReportList.class);
            list.putExtras(bundle);
            startActivity(list);*/
        }
        else if(company_sp.getSelectedItem() == null){
            company_sp.setError("Please select required field");
            company_sp.setHintEnabled(false);
            return;
        }
        else if(FromDate == null){
            tl1.setError("Please select From Date");
            tl1.setHintEnabled(false);
            return;
        }
        else if(ToDate == null){
            tl2.setError("Please select To Date");
            tl2.setHintEnabled(false);
            return;
        }
        /*else
        {
            spinner1.setError("Please select required field");
            spinner1.setHintEnabled(false);
            tl1.setError("Please select From Date");
            tl1.setHintEnabled(false);
            tl2.setError("Please select To Date");
            tl2.setHintEnabled(false);


        }*/
    }
}

