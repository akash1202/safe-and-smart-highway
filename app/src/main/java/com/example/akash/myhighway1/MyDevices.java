package com.example.akash.myhighway1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MyDevices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar7);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Device");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
