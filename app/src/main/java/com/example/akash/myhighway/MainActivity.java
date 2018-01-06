package com.example.akash.myhighway;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    FirebaseAuth fbauth;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN=1;
    private static final int GOOGLE_LOGIN=11;
    private static final int FACEBOOK_LOGIN=12;
    private static final int CUSTOM_LOGIN=13;
    Button registerButton,logincustomButton;
    EditText e1,e2;
    CircleImageView prPhoto;
    SignInButton gmailButton;
    CallbackManager callbackManager;
    LoginButton loginButton;
    MyAsyncTask task=new MyAsyncTask();
    URL profilePicture;
    String UserId,first_name,last_name,email,birthday,gender,s1="",s2="",s3="",s4="";
    private String PREFRENCENAME="AKASHHIGHWAY";
    Intent i1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        registerButton=(Button)findViewById(R.id.registerButton);
        logincustomButton=(Button) findViewById(R.id.loginButton);
        Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.statusbarmatchingcolor1));

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("emailkey","").equals("")||!sharedPreferences.getString("usernamekey","").equals("")||!sharedPreferences.getString("imageURLkey","").equals("")){
            startActivity(new Intent(MainActivity.this,testIt.class));
        }
        task.execute();
        e1=(EditText) findViewById(R.id.userText);
        e2=(EditText) findViewById(R.id.passwordText);
        logincustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s1=e1.getText().toString();
                s2=e2.getText().toString();
                String urlforlogin="https://myhighway.000webhostapp.com/api/login.php";
                RequestQueue rq= Volley.newRequestQueue(getApplicationContext());
                StringRequest sr=new StringRequest(Request.Method.POST, urlforlogin, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray=null;
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            s3=jsonObject.getString("msg");
                            s4=jsonObject.getString("username");
                            Log.e("response",response);
                            Toast.makeText(getApplicationContext(),""
                                    +s3,Toast.LENGTH_SHORT).show();
                            if(s3.equals("Success!!")) {
                                SSHSLogin(s1,s4,getResources().getDrawable(R.drawable.default_profile_image).toString(),CUSTOM_LOGIN);
                                /*i1 = new Intent(getApplicationContext(), testIt.class);
                                i1.putExtra("email",s1);
                                i1.putExtra("userName","user");
                                i1.putExtra("imageURL",getResources().getDrawable(R.drawable.default_profile_image).toString());
                                startActivity(i1);*/
                            }
                        }
                        catch(JSONException ex){
                            Toast.makeText(getApplicationContext(),"Json parsing Exception!",Toast.LENGTH_SHORT).show();
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
                        hm.put("user",e1.getText().toString());
                        hm.put("upassword",e2.getText().toString());
                        return hm;
                    }
                };
                rq.add(sr);
            }});
        gmailButton= (SignInButton) findViewById(R.id.gmailButton);
        fbauth= FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this) //need to add on failed listener....
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });
      FirebaseUser user=fbauth.getCurrentUser();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebookButton);
        loginButton.setReadPermissions("email","public_profile","user_friends","user_birthday");
        // If using in a fragment
        //loginButton.setFragment(MainActivity.this);
        boolean loggedin= AccessToken.getCurrentAccessToken()!=null;
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              String currentToken=  loginResult.getAccessToken().getUserId();
              handleFacebookAccessToken(loginResult.getAccessToken());
                final GraphRequest graphRequest= GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Toast.makeText(MainActivity.this,object+" ",Toast.LENGTH_SHORT).show();
                            //UserId=object.getString("id");
                            //profilePicture=new URL("https://graph.facebook/"+UserId+"/picture?width=500&height=500");
                            //Toast.makeText(MainActivity.this,"url: "+profilePicture,Toast.LENGTH_SHORT).show();
                            //Log.d(" Facebook profile: ",profilePicture.toString());
                            /*if(object.has("first_name")) {
                                first_name = object.getString("first_name");
                            }
                            if(object.has("last_name")) {
                                last_name = object.getString("last_name");
                                Log.d("Name: ",first_name+" "+last_name);
                                Toast.makeText(MainActivity.this,"name: "+first_name+" "+last_name,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("email")) {
                                email = object.getString("email");
                                Log.d("Email: ",email);
                                Toast.makeText(MainActivity.this,"Email: "+email,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("birthday")) {
                                birthday = object.getString("birthday");
                                Log.d("Birthday: ",birthday);
                                Toast.makeText(MainActivity.this,"Birthday: "+birthday,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("gender")) {
                                gender = object.getString("gender");
                                Log.d("Gender: ",gender);
                                Toast.makeText(MainActivity.this,"Gender: "+gender,Toast.LENGTH_SHORT).show();
                            }*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "friendlist");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
              Toast.makeText(getApplicationContext(),"Success facebook!",Toast.LENGTH_SHORT).show();

            }

            public void handleFacebookAccessToken(AccessToken accessToken){
                AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
                fbauth.signInWithCredential(credential).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user=fbauth.getCurrentUser();
                                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS},1);
                                    SSHSLogin(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString(),FACEBOOK_LOGIN);
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"Authentication failed!!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
            }
        });
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

    }

    public void displayuserinfo(JSONObject object){
        String fname,lname,email,id,picture;
        try {
            fname=object.getString("first_name");
            lname=object.getString("last_name");
            email=object.getString("email");
            //picture=object.getString("picture");
            id=object.getString("id");
            Intent intentforLogin=new Intent(getApplicationContext(),testIt.class);
            intentforLogin.putExtra("email",email);
            intentforLogin.putExtra("userName",fname+" "+lname);
            intentforLogin.putExtra("imageURL","abc");
            Toast.makeText(getApplicationContext(),"email:"+email+" name:"+fname+" "+lname,Toast.LENGTH_SHORT).show();
            //startActivity(intentforLogin);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection.....failed",Toast.LENGTH_SHORT).show();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult signInResult= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(signInResult.isSuccess()){
                GoogleSignInAccount account=signInResult.getSignInAccount();
                signInWith(account);
                Toast.makeText(getApplicationContext(),"success result!!!",Toast.LENGTH_SHORT).show();
            }
        }
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    public void signInWith(GoogleSignInAccount account){
        fbauth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(),null))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Signed in...",Toast.LENGTH_LONG).show();
                            //prPhoto.setImageURI(fbauth.getCurrentUser().getPhotoUrl());
                            FirebaseUser user =fbauth.getCurrentUser();
                            MyAsyncTask as=new MyAsyncTask();
                            SSHSLogin(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString(),GOOGLE_LOGIN);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Authentication Failed...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Gps access for Better result!",Toast.LENGTH_SHORT).show();
                (new MyAsyncTask()).execute();
            }
            if(grantResults[1]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Contact access for Better result!",Toast.LENGTH_SHORT).show();
                (new MyAsyncTask()).execute();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void SSHSLogin(String email,String name,String imageURL,int type){
        Intent intentforLogin=new Intent(this,testIt.class);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        switch (type) {
            case GOOGLE_LOGIN:
                intentforLogin.putExtra("email",email);
                intentforLogin.putExtra("userName", name);
                intentforLogin.putExtra("imageURL", imageURL);
                break;
            case FACEBOOK_LOGIN:
                Toast.makeText(this,"you needs to verify your email if null",Toast.LENGTH_SHORT).show();
                intentforLogin.putExtra("email",email);
                intentforLogin.putExtra("userName", name);
                intentforLogin.putExtra("imageURL", imageURL);
                break;
            case CUSTOM_LOGIN:
                intentforLogin.putExtra("email",email);
                intentforLogin.putExtra("userName", name);
                intentforLogin.putExtra("imageURL", imageURL);
                break;
                default:return;
        }
        editor.putString("emailkey",email);
        editor.putString("usernamekey",name);
        editor.putString("imageURLkey",imageURL);
        editor.commit();
        Toast.makeText(this,"Go...",Toast.LENGTH_SHORT).show();
        startActivity(intentforLogin);
       // finish();
    }

    public class MyAsyncTask extends AsyncTask<String,String,String>{
       // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
        protected String doInBackground(String... strings) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS},1);
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
        public void showProgressbar(int seconds){
            /*loader.setClickable(false);
            loader.setIndicatorColor(getResources().getColor(R.color.colorWhatsapp));
            loader.show();*/
        }
    }
}
