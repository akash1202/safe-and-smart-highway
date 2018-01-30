package com.example.akash.myhighway1;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Splash extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.initial_anim);
        Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Splash.this,R.color.statusbarmatchingcolor1));
        //getActionBar().setDisplayOptions();

        FitWindowsFrameLayout splashView=(FitWindowsFrameLayout) findViewById(R.id.splashView);
        splashView.setOnClickListener(this);

        splashView.setAnimation(anim);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.splashView)
        startActivity(new Intent(this,verify_number.class));
        finish();
    }

}
