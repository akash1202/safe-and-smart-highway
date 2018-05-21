package com.example.akash.myhighway1;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Vishal on 1/8/2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    private String PREFRENCENAME="AKASHSASH";
    String requestfrom="",requestTo="",deviceId="",latitude="",longitude="",altitude="",speed="",problemCode="";
    SharedPreferences.Editor editor;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    Context context;
    String urlforrequest1="http://sshs.co.in/new_request_register.php";
    String urlforrequest2="https://myhighway.000webhostapp.com/api/registerproblem.php";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        this.context=context;
        SmsMessage[] messages=null;
        String content="";
        if(bundle!=null){
            Object[] pdus=(Object[]) bundle.get("pdus");
            messages=new SmsMessage[pdus.length];
            for (int i=0;i<messages.length;i++){
                messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                String sender=messages[i].getOriginatingAddress();
                String body=messages[i].getMessageBody().toString();


                sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
                editor=sharedPreferences.edit();
                String rec=sharedPreferences.getString("forwardTo","");

                if(!rec.equals("")){
                    if(isValidPhone(rec)) {
                        sendSMS(rec, body);
                    }
                }

                if(body.contains("SSHS:")) {
                    String values[]=body.split(",");
                    String values1[]=values[0].split(":");

                    requestfrom=sender;
                    requestTo=values1[0].trim();

                    String deviceId1[]=values1[1].split("-");
                    deviceId=deviceId1[1].trim();

                    latitude=values[1].trim();
                    longitude=values[2].trim();
                    altitude=values[3].trim();
                    speed=values[4].trim();
                    problemCode=values[5].trim();
                    if(problemCode.equals("7"))
                        problemCode="3";
                    content+="\rFrom:"+sender+"\nMessage:";
                    content+=body+"\r";
                    Toast.makeText(context,"0:"+deviceId+" 1:"+latitude+" 2:"+longitude+" 3:"+altitude+" 4:"+speed+" 5:"+problemCode,Toast.LENGTH_LONG).show();

                    Log.d("Message:",content);
                    Toast.makeText(context,""+content,Toast.LENGTH_LONG).show();
                    if(sendRequest(urlforrequest1,requestfrom,requestTo,deviceId,latitude,longitude,altitude,speed,problemCode)){
                        Toast.makeText(context,"Request Forwarded Successfully..."+content,Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(context,"There is some error in forwarding..."+content,Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public boolean sendRequest(final String urlforrequest, final String requestfrom, final String requestTo, final String deviceId, final String latitude, final String longitude, final String altitude, final String speed, final String problemCode){

        final Context context1;
        requestQueue= Volley.newRequestQueue(context);
        stringRequest =new StringRequest(Request.Method.POST, urlforrequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(context, ""+response, Toast.LENGTH_LONG).show();
                response=response.replaceAll("\\<.*?>","");
                Toast.makeText(context, "response:"+response, Toast.LENGTH_LONG).show();
                if(response.equals("success")||response.equals("success1")){
                    Toast.makeText(context, "problem request has been recorded!!", Toast.LENGTH_LONG).show();
                }
                else{
                    if(urlforrequest.equals(urlforrequest1)) {
                        sendRequest(urlforrequest2, requestfrom, requestTo, deviceId, latitude, longitude, altitude, speed, problemCode);
                    }
                    if(urlforrequest.equals(urlforrequest2)){
                        sendRequest(urlforrequest1, requestfrom, requestTo, deviceId, latitude, longitude, altitude, speed, problemCode);
                    }
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"make sure internet is working!!!"+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hm=new HashMap<>();
                hm.put("device_mobile_number",requestfrom);
                hm.put("request_to",requestTo);
                hm.put("device_id",deviceId);
                hm.put("altitude",altitude);
                hm.put("location",latitude+","+longitude);
                hm.put("speed",speed);
                hm.put("emergency_code",problemCode);
                return hm;
            }
        };
        requestQueue.add(stringRequest);
        return true;
    }
    private boolean isValidPhone(String phone)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
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

    public void sendSMS(String to,String content){
        if(to.length()>9) {
            PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
            PendingIntent deliveryIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(to, null, content, null, null);
        }
    }
}
