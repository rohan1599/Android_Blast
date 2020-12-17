package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getCompany.CompanyResponse;
import com.example.demotry.getCompany.DataItem;
import com.example.demotry.getCustomer.CustomerResponse;
import com.example.demotry.getPlant.PlantResponse;
import com.example.demotry.getSOType.SOTypeResponse;
import com.example.demotry.getSalesInvoiceData.InvoiceData;
import com.example.demotry.getSalesInvoiceData.SalesInvoiceDataResponse;
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

public class SalesReport extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    //to store code
    private List<String> sl_cmycode = new ArrayList<>();
    private List<String> sl_plantcode = new ArrayList<>();
    private List<String> sl_so_code= new ArrayList<>();
    private List<String> sl_customercode= new ArrayList<>();
    //to display value in spinner
    private ArrayList<String> al_company = new ArrayList<>();
    private ArrayList<String> al_plant= new ArrayList<>();
    private ArrayList<String> al_sotype= new ArrayList<>();
    private ArrayList<String> al_customer= new ArrayList<>();
    //array adapters
    private ArrayAdapter company_adapter,plant_adapter,so_adapter;
    //spinners
    private MaterialSpinner sp_company,sp_plant,sp_vendor,sp_customer;
    //textview to store selected values n pass to next activity
    private String companyCode,plantcode,SOtype_code,ToDate,FromDate,Cust_code;
    private TextInputLayout tl1,tl2;
    List<InvoiceData> data ;
    private ImageView displayDate;
    private DatePickerDialog.OnDateSetListener aDateListener;
    private TextInputEditText edt_frmDate, edt_toDate;
    private ProgressDialog mProgressDialog;
    //shared preferences
    private SharedPreferences Salesprefs;
    private SharedPreferences.Editor editor;
    //shared prference storage
    private String company_Code,plant_Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        sp_company = findViewById(R.id.sp_cmy);
        sp_plant = findViewById(R.id.sp_salesplant);
        sp_vendor = findViewById(R.id.sp_vendor);
        sp_customer = findViewById(R.id.sp_customer);
        tl1 = findViewById(R.id.tl_fromdate);
        tl2 = findViewById(R.id.tl_todate);
        Button  btn_salesDetails = (Button)findViewById(R.id.btn_salesDetails);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            token = bundle.getString("token");
        }

        Salesprefs = getSharedPreferences("SalesSettings", Context.MODE_PRIVATE);
        editor = Salesprefs.edit();


        getAllSharedPreferences();

        if(token != null) {
           getcompany(token);
        }

        edt_frmDate =  findViewById(R.id.edt_ly1);
        edt_toDate = findViewById(R.id.edt_ly2);


        mDisplayDate = findViewById(R.id.Todate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Calendar cal = Calendar.getInstance();
                                                int year = cal.get(Calendar.YEAR);
                                                int month = cal.get(Calendar.MONTH);
                                                int day = cal.get(Calendar.DAY_OF_MONTH);
                                                DatePickerDialog dialog = new DatePickerDialog(SalesReport.this,android.R.style.Theme_Holo_Light,mDateSetListener,year,month,day);
                                               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog.show();
                                            }
                                        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                tl2.setError(null);
                tl2.setHintEnabled(true);
                edt_toDate.setText(date);
                ToDate = date;


            }
        };



        displayDate = findViewById(R.id.fromdate);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int y = calendar.get(Calendar.YEAR);
                int m = calendar.get(Calendar.MONTH);
                int d = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dlg = new DatePickerDialog(SalesReport.this,android.R.style.Theme_Holo_Light,aDateListener,y,m,d);
                dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlg.show();
            }
        });


        aDateListener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                tl1.setError(null);
                tl1.setHintEnabled(true);
                edt_frmDate.setText(date);
                FromDate = date;

            }
        };


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

        btn_salesDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesList();
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
                }
                else
                {
                    sp_company.setError(null);
                    sp_company.setHintEnabled(true);
                    String item = materialSpinner.getSelectedItem().toString();
                    // String companyCode = spinnerCmpCode.get(i);



                    editor.putInt("company_pos", i).apply();
                    int pos_select = Salesprefs.getInt("company_pos", 0);

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
                if(materialSpinner.getSelection() == 0)
                {
                    sp_plant.setError("Please select required field");
                    sp_plant.setHintEnabled(false);
                    return;
                }
                else
                {
                    sp_plant.setError(null);
                    sp_plant.setHintEnabled(true);
                    String plantselected = materialSpinner.getSelectedItem().toString();

                    editor.putInt("plant_pos", i).apply();
                    int _pos = Salesprefs.getInt("plant_pos", 0);

                    plantcode = sl_plantcode.get(_pos);
                    editor.putString("Plant_code", plantcode).apply();


                        getSOType(token, companyCode, plantcode);


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

                    String sovalue = materialSpinner.getSelectedItem().toString();
                    SOtype_code= sl_so_code.get(i);


                    getCustomerData(token, companyCode,plantcode);
                   // Log.d("So type",""+socode);


            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {

            }
        });
        sp_customer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NotNull MaterialSpinner materialSpinner, @Nullable View view, int i, long l) {

                    String custvalue = materialSpinner.getSelectedItem().toString();
                    Cust_code= sl_customercode.get(i);
                   // Log.d("Customer value","Customer"+ Cust_code);

            }

            @Override
            public void onNothingSelected(@NotNull MaterialSpinner materialSpinner) {
                Log.d("TAG", "onNothingSelected: ");
            }
        });

    }

    private void getAllSharedPreferences() {
        company_Code = Salesprefs.getString("Company_code", "");
        plant_Code = Salesprefs.getString("Plant_code", "");

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
                    mProgressDialog.dismiss();
                    CompanyResponse companyResponse = response.body();
                    DataItem companydata = companyResponse.getData().get(0);
                    String flag = companyResponse.getFlag();
                    final String company_code = companydata.getCompany_code();
                    String Name = companydata.getCompany_name();


                    sl_cmycode.add(0," ");
                    sl_cmycode.add(company_code);
                    al_company.add(0,"--SELECT COMPANY--");
                    al_company.add(Name);

                   // Log.d("TAG", "onResponse: " + companydata.toString());
                    updateComapnySpinner();
                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                    Toast.makeText(SalesReport.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Toast.makeText(SalesReport.this, "Server Error : Unknown", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateComapnySpinner() {
        int company_pos= 1 ;
        if (company_Code.trim().length() > 0) {
            company_pos = Salesprefs.getInt("company_pos", 0);
        }
        company_adapter = new ArrayAdapter<String>(SalesReport.this, android.R.layout.simple_spinner_dropdown_item, al_company);
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

                  //  Log.d("TAG","PlantResponse"+ plantResponse.toString());

                    al_plant.clear();
                    sl_plantcode.clear();
                    al_plant.add(0,"--SELECT PLANT--");
                    sl_plantcode.add(0," ");
                    for(com.example.demotry.getPlant.DataItem size: plantResponse.getData()) {
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
                    Toast.makeText(SalesReport.this, "Server Error unknown Raw: Code = " + response.raw().body(), Toast.LENGTH_SHORT).show();
                }

            }



            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Toast.makeText(SalesReport.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });
    }

    private void updatePlantSpinner() {
        int plantPos_Click = 0 ;
        if (plant_Code.trim().length() > 0) {
            plantPos_Click = Salesprefs.getInt("plant_pos", 0);
        }
        plant_adapter = new ArrayAdapter<String>(SalesReport.this, android.R.layout.simple_spinner_dropdown_item, al_plant);
        plant_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_plant.setAdapter(plant_adapter);
        sp_plant.setSelection(plantPos_Click);
    }

    private void getSOType(final String token, String companyCode, final String plantcode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> sotype= new HashMap<>();
        sotype.put("Company_Code",companyCode);
        sotype.put("Plant_Code",plantcode);
        Call<SOTypeResponse> vendorcall = jsonPlaceHolderApi.sotype(token,sotype);
        vendorcall.enqueue(new Callback<SOTypeResponse>() {
            @Override
            public void onResponse(Call<SOTypeResponse> call, Response<SOTypeResponse> response) {

                if(response.isSuccessful()){

                    SOTypeResponse soTypeResponse = response.body();
                    String flag = soTypeResponse.getFlag();

                    al_sotype.clear();
                    sl_so_code.clear();
                    al_sotype.add(0,"--SELECT SO TYPE--");
                    sl_so_code.add(0," ");
                    for(com.example.demotry.getSOType.DataItem sotypedata:soTypeResponse.getData())
                    {
                        String sodoc= sotypedata.getDoctypecode();
                        String sodesc = sotypedata.getDesc();
                        sl_so_code.add(sodoc);
                        al_sotype.add(sodesc);
                    }

                    updateSoTypeSpinner();

                }
                else
                {
                    Log.d("TAG", "Unsuccessfull Response :" + response.code());
                }

            }


            @Override
            public void onFailure(Call<SOTypeResponse> call, Throwable t) {
                Toast.makeText(SalesReport.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }


        } );
    }

    private void updateSoTypeSpinner() {
        so_adapter =new ArrayAdapter<String>(SalesReport.this, android.R.layout.simple_spinner_dropdown_item, al_sotype);
        so_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_vendor.setAdapter(so_adapter);
    }

    private void getCustomerData(String token, String companyCode, String plantcode) {

        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> Cust= new HashMap<>();
        Cust.put("Company_Code",companyCode);
        Cust.put("Plant_Code",plantcode);
        Call<CustomerResponse> customer = jsonPlaceHolderApi.customer(token,Cust);
        customer.enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                if(response.isSuccessful())
                {
                    CustomerResponse customerResponse = response.body();
                    String flag = customerResponse.getFlag();

                    al_customer.clear();
                    sl_customercode.clear();
                    sl_customercode.add(0," ");
                    al_customer.add(0,"--SELECT CUSTOMER--");
                    for(com.example.demotry.getCustomer.DataItem cust : customerResponse.getData()) {
                        String customer_code = cust.getCustomer_no();
                        String customer_name = cust.getCustname();
                        al_customer.add(customer_name);
                        sl_customercode.add(customer_code);
                        // System.out.println(size.toString());
                    }

                    updateCustomerSpinner();

                }

                else
                {
                    Log.d("TAG", "error unknown Raw :" + response.code());
                   }
            }


            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                Toast.makeText(SalesReport.this, "Server Error unknown Failure: Code = " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "error Failure Response :" + t.getMessage());
            }
        });

    }

    private void updateCustomerSpinner() {
        so_adapter = new ArrayAdapter<String>(SalesReport.this, android.R.layout.simple_spinner_dropdown_item, al_customer);
        so_adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sp_customer.setAdapter(so_adapter);
    }

    private void getSalesInvoiceData(final String token, final String companyCode, final String plantcode, final String SOtype_code, final String Cust_code, final String FromDate, final String ToDate) {

        mProgressDialog.setMessage("Fetching....");
        mProgressDialog.show();
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> productdata = new HashMap<>();
        productdata.put("Company_Code",companyCode);
        productdata.put("Plant_Code",plantcode);
        productdata.put("SOType",SOtype_code);
        productdata.put("CUSTOMER_NO",Cust_code);
        productdata.put("FromDate",FromDate);
        productdata.put("ToDate",ToDate);
        Log.d("TAG","Message " + companyCode);
        Log.d("TAG","Message " + plantcode);
        Log.d("TAG","Message " + SOtype_code);
        Log.d("TAG","Message " + Cust_code);
        Log.d("TAG","Message " + FromDate);
        Log.d("TAG","Message " + ToDate);
        Call<SalesInvoiceDataResponse> saleinvoice = jsonPlaceHolderApi.sinvoice(token,productdata);
        saleinvoice.enqueue(new Callback<SalesInvoiceDataResponse>() {
            @Override
            public void onResponse(Call<SalesInvoiceDataResponse> call, Response<SalesInvoiceDataResponse> response) {
                if(response.isSuccessful())
                {
                  if(response.body().getFlag().equals("False"))
                    {
                        mProgressDialog.hide();
                        Toast.makeText(SalesReport.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mProgressDialog.hide();
                        SalesInvoiceDataResponse saleinvoiceData = response.body();
                        Log.d("TAG", "SalesInvoice Data " + saleinvoiceData);
                        data = saleinvoiceData.getData();
                        Log.d("TAG", "SalesInvoice Data" + data);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", (Serializable) data);
                        bundle.putString("token", token);
                        Intent list = new Intent(SalesReport.this, SalesList.class);
                        list.putExtras(bundle);
                        startActivity(list);
                    }
                }
                else
                {
                    mProgressDialog.hide();
                    Log.d("TAG","Failed"+ response.code());
                }
            }

            @Override
            public void onFailure(Call<SalesInvoiceDataResponse> call, Throwable t) {
                mProgressDialog.hide();
                Log.d("TAG","Server Failed"+ t.getMessage());
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

    public void SalesList() {
        if(sp_company.getSelection() > 0  && sp_plant.getSelection() > 0 && ToDate!=null && FromDate!=null) {

            getSalesInvoiceData(token, companyCode,plantcode,SOtype_code,Cust_code,FromDate,ToDate);

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

       /* else
        {
            spinner1.setError("Please Select required field");
            spinner1.setHintEnabled(false);
            spinner2.setError("Please Select required field");
            spinner2.setHintEnabled(false);
            tl1.setError("Please select Date");
            tl1.setHintEnabled(false);
            tl2.setError("Please select Date");
            tl2.setHintEnabled(false);

        }*/

    }
}
