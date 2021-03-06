package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akash.myhighway1.databinding.ActivityDashboardBinding;
import com.example.akash.myhighway1.data.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    CircleImageView dashboardimage;

    private String PREFRENCENAME = "AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageLoader imageLoader;
    String responseOfSendRequest = "";
    String dashboardpimageURL = "";
    String id1 = "", userimage = "", username = "", password = "", dob = "", usertype = "", email = "", primary_phone = "", secondary_phone = "", deviceid = "";
    String register_timestamp = "", current_token = "", address_street = "", address_district = "", address_city = "", address_state = "", address_country = "";
    String dashboardNotification="";
    Integer totalfriends=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        //setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        totalfriends = sharedPreferences.getInt("friends", 0);
        dashboardpimageURL = sharedPreferences.getString("userPhotoURikey", "");
        String userName = sharedPreferences.getString("userNamekey", "NA");
        String userEmail = sharedPreferences.getString("userEmailkey", "NA");
        String userPhone = sharedPreferences.getString("userMobNumberkey", "NA");
        dashboardNotification = "this is to notify you that this is notification to you only! this is notification only";
        binding.dashboardEmailText.setText(userEmail);
        binding.dashboardPhoneText.setText(userPhone);
        binding.dashboardNameText.setText(userName);
        // dashboardNotify.setText(dashboardNotification);
        // dashboardNotify.setSelected(true);
        if (userPhone.equals("NA") || userEmail.equals("NA") || responseOfSendRequest.equals("")) {
            String urlToRequest = getString(R.string.appwebsite) + "/api/getprofile.php";
            if (userPhone.length() > 10)
                userPhone = userPhone.substring(3, userPhone.length() - 1);
            String user = (userPhone.equals("NA") ? userEmail : userPhone);
            String s[] = {urlToRequest, "user", user};
            sendRequest request = new sendRequest();
            request.execute(s);
        }

        //dashboardimage.setImageURI(dashboardpimageURL);
        binding.dasboardfriendsText.setText(totalfriends + "");
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageLoader.displayImage(dashboardpimageURL, binding.dashboardpimage);
    }

    public static String convertToSuffix(long count) {   //for 5.5k 220k
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.1f%c",
                count / Math.pow(1000, exp),
                "kmgtpe".charAt(exp - 1));
    }

    public class sendRequest extends AsyncTask<String, String, String> {
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(LoginActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseOfSendRequest = "";
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

            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseOfSendRequest = response;
                    try {
                        JSONObject jsonObject = new JSONObject(responseOfSendRequest);
                        id1 = jsonObject.getString("id");
                        username = jsonObject.getString("username");
                        userimage = jsonObject.getString("userimage");
                        password = jsonObject.getString("password");
                        dob=jsonObject.getString("dob");
                        usertype=jsonObject.getString("usertype");
                        email=jsonObject.getString("email");
                        primary_phone=jsonObject.getString("primary_phone");
                        secondary_phone = jsonObject.getString("secondary_phone");
                        deviceid = jsonObject.getString("deviceid");
                        register_timestamp = jsonObject.getString("register_timestamp");
                        current_token = jsonObject.getString("current_token");
                        address_street = jsonObject.getString("address_street");
                        address_district = jsonObject.getString("address_district");
                        address_city = jsonObject.getString("address_city");
                        address_state = jsonObject.getString("address_state");
                        address_country = jsonObject.getString("address_country");
                        dashboardNotification = jsonObject.getString("notification");
                        binding.dashboardNameText.setText(username);
                        binding.dashboardEmailText.setText(email);
                        binding.dashboardPhoneText.setText(primary_phone);
//                        dashboardPhoneText.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.add_friend,0);
//                        dashboardPhoneText.setCompoundDrawablePadding(getApplicationContext().getResources().getDimensionPixelOffset(R.dimen.margin_small));
                        if (dashboardNotification.equalsIgnoreCase("no")) {
                            binding.dashboardNotify.setVisibility(View.GONE);
                        } else {
                            binding.dashboardNotify.setVisibility(View.VISIBLE);
                            binding.dashboardNotify.setText(dashboardNotification);
                            binding.dashboardNotify.setSelected(true);
                        }
                        User user = new User(DashboardActivity.this);
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
