package com.example.akash.myhighway1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

public class MyVehicle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar6);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar); //first need to set toolbar then you can call only getSupportActionbar
        getSupportActionBar().setTitle("Vehicle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
