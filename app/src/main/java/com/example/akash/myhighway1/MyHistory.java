package com.example.akash.myhighway1;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MyHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CustomAdapter3 customAdapter;
    String responseOfSendRequest="";
    String urlforrequest="http://sshs.co.in/service/get_user_data.php";
    int method= Request.Method.POST;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar8);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.preyclerview);
        linearLayoutManager= new LinearLayoutManager(MyHistory.this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        customAdapter=null;
        User user=new User(MyHistory.this);
        String temp=user.getUserMobNumber().trim();
        int length=temp.length();
        temp= length>10? temp.substring((length-1)-9,length):temp;
        temp="91"+temp;
        SendRequest sendRequest=new SendRequest(getApplicationContext(),recyclerView,"recycler");
        String s[]={urlforrequest,"mobile_number",temp};
        sendRequest.execute(s);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchwidget,menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView) menu.findItem(R.id.searchButton).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filter Recyclerview when textquery submitted
                customAdapter.getFilter().filter(query);
                recyclerView.scrollToPosition(View.SCROLL_INDICATOR_START);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //refresh Recyclerview when textquery change
                customAdapter.getFilter().filter(newText);
                recyclerView.scrollToPosition(View.SCROLL_INDICATOR_START);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.searchButton){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class SendRequest extends AsyncTask<String,String,String> {
        Context context;
        RecyclerView recyclerView;
        String flag;
        public SendRequest(Context context,RecyclerView recyclerView,String flag){
            this.context=context;
            this.recyclerView=recyclerView;
            this.flag=flag;
        }
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

            RequestQueue requestQueue= Volley.newRequestQueue(context);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    responseOfSendRequest=response;
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    Log.d("response:",response);
                    if(flag.equals("recycler")) {
                        try {
                            Log.d("response history:",response);
                            JSONArray obj1 = new JSONArray(response);
                            /*String problemcode[] = new String[obj1.length()];
                            String location[] = new String[obj1.length()];
                            String speed[] = new String[obj1.length()];
                            String mobilenumber[] = new String[obj1.length()];
                            String time[] = new String[obj1.length()];
                            String status[] = new String[obj1.length()];*/
                            List<Problem> problems=new ArrayList<Problem>();
                            for(int i=0;i<obj1.length();i++) {
                                JSONObject obj2 = obj1.getJSONObject(i);
/*                                  problemcode[i] = obj2.getString("emergency_code");
                                    location[i] = obj2.getString("location");
                                    speed[i] = obj2.getString("speed");
                                    mobilenumber[i] = obj2.getString("device_mobile_number");
                                    time[i] = obj2.getString("time");
                                    status[i] = obj2.getString("status");*/
                                    Problem tempProblem=new Problem();
                                tempProblem.setProblemcode(Integer.parseInt(obj2.getString("emergency_code")));
                                tempProblem.setProblem_loc1(obj2.getString("location"));
                                tempProblem.setSpeed(obj2.getString("speed"));
                                tempProblem.setDevice_number(obj2.getString("device_mobile_number"));
                                tempProblem.setTime1(obj2.getString("time"));
                                tempProblem.setStatus(obj2.getString("status"));
                                problems.add(tempProblem);
                            }
                            customAdapter = new CustomAdapter3(MyHistory.this,getFragmentManager(),problems);
                            recyclerView.setAdapter(customAdapter);
                        } catch (Exception e) {
                            Toast.makeText(context, "Exception:" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                    else{
                        /*startActivity(new Intent(listActivity.this,HomeActivity.class));
                        finish();*/
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,""+error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }){
               /* @Override
                public Map<String, String> getHeaders() throws AuthFailureError {        //here Header is created
                    HashMap<String,String> params=new Hashtable<String, String>();
                    params.put("Content-Type","application/json;charset=UTF-8");
                    return params;
                }
                @Override
                public String getPostBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    return super.getParams();
                }*/


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> params=new HashMap<String,String>();
                        for(int i=1;i+1<strings.length;i++){
                            params.put(strings[i],strings[i+1]);
                        }
                        return params;
                }

            };
            requestQueue.add(stringRequest);
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
