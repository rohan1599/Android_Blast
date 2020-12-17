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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demotry.Adapters.PRAdapter;
import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.Helper.MyButtonClickListener;
import com.example.demotry.Helper.SwipeHelper;
import com.example.demotry.getPrmDetails.DataItem;
import com.example.demotry.getSavePRDetails.SavePRDetails;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class prmpoApproval extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText et_search;
    private String token,Company_code,Plant_code,PRType;
    private List<DataItem> prmData;
    private PRAdapter prAdapter;
    final ArrayList<String> rejectItems = new ArrayList<>();
    final ArrayList<String> acceptedItems = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prmpo_approval);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        et_search = findViewById(R.id.et_prsearch);
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
            PRType = bundle.getString("PRType");
            prmData = (List<DataItem>) bundle.getSerializable("data");

        }

        SharedPreferences mPrefs = getSharedPreferences("PRDetails", 0);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("token", token);
        editor.putString("Company_code", Company_code);
        editor.putString("Plant_code", Plant_code);
        editor.putString("PRType", PRType);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(prmpoApproval.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        prAdapter = new PRAdapter(prmData, getApplicationContext());
        prAdapter.setPRData(prmData);
        prAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(prAdapter);


        SwipeHelper swipeHelper = new SwipeHelper(this, recyclerView, 200) {
            @Override
            public void instantiateMyButton(final RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(prmpoApproval.this, "Reject", 20, R.drawable.ic_block_black_24dp, Color.parseColor("#E80704"),
                        new MyButtonClickListener() {

                            @Override
                            public void onClick(final int pos) {
                                //  final int position = pos;
                                //
                                //new AlertDialog.Builder(viewHolder.itemView.getContext())
                                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(prmpoApproval.this);
                                builder.setMessage("Are you sure you want to Reject ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // Your action
                                       String Doc_No = prmData.get(pos).getDoc_no();
                                       String Doc_Item_No = prmData.get(pos).getDOC_ITEM_NO();
                                        // int position = viewHolder.getAdapterPosition();
                                        String flag = "2";

                                        Toast.makeText(prmpoApproval.this, "Delete", Toast.LENGTH_SHORT).show();
                                        getSavePR(token, Company_code, Plant_code, PRType, Doc_No, Doc_Item_No,flag);
                                        prAdapter.removeItem(pos);
                                        prAdapter.notifyDataSetChanged();

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

                buffer.add(new MyButton(prmpoApproval.this, "Accept", 20, R.drawable.ic_assignment_turned_in_white_24dp, Color.parseColor("#389e16"),
                        new MyButtonClickListener() {

                            @Override
                            public void onClick(final int pos) {

                                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(prmpoApproval.this);
                                builder.setMessage("Are you sure you want to Accept ?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        String Doc_No = prmData.get(pos).getDoc_no();
                                        String Doc_Item_No =prmData.get(pos).getDOC_ITEM_NO();
                                        String flag = "1";
                                        getSavePR(token, Company_code, Plant_code, PRType, Doc_No,Doc_Item_No, flag);
                                        Log.d("TAG", "Accepted doc no :" + Doc_No);

                                        Toast.makeText(prmpoApproval.this, "Accept", Toast.LENGTH_SHORT).show();

                                        prAdapter.acceptedItem(pos);
                                        prAdapter.notifyDataSetChanged();
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


    private void getSavePR(String token, String Company_code, String Plant_code, String PRType, String Doc_No, String Doc_Item_No,String flag) {
        jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

        HashMap<String, String> poapproaval = new HashMap<>();
        poapproaval.put("Company_Code", Company_code);
        poapproaval.put("Plant_Code", Plant_code);
        poapproaval.put("PRType", PRType);
        poapproaval.put("Doc_No", Doc_No);
        poapproaval.put("Doc_Item_No", Doc_Item_No);
        poapproaval.put("flag", flag);
             /*   Log.d("TAG", "Purchase Response" + Company_code);
                Log.d("TAG", "Purchase Response" + Plant_code);
                Log.d("TAG", "Purchase Response" + POType_code);
                Log.d("TAG", "Purchase Response" + Doc_no);
                Log.d("TAG", "Purchase Response" + AppCancel);*/
        Call<SavePRDetails> savepo = jsonPlaceHolderApi.saveDetails(token, poapproaval);
        savepo.enqueue(new Callback<SavePRDetails>() {
            @Override
            public void onResponse(Call<SavePRDetails> call, Response<SavePRDetails> response) {
                if (response.isSuccessful()) {
                    SavePRDetails savePOResponse = response.body();
                   // List<DataItem> data = savePOResponse.getData();
                    Log.d("TAG", "DataItem of accept and delete" + savePOResponse);
                    // Log.d("TAG", "DataItem of accept and delete" + savePOResponse);
                } else {
                    Log.d("TAG", "failed of Appcanncel" + response.code());
                }
            }


            @Override
            public void onFailure(Call<SavePRDetails> call, Throwable t) {
                Log.d("TAG", "Failed Saveapproal" + t.getMessage());
            }
        });

    }
    private void filter(String text) {
        ArrayList<DataItem> filterdNames = new ArrayList<>();
        for (DataItem s : prmData) {//if the existing elements contains the search input
            if (s.getREQUISITIONAR().toLowerCase().contains(text.toLowerCase()) || s.getDoc_no().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
           /* else{
                s.getDocNo().toLowerCase().contains(text.toLowerCase());
                filterdNames.add(s);
            }*/
        }

        //calling a method of the adapter class and passing the filtered list
        prAdapter.filterList(filterdNames);

    }

@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuhome) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, HomeScreen.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupayables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, Payables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupurchase) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, PurchaseReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menupoapprovals) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, poapproval.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menustock) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, Stock.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menusales) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, SalesReport.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menureceiveables) {
            Bundle bundle = new Bundle();
            bundle.putString("token", token);
            Intent intent = new Intent(prmpoApproval.this, receiveables.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.menulogout) {
            Intent intent = new Intent(prmpoApproval.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
