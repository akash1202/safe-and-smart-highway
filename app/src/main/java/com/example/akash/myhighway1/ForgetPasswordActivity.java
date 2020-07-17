package com.example.akash.myhighway1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {
    Button sendEmail;
    AutoCompleteTextView user;
    String responseOfSendRequest = "";
    int IS_EMAIL = 1, IS_PHONE = 2, flag = 2, count = 0;
    String urlforrequest = getString(R.string.appwebsite) + "/api/forgetpassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar11);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forget Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user=(AutoCompleteTextView)findViewById(R.id.userforgetpassword);
        sendEmail=(Button) findViewById(R.id.sendPasswordButton);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send Email
                count=0;
                if(user.getText().toString().equals("")){
                user.setError("This Field is required!!");
                count++;
                }
                if (!isValidPhone(user.getText().toString().trim())) {
                    user.setError("isn't valid");
                    count++;
                }
                if (isNetworkAvailable(ForgetPasswordActivity.this) && count == 0) {
                    String temp = user.getText().toString().replace(" ", "").trim();
                    if (isValidPhone(temp)) {
                        flag = IS_PHONE;
                        int length = temp.length();
                        temp = length > 10 ? temp.substring((length - 1) - 9, length) : temp;
                    }
                    SendRequest sendRequest = new SendRequest();
                    String params[] = {urlforrequest, "user", temp};
                    sendRequest.execute(params);
                } else if (!isNetworkAvailable(ForgetPasswordActivity.this)) {
                    //show alert
                    Toast.makeText(ForgetPasswordActivity.this, "Network is Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class SendRequest extends AsyncTask<String,String,String> {
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseOfSendRequest="";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(final String... strings) {

            RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest=new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseOfSendRequest=response;
                    if(response.equals("1")) {
                        Toast.makeText(ForgetPasswordActivity.this, "Please Check Your Mail!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                        finish();
                    }
                    else if(response.equals("0")){
                        Toast.makeText(ForgetPasswordActivity.this, "Error in send Email", Toast.LENGTH_LONG).show();
                    }
                    else if(response.equals("3")){
                        Toast.makeText(ForgetPasswordActivity.this, "This Email is not Registered", Toast.LENGTH_LONG).show();
                    }
                    else if(response.equals("2")){
                        Toast.makeText(ForgetPasswordActivity.this, "This Phone number is not Registered", Toast.LENGTH_LONG).show();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<String, String>();
                    for(int i=1;i+1<=strings.length;i+=2)
                        params.put(strings[i],strings[i+1]);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            return null;
        }
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


    public boolean isNetworkAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (manager.getActiveNetworkInfo()!=null&&manager.getActiveNetworkInfo().isConnected());
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
        finish();
        return true;
    }
}
