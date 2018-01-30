package com.example.akash.myhighway;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button registerButton;
    EditText name,email,deviceid,password,conpassword;
    TextView dob;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    int mDay,mMonth,mYear;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText) findViewById(R.id.fullnameregister);
        email=(EditText) findViewById(R.id.emailregister);
        dob=(TextView) findViewById(R.id.dobregister);
        deviceid=(EditText) findViewById(R.id.deviceidregister);
        password=(EditText) findViewById(R.id.passwordregister);
        conpassword=(EditText) findViewById(R.id.conpasswordregister);
        registerButton=(Button) findViewById(R.id.registerButton);

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);

        final Calendar mCalendar=Calendar.getInstance();
        mYear=mCalendar.get(Calendar.YEAR);
        mMonth=mCalendar.get(Calendar.MONTH);
        mDay=mCalendar.get(Calendar.DAY_OF_MONTH);
        String myFormat="dd/MM/yyyy";
        final SimpleDateFormat simpleDateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog=new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        Calendar temp=Calendar.getInstance();
                        temp.set(Calendar.YEAR,year);
                        temp.set(Calendar.MONTH,monthOfYear);
                        temp.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        dob.setText(simpleDateFormat.format(temp.getTime()));
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000*60*60*24);
                datePickerDialog.show();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestregister();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void requestregister(){
        int count=0;
        if(name.getText().toString().equals("")){ name.setError("username can't be Empty");count++;}
        if(email.getText().toString().equals("")){ email.setError("Email Address can't be Empty");count++;}
        if(dob.getText().toString().equals("")){ dob.setError("Birthdate can't be Empty");count++;}
        if(deviceid.getText().toString().equals("")){ deviceid.setError("DeviceId can't be Empty");count++;}
        if(password.getText().toString().equals("")){ password.setError("Password can't be Empty");count++;}
        if(!conpassword.getText().toString().equals(password.getText().toString())){ conpassword.setError("Both password must be match");count++;}
        if(count>0){
            return;
        }
        final ProgressDialog process=new ProgressDialog(Register.this);
        process.setMessage("Wait...");
        process.setCancelable(false);
        process.show();
        String urlforlogin="https://myhighway.000webhostapp.com/api/register.php";
        RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
        StringRequest sr=new StringRequest(Request.Method.POST, urlforlogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    process.cancel();
                   String s3=response.toString();
                    Toast.makeText(getApplicationContext(),""
                            +s3,Toast.LENGTH_SHORT).show();
                    if(s3.equals("Registered Successfully!!")) {
                       Intent i1 = new Intent(getApplicationContext(), testIt.class);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("userEmailkey",email.getText().toString().trim().toLowerCase());
                        editor.putString("userNamekey",name.getText().toString().trim().toLowerCase());
                        editor.putString("userPhotoURikey","");
                        editor.commit();
                        startActivity(i1);
                    }
                }
                catch(Exception ex){
                    Toast.makeText(getApplicationContext(),"Sorry can't register!",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Something gone Wrong!!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hm=new HashMap<String, String>();
                hm.put("uname",name.getText().toString().trim().toLowerCase());
                hm.put("dob",dob.getText().toString().trim().toLowerCase());
                hm.put("uemail",email.getText().toString().trim().toLowerCase());
                hm.put("deviceid",deviceid.getText().toString().trim().toLowerCase());
                hm.put("upassword",password.getText().toString().trim());
                return hm;
            }
        };
        rq.add(sr);
    }
}
