package com.example.demotry;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demotry.AppConfig.JsonPlaceHolderApi;
import com.example.demotry.AppConfig.RetrofitClient;
import com.example.demotry.loginResponse.DataItem;
import com.example.demotry.loginResponse.LoginResponse;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    EditText edt_username, edt_pass;
    Button button;
    String _username;
    String _password;
    private String prename, prepass;
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String Name = "nameKey";
    public static final String Pass = "passKey";
    TextInputLayout email, pass;

    public void doLogin() {

        boolean isValid = true;
        if (edt_username.getText().toString().isEmpty()) {
            email.setError("Field can't be empty");
            isValid = false;
        } else {
            email.setErrorEnabled(false);
        }


        if (edt_pass.getText().toString().isEmpty()) {
            pass.setError("Field can't be empty");
            isValid = false;
        } else if (edt_pass.getText().toString().length() > 15) {
            pass.setError("password too long");
            isValid = false;
        } else {
            pass.setErrorEnabled(false);
        }

        if (isValid) {
            _username = edt_username.getText().toString();
            _password = edt_pass.getText().toString();


            jsonPlaceHolderApi = RetrofitClient.getRetrofit().create(JsonPlaceHolderApi.class);

            HashMap<String, String> loginParam = new HashMap<>();
            loginParam.put("USER", _username);
            loginParam.put("PASS", _password);
            Call<LoginResponse> call = jsonPlaceHolderApi.login(loginParam);

            call.enqueue(new Callback<LoginResponse>() {


                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        //Log.d("TAG","Messsage "+ loginResponse);

                        if (loginResponse.getFlag().equals("True")) {
                            String flag = loginResponse.getFlag();
                            String msg = loginResponse.getMsg();

                            Log.d("TAG", "Measssage" + flag);
                            Log.d("TAG", "Measssage" + msg);

                            String token = loginResponse.getToken();
                            DataItem userData = loginResponse.getData().get(0);
                            saveData();
                            Bundle bundle = new Bundle();
                            bundle.putString("token", token);
                            bundle.putString("USER", _username);
                            bundle.putString("PASS", _password);
                            Intent intent = new Intent(MainActivity.this, HomeScreen.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "" + loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(MainActivity.this, "Error : " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("TAG", "Measssage",t);
                    Toast.makeText(MainActivity.this, "Server Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


    }

    private void saveData() {
        SharedPreferences mpreference = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreference.edit();
        editor.putString(Name, _username);
        editor.putString(Pass, _password);
        editor.apply();
        //Toast.makeText(MainActivity.this,"Thanks",Toast.LENGTH_LONG).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        prename = sharedPreferences.getString(Name, "");
        prepass = sharedPreferences.getString(Pass, "");
    }

    private void updateViews() {
        edt_username.setText(prename);
        edt_pass.setText(prepass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  textViewResult = findViewById(R.id.tv1);
        edt_username = findViewById(R.id.et1);
        edt_username.requestFocus();
        edt_username.setCursorVisible(true);
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        edt_pass = findViewById(R.id.et2);
        button = findViewById(R.id.button);
        email = findViewById(R.id.text_input_email);
        pass = findViewById(R.id.tl_materialgrp_error);

       /* edt_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {

                }
                else
                {
                    //Toast.makeText(MainActivity.this,"out of focus",Toast.LENGTH_SHORT).show();
                }
            }
        });

       text1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                    text2.requestFocus();
                    return false;

            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        loadData();
        updateViews();

    }


}

