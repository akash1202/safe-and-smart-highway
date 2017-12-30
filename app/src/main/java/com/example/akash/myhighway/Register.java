package com.example.akash.myhighway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Button registerButton;
    EditText name,email,dob,deviceid,password,conpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText) findViewById(R.id.fullnameregister);
        email=(EditText) findViewById(R.id.emailregister);
        dob=(EditText) findViewById(R.id.dobregister);
        deviceid=(EditText) findViewById(R.id.deviceidregister);
        password=(EditText) findViewById(R.id.passwordregister);
        conpassword=(EditText) findViewById(R.id.conpasswordregister);
        registerButton=(Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestregister();
            }
        });

    }
    public void requestregister(){
        String urlforlogin="https://myhighway.000webhostapp.com/api/register.php";
        RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
        StringRequest sr=new StringRequest(Request.Method.POST, urlforlogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   String s3=response.toString();
                    Toast.makeText(getApplicationContext(),""
                            +s3,Toast.LENGTH_SHORT).show();
                    if(s3.equals("Registered Successfully!!")) {
                       Intent i1 = new Intent(getApplicationContext(), testIt.class);
                        i1.putExtra("email",email.getText().toString());
                        i1.putExtra("userName",name.getText().toString());
                        i1.putExtra("imageURL","");
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
                hm.put("uname",name.getText().toString());
                hm.put("dob",dob.getText().toString());
                hm.put("uemail",email.getText().toString());
                hm.put("deviceid",deviceid.getText().toString());
                hm.put("upassword",password.getText().toString());
                return hm;
            }
        };
        rq.add(sr);
    }
}
