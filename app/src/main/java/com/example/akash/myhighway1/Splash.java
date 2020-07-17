package com.example.akash.myhighway1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFRENCENAME = "AKASHSASH";
    Integer GPS_ENABLE_REQUEST = 121;
    Animation anim;
    FitWindowsFrameLayout splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        anim = AnimationUtils.loadAnimation(this, R.anim.initial_anim);
        sharedPreferences = getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //Window window=this.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(Splash.this,R.color.statusbarmatchingcolor1));
        //getActionBar().setDisplayOptions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        splashView = (FitWindowsFrameLayout) findViewById(R.id.splashView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.getString("userNamekey", "").equals("")) {
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                    finish();
                } else {
                    try {           //to check gps is on and open gps setting
                        int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
                        if(off==0) {
                            Toast.makeText(getApplicationContext(), "Without GPS you can't use this app", Toast.LENGTH_LONG);
                            showGPSDisabledDialog();
                            // Intent GPSOptionIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            // startActivityForResult(GPSOptionIntent,GPS_ENABLE_REQUEST);
                        }
                        //GPS is on
                        else {
                            startActivity(new Intent(Splash.this, MyHighwayActivity.class));
                            finish();
                        }
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, 3000);
        splashView.setAnimation(anim);

    }

    public void showGPSDisabledDialog(){
        Dialog dialog;
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("GPS Disabled");
        builder.setMessage("GPS is Disabled, In order to use this app properly enable GPS");
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),GPS_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog=builder.create();
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GPS_ENABLE_REQUEST){
            try {           //to check gps is on and open gps setting
                int off=Settings.Secure.getInt(getContentResolver(),Settings.Secure.LOCATION_MODE);
                if(off==0) {
                    finish();
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
