package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dashboard extends AppCompatActivity {
    CircleImageView dashboardimage;
    TextView dashboardFriendsText;
    TextView dashboardEmailText;
    TextView dashboardPhoneText;
    TextView dashboardNameText;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageLoader imageLoader;
    String responseOfSendRequest="";
    String dashboardpimageURL="";
    String id1="",userimage="",username="",password="",dob="",usertype="",email="",primary_phone="",secondary_phone="",deviceid="";
    String register_timestamp="",current_token="",address_street="",address_district="",address_city="",address_state="",address_country="";
    Integer totalfriends=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dashboardimage=(CircleImageView) findViewById(R.id.dashboardpimage);
        dashboardFriendsText=(TextView) findViewById(R.id.dasboardfriendsText);
        dashboardEmailText=(TextView)findViewById(R.id.dashboardEmailText);
        dashboardPhoneText=(TextView)findViewById(R.id.dashboardPhoneText);
        dashboardNameText=(TextView)findViewById(R.id.dashboardNameText);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        totalfriends=sharedPreferences.getInt("friends",0);
        dashboardpimageURL=sharedPreferences.getString("userPhotoURikey","");

        String userName=sharedPreferences.getString("userNamekey","NA");
        String userEmail=sharedPreferences.getString("userEmailkey","NA");
        String userPhone=sharedPreferences.getString("userMobNumberkey","NA");
        dashboardEmailText.setText(userEmail);
        dashboardPhoneText.setText(userPhone);
        dashboardNameText.setText(userName);

        if(userPhone.equals("NA")||userEmail.equals("NA")||responseOfSendRequest.equals("")){
            String urlToRequest="https://myhighway.000webhostapp.com/api/getprofile.php";
            if (userPhone.length() > 10)
                userPhone = userPhone.substring(3, userPhone.length() - 1);
            String user = (userPhone.equals("NA") ? userEmail : userPhone);
            String s[] = {urlToRequest, "user", user};
            sendRequest request=new sendRequest();
            request.execute(s);
        }

        //dashboardimage.setImageURI(dashboardpimageURL);
        dashboardFriendsText.setText(totalfriends+"");
        imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
        imageLoader.displayImage(dashboardpimageURL,dashboardimage);

    }

    public class sendRequest extends AsyncTask<String,String,String> {
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(MainActivity.this);

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

            RequestQueue requestQueue= Volley.newRequestQueue(Dashboard.this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseOfSendRequest=response;
                    try {
                        JSONObject jsonObject=new JSONObject(responseOfSendRequest);
                        id1=jsonObject.getString("id");
                        username=jsonObject.getString("username");
                        userimage=jsonObject.getString("userimage");
                        password=jsonObject.getString("password");
                        dob=jsonObject.getString("dob");
                        usertype=jsonObject.getString("usertype");
                        email=jsonObject.getString("email");
                        primary_phone=jsonObject.getString("primary_phone");
                        secondary_phone=jsonObject.getString("secondary_phone");
                        deviceid=jsonObject.getString("deviceid");
                        register_timestamp=jsonObject.getString("register_timestamp");
                        current_token=jsonObject.getString("current_token");
                        address_street=jsonObject.getString("address_street");
                        address_district=jsonObject.getString("address_district");
                        address_city=jsonObject.getString("address_city");
                        address_state=jsonObject.getString("address_state");
                        address_country=jsonObject.getString("address_country");
                        dashboardNameText.setText(username);
                        dashboardEmailText.setText(email);
                        dashboardPhoneText.setText(primary_phone);

                        User user=new User(Dashboard.this);
                        user.setUserId(id1);
                        user.setUserName(username);
                        user.setUserEmail(email);
                        user.setUserMobNumber(primary_phone);
                        user.setUserDeviceNumber(deviceid);
                        if(!user.getUserPhotoURi().equals(""))
                            user.setUserPhotoURi(userimage);
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    for(int i=1;i+2<=strings.length;i+=2)
                        params.put(strings[i],strings[i+1]);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
           /* showProgressbar(2);
            try {
                Thread.sleep(3*1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
            loader.hide();
            }*/
            return null;
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
