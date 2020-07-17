package com.example.akash.myhighway1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutusActivity extends AppCompatActivity {

    Button sendFeedBack;
    TextView policyTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        sendFeedBack = (Button) findViewById(R.id.sendFeedbackButton);
        policyTextview = (TextView) findViewById(R.id.policylinktext);
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
        policyTextview.setMovementMethod(LinkMovementMethod.getInstance());
        policyTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PrivacyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.privacypolicylink)));
                startActivity(PrivacyIntent);
            }
        });
    }

    public void sendFeedbackEmail(){
        Intent SendEmail=new Intent(Intent.ACTION_SEND);
        SendEmail.setType("text/email");
        SendEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{"sashighway@gmail.com"});
        SendEmail.putExtra(Intent.EXTRA_SUBJECT,"FeedBack");
        SendEmail.putExtra(Intent.EXTRA_TEXT,"Dear....");
        startActivity(Intent.createChooser(SendEmail,"Send Feedaback To sshs admin"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
