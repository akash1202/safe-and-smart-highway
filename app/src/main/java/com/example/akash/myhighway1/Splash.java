package com.example.akash.myhighway1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Splash extends AppCompatActivity{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFRENCENAME="AKASHSASH";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.initial_anim);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        //Window window=this.getWindow();
        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(ContextCompat.getColor(Splash.this,R.color.statusbarmatchingcolor1));
        //getActionBar().setDisplayOptions();
        FitWindowsFrameLayout splashView=(FitWindowsFrameLayout) findViewById(R.id.splashView);
        //splashView.setOnClickListener(this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(sharedPreferences.getString("userNamekey","").equals("")){
                    startActivity(new Intent(Splash.this,MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Splash.this, testIt.class));
                    finish();
                }
            }
        },5000);
        splashView.setAnimation(anim);
        splashView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("userNamekey","").equals("")){
                    startActivity(new Intent(Splash.this,MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(Splash.this, testIt.class));
                    finish();
                }

            }
        });
    }
}
