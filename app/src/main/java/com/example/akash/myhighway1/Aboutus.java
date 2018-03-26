package com.example.akash.myhighway1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Aboutus extends AppCompatActivity {

    Button sendFeedBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar4);
        sendFeedBack=(Button) findViewById(R.id.sendFeedbackButton);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFeedbackEmail();
            }
        });
    }

    public void sendFeedbackEmail(){
        Intent SendEmail=new Intent(Intent.ACTION_SEND);
        SendEmail.setType("text/email");
        SendEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{"sashighway@gmail.com"});
        SendEmail.putExtra(Intent.EXTRA_SUBJECT,"FeedBack");
        SendEmail.putExtra(Intent.EXTRA_TEXT,"Dear....");
        startActivity(Intent.createChooser(SendEmail,"Send Feedaback To SSHS admin"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
