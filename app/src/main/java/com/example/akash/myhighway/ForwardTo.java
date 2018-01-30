package com.example.akash.myhighway;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForwardTo extends AppCompatActivity {
EditText forwardto;
Button forwardtoButton,resetButton;
SharedPreferences sharedPreferences;
String PREFRENCENAME="AKASHSASH";
SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_to);
        forwardto=(EditText) findViewById(R.id.forwardtoid);
        forwardtoButton=(Button) findViewById(R.id.forwardtoidok);
        resetButton=(Button) findViewById(R.id.forwardResetButton);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        forwardtoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNumber=forwardto.getText().toString();
                if(newNumber.length()==13) {
                    editor.putString("forwardTo", newNumber).commit();
                    forwardtoButton.setEnabled(false);
                    Toast.makeText(ForwardTo.this, "set number success!!", Toast.LENGTH_SHORT).show();
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
                forwardto.setText("");
                Toast.makeText(ForwardTo.this,"reset success!!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
