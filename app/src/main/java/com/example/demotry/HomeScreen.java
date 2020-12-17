package com.example.demotry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    DrawerLayout drawerLayout;
    private String token,USER,PASS;

    public void stock(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, Stock.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void poapproval(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, poapproval.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void receive(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, receiveables.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void payables(View view)
    {
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, Payables.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void sales(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, SalesReport.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void purchase(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("token",token);
        Intent intent = new Intent(this, PurchaseReport.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        navigationView = findViewById(R.id.nv1);
        NavigationMenuView navigationMenu = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenu.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //Menu menu = navigationView.getMenu();
       // menu.addSubMenu(" ").add(" ");

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);




        setTitle("Menu");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        token = bundle.getString("token");

       // Log.d("TAG", "Token Renew: " + token);
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

}

