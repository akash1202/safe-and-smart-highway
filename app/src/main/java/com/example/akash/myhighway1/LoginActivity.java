package com.example.akash.myhighway1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akash.myhighway1.data.model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    FirebaseAuth fbauth;
    GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 1;
    private static final int GOOGLE_LOGIN = 11;
    private static final int FACEBOOK_LOGIN = 12;
    private static final int CUSTOM_LOGIN = 13;
    Button registerButton, logincustomButton;
    TextInputEditText e1, e2;
    TextView forgetPasswordLink;
    CircleImageView prPhoto;
    SignInButton gmailButton;
    CallbackManager callbackManager;
    LoginButton loginButton;
    MyAsyncTask task=new MyAsyncTask();
    private int seconds;
    ProgressDialog waitingDialog;
    ProgressDialog process;
    Snackbar snackbar;
    URL profilePicture;
    String UserId,first_name,last_name,email,birthday,gender,s1="",s2="",s3="",s4="",s5="";
    String urlforlogin = "";
    private String PREFRENCENAME="AKASHSASH";
    Intent i1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        task.execute();
        registerButton=(Button)findViewById(R.id.registerButton);
        logincustomButton=(Button) findViewById(R.id.loginButton);
        forgetPasswordLink=(TextView) findViewById(R.id.forgetpasswordLink);
       /* Window window=this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.colorWhatsapp));*/

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("userEmailkey","").equals("")||!sharedPreferences.getString("userNamekey","").equals("")||!sharedPreferences.getString("userPhotoURikey","").equals("")){
            startActivity(new Intent(LoginActivity.this, MyHighwayActivity.class));
            finish();
        }

        e1 = (TextInputEditText) findViewById(R.id.userText);
        e2 = (TextInputEditText) findViewById(R.id.passwordText);
        logincustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                s1=e1.getText().toString();
                s2 = e2.getText().toString();
                int count = 0;
                if (s1.equals("")) {
                    e1.setError("This Field is required!!");
                    count++;
                }
                if (s2.equals("")) {
                    e2.setError("This Field is required!!");
                    count++;
                }
                if (count == 0 && isNetworkAvailable(LoginActivity.this)) {
                    /*final AVLoadingIndicatorView loader=new AVLoadingIndicatorView(LoginActivity.this);
                    loader.setIndicator("LineScalePulseOutRapidIndicator");
                    loader.setElevation(100);
                    loader.setVisibility(View.VISIBLE);
                    loader.setForegroundGravity(Gravity.CENTER);
                    loader.setIndicatorColor(R.color.colorWhatsapp);
                    loader.setClickable(false);
                    loader.show();*/
                    urlforlogin = getString(R.string.appwebsite) + "/api/login.php";
                    doCustomLogin(urlforlogin, e1.getText().toString().trim().replace(" ", ""), e2.getText().toString());
                }         //end of if
                if (!isNetworkAvailable(LoginActivity.this)) {
                    snackbar = Snackbar.make(view, "Make Sure Internet is Working!!", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(getResources().getColor(R.color.whitecolor));
                    snackbar.setAction("dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.colorWhatsapp));
                    snackbar.setDuration(Snackbar.LENGTH_INDEFINITE).show();
                }
            }});
        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                finish();
            }
        });

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
                startActivity(new Intent(LoginActivity.this, Register.class));
                finish();
            }
        });
        FirebaseUser user=fbauth.getCurrentUser();
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebookButton);
        loginButton.setReadPermissions("email","public_profile"); //"user_friends","user_birthday"
        // If using in a fragment
        //loginButton.setFragment(LoginActivity.this);
        boolean loggedin= AccessToken.getCurrentAccessToken()!=null;
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // String currentToken=  loginResult.getAccessToken().getUserId();
                handleFacebookAccessToken(loginResult.getAccessToken());
                final GraphRequest graphRequest= GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Toast.makeText(LoginActivity.this, object + " ", Toast.LENGTH_SHORT).show();
                            //UserId=object.getString("id");
                            //profilePicture=new URL("https://graph.facebook/"+UserId+"/picture?width=500&height=500");
                            //Toast.makeText(LoginActivity.this,"url: "+profilePicture,Toast.LENGTH_SHORT).show();
                            //Timber.d(" Facebook profile: ",profilePicture.toString());
                            /*if(object.has("first_name")) {
                                first_name = object.getString("first_name");
                            }
                            if(object.has("last_name")) {
                                last_name = object.getString("last_name");
                                Timber.d("Name: ",first_name+" "+last_name);
                                Toast.makeText(LoginActivity.this,"name: "+first_name+" "+last_name,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("email")) {
                                email = object.getString("email");
                                Timber.d("Email: ",email);
                                Toast.makeText(LoginActivity.this,"Email: "+email,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("birthday")) {
                                birthday = object.getString("birthday");
                                Timber.d("Birthday: ",birthday);
                                Toast.makeText(LoginActivity.this,"Birthday: "+birthday,Toast.LENGTH_SHORT).show();
                            }
                            if(object.has("gender")) {
                                gender = object.getString("gender");
                                Timber.d("Gender: ",gender);
                                Toast.makeText(LoginActivity.this,"Gender: "+gender,Toast.LENGTH_SHORT).show();
                            }*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                //parameters.putString("fields", "FrienLlistActivity");
                // graphRequest.setParameters(parameters);
                // graphRequest.executeAsync();
                Toast.makeText(getApplicationContext(),"Success facebook!",Toast.LENGTH_SHORT).show();

            }

            public void handleFacebookAccessToken(AccessToken accessToken){
                AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
                fbauth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fbauth.getCurrentUser();
                            //loginwithfacebook(user.getUid(),user.getDisplayName());
                            SSHSLogin(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString(), FACEBOOK_LOGIN);
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed!!", Toast.LENGTH_SHORT).show();
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


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

   /* public void displayuserinfo(JSONObject object){
        String fname,lname,email,id,picture;
        try {
            fname=object.getString("first_name");
            lname=object.getString("last_name");
            email=object.getString("email");
            //picture=object.getString("picture");
            id=object.getString("id");
            Intent intentforLogin=new Intent(getApplicationContext(),MyHighwayActivity.class);
            intentforLogin.putExtra("email",email);
            intentforLogin.putExtra("userName",fname+" "+lname);
            intentforLogin.putExtra("imageURL","abc");
            Toast.makeText(getApplicationContext(),"email:"+email+" name:"+fname+" "+lname,Toast.LENGTH_SHORT).show();
            //startActivity(intentforLogin);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

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
                            SSHSLogin(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString(),GOOGLE_LOGIN);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Authentication Failed...",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void doCustomLogin(final String urlforlogin, final String user, final String password) {
        process = new ProgressDialog(LoginActivity.this);
        process.setMessage("Wait...");
        process.setCancelable(false);
        process.show();
        RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
        StringRequest sr = new StringRequest(Request.Method.POST, urlforlogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //loader.hide();
                /*if(process.isShowing())*/
                process.cancel();
                Timber.d("response:", response);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    s3 = jsonObject.getString("msg");
                    Timber.d("response:", s3);
                    JSONObject data = jsonObject.getJSONObject("0");
                    Timber.d("response:", data.toString());
                    User user = new User(getApplicationContext());
                    user.setUserEmail(data.getString("email"));
                    user.setUserMobNumber(data.getString("primary_phone"));
                    user.setUserDeviceNumber(data.getString("deviceid"));
                    user.setUserPhotoURi(data.getString("userimage"));
                    user.setUserName(data.getString("username"));
                    String loc[] = data.getString("last_location").split(",");
                    user.setLastLocationLat(Double.parseDouble(loc[0]+""));
                    user.setLastLocationLong(Double.parseDouble(loc[1]+""));
                    user.setUserId(data.getString("id"));
                    Log.e("response", response);
                    Toast.makeText(getApplicationContext(), "" + s3, Toast.LENGTH_SHORT).show();
                    if (s3.equals("Success!!")) {
                        //process.cancel();
                        SSHSLogin(user.getUserMobNumber(), user.getUserName(), user.getUserPhotoURi(), CUSTOM_LOGIN);
                                /*i1 = new Intent(getApplicationContext(), MyHighwayActivity.class);
                                i1.putExtra("email",s1);
                                i1.putExtra("userName","user");
                                i1.putExtra("imageURL",getResources().getDrawable(R.drawable.default_profile_image).toString());
                                startActivity(i1);*/
                    }
                } catch (JSONException ex) {
                    //process.cancel();
                    Toast.makeText(getApplicationContext(), "Json parsing Exception!" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                process.cancel();
                Toast.makeText(getApplicationContext(), "Something gone Wrong!!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("user", user.toLowerCase().replace(" ", ""));
                hm.put("upassword", password);
                return hm;
            }
        };
        rq.add(sr);
    }

    private void loginwithfacebook(final String userid, final String userName) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        String urlForRequest = getString(R.string.appwebsite) + "/api/facebooklogin.php";
            StringRequest stringRequest=new StringRequest(Request.Method.POST, urlForRequest, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("success")){
                        FirebaseUser user=fbauth.getInstance().getCurrentUser();
                        SSHSLogin(user.getEmail(),user.getDisplayName(),user.getPhotoUrl().toString(),FACEBOOK_LOGIN);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<String,String>();
                    params.put("uid",userid);
                    params.put("username",userName);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                //Toast.makeText(this,"Provide Gps access for Better result!",Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
            if(grantResults[1]== PackageManager.PERMISSION_DENIED){
                //Toast.makeText(this,"Provide Contact access for Better result!",Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
            if(grantResults[2]== PackageManager.PERMISSION_DENIED){
                //Toast.makeText(this,"Provide send SMS for Better result!",Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
            if (grantResults[3] == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(this, "Provide Permission for Read External Storage!", Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
            if (grantResults[4] == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(this, "Provide Permission for Write External Storage!", Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
            if (grantResults[5] == PackageManager.PERMISSION_DENIED) {
                //Toast.makeText(this, "Provide Permission for read Network status!", Toast.LENGTH_SHORT).show();
                //(new LoginActivity.MyAsyncTask()).execute();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void SSHSLogin(String email,String name,String imageURL,int type){
        Intent intentforLogin = new Intent(this, MyHighwayActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (type) {
            case GOOGLE_LOGIN:
                urlforlogin = getString(R.string.appwebsite)+"/api/loginwithGooglefacebook.php";
                doCustomLogin(urlforlogin, email, "");
                return;
            case FACEBOOK_LOGIN:
                urlforlogin = getString(R.string.appwebsite)+"/api/loginwithGooglefacebook.php";
                if (email == null) {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    Toast.makeText(this, "Can't Retrieve Email Choose another Option of Login", Toast.LENGTH_SHORT).show();
                } else {
                    doCustomLogin(urlforlogin, email, "");
                }
                return;
            case CUSTOM_LOGIN:
                break;
                default:return;
        }
        if (isnumber(email)) {
            editor.putString("userMobNumberkey", email);
        } else {
            editor.putString("userEmailkey", email);
        }
        editor.putString("userNamekey",name);
        editor.putString("userPhotoURikey",imageURL);
        editor.putInt("friends",0);
        editor.commit();
       // Toast.makeText(this,"Go...",Toast.LENGTH_SHORT).show();
        startActivity(intentforLogin);
        finish();
    }

    public boolean isnumber(String user) {
        String regex = "-?\\d+(\\.\\d+)?";
        try {
            if (user.matches(regex))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (manager.getActiveNetworkInfo() != null && manager.getActiveNetworkInfo().isConnected());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {     //event on pressed BACK key
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitAlert();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public class WaitingTask extends AsyncTask<String,String, String> {
       // private int seconds;
        //ProgressDialog progressDialog;
        public WaitingTask(int timeinSecond) {
            seconds=timeinSecond;
        }
        @Override
        protected void onPreExecute() {
            waitingDialog = new ProgressDialog(LoginActivity.this);
            waitingDialog.setMessage("Please Wait for " + seconds + "seconds");
            waitingDialog.setCancelable(false);
            waitingDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                for(int i=1;i<=seconds;i++){
                    //Thread.sleep((seconds/5)*1000);
                    //publishProgress(5-i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
               // waitingDialog.cancel();
            //progressDialog.cancel();
        }
    }

    public void showExitAlert() {    //for show exit application alertdialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setCancelable(false)
                .setMessage("Do You Want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    public class MyAsyncTask extends AsyncTask<String,String,String>{
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(LoginActivity.this);

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
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_NETWORK_STATE}, 1);
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
}
