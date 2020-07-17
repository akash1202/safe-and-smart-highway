package com.example.akash.myhighway1.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.example.akash.myhighway1.R;

public class MyVehicles extends AppCompatActivity {


    private String PREFRENCENAME = "AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView MyVehicleUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicle);

        MyVehicleUserId=(TextView)findViewById(R.id.myvehicleuserid);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar6);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar); //first need to set toolbar then you can call only getSupportActionbar
        setTitle("Vehicle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        MyVehicleUserId.setText("User Id : "+sharedPreferences.getString("userIdkey",""));
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
