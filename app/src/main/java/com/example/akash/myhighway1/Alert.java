package com.example.akash.myhighway1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akash.myhighway1.Friends.MyContact;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alert extends AppCompatActivity {
    Button SendAlert;
    RadioGroup radioGroupAlert;
    EditText AlertMessage;
    String AlertType="",responseOfSendRequest="",userNumber="";
    String contactlist="",myNumber="";
    Integer totalfriends=0;
    List <MyContact> contactList;
    DatabaseHandler db;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private int TEST=0,EMERGENCY=1,URGENT=2,REQUEST=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        SendAlert=(Button) findViewById(R.id.sendAlert);
        radioGroupAlert=(RadioGroup)findViewById(R.id.radioGroupAlert);
        AlertMessage=(EditText)findViewById(R.id.alertMessage);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        totalfriends=sharedPreferences.getInt("friends",0);
        myNumber=sharedPreferences.getString("userMobNumberkey","");
        db=new DatabaseHandler(Alert.this);
        contactList= db.getAllContacts();
        contactlist=contactList.size()+"";
        for(int i=0;i<contactList.size();i++){
            String temp=contactList.get(i).getNumber().replace(" ","").trim();
            int length=temp.length();
            temp= length>10? temp.substring((length-1)-9,length):temp;
            contactlist+=","+temp;
        }
        radioGroupAlert.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton;
                switch(i){
                case R.id.emergencyrb:
                    radioButton=(RadioButton) findViewById(R.id.emergencyrb);
                    //radioButton.setHighlightColor();
                    AlertType=""+EMERGENCY;
                    break;
                case R.id.urgentrb:
                    radioButton=(RadioButton) findViewById(R.id.urgentrb);
                    AlertType=""+URGENT;
                    break;
                case R.id.requestrb:
                    radioButton=(RadioButton) findViewById(R.id.requestrb);
                    AlertType=""+REQUEST;
                    break;
                case R.id.testrb:
                    radioButton=(RadioButton) findViewById(R.id.testrb);
                    AlertType=""+TEST;
                    break;
                default: break;
            }
            }
        });
        SendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // hideKeyboard();

                SendAlert.setEnabled(false);
                if(!AlertType.equals("")&&totalfriends>0&&!myNumber.equals("")&&isNetworkAvailable(Alert.this)) {
                    String AlertDesc = ","+AlertType + (AlertMessage.getText().toString().equals("")? ",NA":(","+AlertMessage.getText().toString()));
                    User user=new User(Alert.this);
                    String lastLoc=user.getLastLocationLat()+":"+user.getLastLocationLong();
                    AlertDesc+=","+myNumber;
                    AlertDesc+=","+lastLoc;
                    String s1=contactlist+AlertDesc;
                   // Toast.makeText(Alert.this, s1, Toast.LENGTH_SHORT).show();
                    String s[]={getString(R.string.appwebsite)+"/api/sendAlert.php","alert",s1};
                    sendRequest sendrequest=new sendRequest();
                    sendrequest.execute(s);
                }
                else if(AlertType.equals("")){
                    Toast.makeText(Alert.this,"First Select Type of Alert", Toast.LENGTH_SHORT).show();
                    SendAlert.setEnabled(true);
                }
                else if(myNumber.equals("")){
                    Toast.makeText(Alert.this,"Make  Sure You have registered Your Number",Toast.LENGTH_LONG).show();
                    SendAlert.setEnabled(true);
                }
                else if (!isNetworkAvailable(Alert.this)){
                    Toast.makeText(Alert.this,"Make Sure Internet Is working!!!", Toast.LENGTH_SHORT).show();
                    SendAlert.setEnabled(true);
                }

            }
        });

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar9);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alert");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

            RequestQueue requestQueue= Volley.newRequestQueue(Alert.this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseOfSendRequest=response;
                    try {
                        SendAlert.setEnabled(true);
                        JSONObject jsonObject=new JSONObject(responseOfSendRequest);
                        if(jsonObject.getInt("success")==1){
                            hideSoftKeyboard();
                            Toast.makeText(getApplicationContext(),"request Sent Successfully!!",Toast.LENGTH_LONG).show();
                            editor.putString("Alert",AlertType).commit();
                        startActivity(new Intent(Alert.this,ChatActivity.class));
                        finish();
                        }
                        else if (jsonObject.getInt("success")==0){
                            SendAlert.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"Because of some reason we aren't able to send Alert",Toast.LENGTH_LONG).show();
                        }
                        else if(jsonObject.getInt("success")==2){
                            SendAlert.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"sorry!!! May none of your friend using sshs",Toast.LENGTH_LONG).show();
                        }
                        else{
                            SendAlert.setEnabled(true);
                            Toast.makeText(Alert.this,"You may no have any sshs user as Friend",Toast.LENGTH_LONG);
                        }
                    } catch (JSONException e) {
                        SendAlert.setEnabled(true);
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
                    for(int i=1;i+1<=strings.length;i+=2)
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

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (manager.getActiveNetworkInfo()!=null&&manager.getActiveNetworkInfo().isConnected());
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
