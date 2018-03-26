package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ForwardTo extends AppCompatActivity {
EditText forwardto,forwardtocountrycode;
LinearLayout forwardtoInput;
TextView savednumber;
Button forwardtoButton,resetButton;
SharedPreferences sharedPreferences;
String PREFRENCENAME="AKASHSASH";
SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_to);
        forwardtoInput=(LinearLayout) findViewById(R.id.forwardtoInput);
        forwardto=(EditText) findViewById(R.id.forwardtoid);
        forwardtocountrycode=(EditText) findViewById(R.id.forwardtocountrycode);
        savednumber=(TextView) findViewById(R.id.savednumber);
        forwardtoButton=(Button) findViewById(R.id.forwardtoidok);
        resetButton=(Button) findViewById(R.id.forwardtoResetButton);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar5);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forwardto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(sharedPreferences.getString("forwardTo","").equals("")){
            forwardtoInput.setVisibility(View.VISIBLE);
            savednumber.setVisibility(View.GONE);
            forwardtoButton.setEnabled(true);
            resetButton.setEnabled(false);
        }
        else{
            forwardtoInput.setVisibility(View.GONE);
            savednumber.setText(sharedPreferences.getString("forwardTo",""));
            savednumber.setVisibility(View.VISIBLE);
            forwardtoButton.setEnabled(false);
            resetButton.setEnabled(true);
        }
        forwardtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNumber=forwardto.getText().toString();
                String newCountryCode=forwardtocountrycode.getText().toString();
               String newCompleteNumber = newCountryCode + newNumber;
                        if(isValidPhone(newCompleteNumber)&&newNumber.length()==10) {
                            if(newCompleteNumber.length()>10) {
                                if(!newCompleteNumber.contains("+")&&newCompleteNumber.length()==12)
                                    newCompleteNumber="+"+newCompleteNumber;
                                editor.putString("forwardTo", newCompleteNumber).commit();
                                forwardto.setText("");
                                forwardtocountrycode.setText("");
                                forwardtoInput.setVisibility(View.GONE);
                                forwardtoButton.setEnabled(false);
                                resetButton.setEnabled(true);
                                savednumber.setText(newCompleteNumber);
                                savednumber.setVisibility(View.VISIBLE);
                                Toast.makeText(ForwardTo.this, "set number success!!", Toast.LENGTH_SHORT).show();
                            }
                            else if(newCompleteNumber.length()==10){
                                Toast.makeText(ForwardTo.this, "Country Code is require!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                else{
                    Toast.makeText(ForwardTo.this, "Enter +91###6543210", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("forwardTo","").commit();
                forwardtocountrycode.setText("");
                forwardto.setText("");
                forwardtoInput.setVisibility(View.VISIBLE);
                savednumber.setVisibility(View.GONE);
                forwardtoButton.setEnabled(true);
                resetButton.setEnabled(false);
                Toast.makeText(ForwardTo.this,"reset success!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(Pattern.matches("[+]*[0-9]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;

            }
        }
        else
        {
            check=false;
        }
        return check;
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
