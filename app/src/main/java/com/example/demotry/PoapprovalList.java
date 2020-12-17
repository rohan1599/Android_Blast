package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demotry.Adapters.PoApprovalAdapter;
import com.example.demotry.Helper.MyButtonClickListener;
import com.example.demotry.Helper.SwipeHelper;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.getPurchaseData.PurchaseData;
import com.example.demotry.getSavePOResponse.DataItem;
import com.example.demotry.getSavePOResponse.SavePOResponse;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoapprovalList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String Company_code;
    private String Plant_code;
    private String POType_code;
    private String Doc_No;
    private String AppCancel;
    RecyclerView recyclerView;
    private EditText et_search;
    private List<PurchaseData> purchaseData;
    private PoApprovalAdapter poApprovalAdapter;
    final ArrayList<String> rejectItems = new ArrayList<>();
    final ArrayList<String> acceptedItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poapproval_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        et_search = findViewById(R.id.et_search);
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            token = bundle.getString("token");
            Company_code = bundle.getString("Company_Code");
            Plant_code = bundle.getString("Plant_Code");
            POType_code = bundle.getString("POType");
            purchaseData = (List<PurchaseData>) bundle.getSerializable("data");

        }

        SharedPreferences mPrefs = getSharedPreferences("orderDetails", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("token", token);
        editor.putString("Company_code", Company_code);
        editor.putString("Plant_code", Plant_code);
        editor.putString("POType", POType_code);
// give key value as "sound" you mentioned and value what you // to want give as "1" in you mentioned
        editor.apply();

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

    }


    private void init() {
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PoapprovalList.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        poApprovalAdapter = new PoApprovalAdapter(purchaseData, getApplicationContext());
        poApprovalAdapter.setPurchaseData(purchaseData);
        poApprovalAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(poApprovalAdapter);


        SwipeHelper swipeHelper = new SwipeHelper(this, recyclerView, 200) {
            @Override
            public void instantiateMyButton(final RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(PoapprovalList.this, "Reject", 20, R.drawable.ic_block_black_24dp, Color.parseColor("#E80704"),
                        new MyButtonClickListener() {

                            @Override
                            public void onClick(final int pos) {
                              //  final int position = pos;
                                //
                                //new AlertDialog.Builder(viewHolder.itemView.getContext())
                                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(PoapprovalList.this);
                                builder.setMessage("Are you sure you want to Reject ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Your action
                                        Doc_No = purchaseData.get(pos).getDocNo();
                                       // int position = viewHolder.getAdapterPosition();
                                        AppCancel = "2";

                                        Toast.makeText(PoapprovalList.this, "Delete", Toast.LENGTH_SHORT).show();
                                        getSavePoApproval(token, Company_code, Plant_code, POType_code, Doc_No, AppCancel);
                                        poApprovalAdapter.removeItem(pos);
                                        poApprovalAdapter.notifyDataSetChanged();

                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                    };
                                });

                                android.app.AlertDialog dialog = builder.create();
                                dialog.show();


                            }
                        }));

                buffer.add(new MyButton(PoapprovalList.this, "Accept", 20, R.drawable.ic_assignment_turned_in_white_24dp, Color.parseColor("#389e16"),
                        new MyButtonClickListener() {

                            @Override
                            public void onClick(final int pos) {

                                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(PoapprovalList.this);
                                builder.setMessage("Are you sure you want to Accept ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        Doc_No = purchaseData.get(pos).getDocNo();
                                        AppCancel = "1";
                                        getSavePoApproval(token, Company_code, Plant_code, POType_code, Doc_No, AppCancel);
                                        Log.d("TAG", "Accepted doc no :" + Doc_No);

                                        Toast.makeText(PoapprovalList.this, "Accept", Toast.LENGTH_SHORT).show();

                                        poApprovalAdapter.acceptedItem(pos);
                                        poApprovalAdapter.notifyDataSetChanged();
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                    };
                                });

                                android.app.AlertDialog dialog = builder.create();
                                dialog.show();


                            }
                        }));

            }
        };

    }



    private void getSavePoApproval(String token, String Company_code, String Plant_code, String POType_code, String Doc_No, String AppCancel) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> poapproaval = new HashMap<>();
        poapproaval.put("Company_Code", Company_code);
        poapproaval.put("Plant_Code", Plant_code);
        poapproaval.put("POType", POType_code);
        poapproaval.put("Doc_No", Doc_No);
        poapproaval.put("AppCancel", AppCancel);
             /*   Log.d("TAG", "Purchase Response" + Company_code);
                Log.d("TAG", "Purchase Response" + Plant_code);
                Log.d("TAG", "Purchase Response" + POType_code);
                Log.d("TAG", "Purchase Response" + Doc_no);
                Log.d("TAG", "Purchase Response" + AppCancel);*/
        Call<SavePOResponse> savepo = jsonPlaceHolderApi.savedata(token, poapproaval);
        savepo.enqueue(new Callback<SavePOResponse>() {
            @Override
            public void onResponse(Call<SavePOResponse> call, Response<SavePOResponse> response) {
                if (response.isSuccessful()) {
                    SavePOResponse savePOResponse = response.body();
                    List<DataItem> data = savePOResponse.getData();
                    Log.d("TAG", "DataItem of accept and delete" + data);
                   // Log.d("TAG", "DataItem of accept and delete" + savePOResponse);
                } else {
                    Log.d("TAG", "failed of Appcanncel" + response.code());
                }
            }


            @Override
            public void onFailure(Call<SavePOResponse> call, Throwable t) {
                Log.d("TAG", "Failed Saveapproal" + t.getMessage());
            }
        });

    }




    private void filter(String text) {
        // final String[] itemNames= new String[purchaseData.size()];
        ArrayList<PurchaseData> filterdNames = new ArrayList<>();
        for (PurchaseData s : purchaseData) {//if the existing elements contains the search input
            if (s.getVendorName().toLowerCase().contains(text.toLowerCase()) || s.getDocNo().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
           /* else{
                s.getDocNo().toLowerCase().contains(text.toLowerCase());
                filterdNames.add(s);
            }*/
        }

        //calling a method of the adapter class and passing the filtered list
        poApprovalAdapter.filterList(filterdNames);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(PoapprovalList.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(PoapprovalList.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }


}
