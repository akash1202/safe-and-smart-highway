package com.example.akash.myhighway1.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.akash.myhighway1.R;

public class MyDevices extends AppCompatActivity {

    private String PREFRENCENAME = "AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView MyDeviceUserId, MyDeviceId;
    private String userDeviceNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_devices);
        MyDeviceUserId=(TextView)findViewById(R.id.mydeviceuserid);
        MyDeviceId=(TextView)findViewById(R.id.mydeviceid);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar7);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Device");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        MyDeviceUserId.setText("User Id : "+sharedPreferences.getString("userIdkey",""));
        MyDeviceId.setText(sharedPreferences.getString("userDeviceNumberkey",""));
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
