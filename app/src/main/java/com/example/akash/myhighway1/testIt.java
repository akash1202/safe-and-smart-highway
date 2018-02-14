package com.example.akash.myhighway1;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.akash.myhighway1.POJO.Example;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.datatype.Duration;
import javax.xml.transform.ErrorListener;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//TODO: add customized marker example by implementing GoogleMap.InfoWindowAdapter
public class testIt extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnInfoWindowClickListener,GoogleMap.InfoWindowAdapter{
private static final String TAG="mainActivity";
    private static final int CHANGE_PROFILE_PICTURE = 22;
    public static final String DATABASE_NAME="ContactManager";
    Button b1,updateLocation,getPlaceButton;
    FloatingActionButton floatingActionButton;
    TextView t1,t2,t3,prEmail,prName;
    CircleImageView prPhoto;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    Snackbar snackbar;
    FirebaseAuth FAuth;
    FirebaseUser FUser; //current user
    String OnlineUserId;
    DatabaseReference FReference=null;
    GPSTracker myTracker;
    GeoDataClient geoDataClient;
    MyAsyncTask task=new MyAsyncTask();
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFRENCENAME="AKASHSASH";
    static final int CROP_FROM_CAMERA=21;


    int PICK_CONTACT=3;
    static final int REQUEST_CONTACT=11;
    int changed=0;
    Uri changedprImageUri=null;
    Location l1,l2;
    LocationRequest mLocationRequest=null;
    GoogleApiClient mGoogleApiClient=null;
    private int PROXIMITY_RADIUS = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_it);
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        if(sharedPreferences.getString("userNamekey","").equals("")){
            startActivity(new Intent(testIt.this,MainActivity.class));
            finish();
    }
    floatingActionButton=(FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(testIt.this,MapsActivity.class));
            }
        });

       /* Fragment fragment=new DefaultContentFragment();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.default_container, fragment);
        transaction.commit();*/

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        mDrawer=findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.draweropen_desc,R.string.drawerclose_desc);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

       /* Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
       window.setStatusBarColor(ContextCompat.getColor(testIt.this,R.color.colorWhatsapp));

      */
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().set
        //getSupportActionBar().setHomeButtonEnabled(true);

        //getSupportActionBar(findViewById(R.id.myactionbar));

            /*b1=(Button) findViewById(R.id.refreshtokenButton);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String Token= FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG,"Token:"+Token);
                }
            });*/
/*        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.app_bar);*/
        setnavigationViewListener();
        NavigationView navigationView= findViewById(R.id.navigationMenu);
        View header =navigationView.getHeaderView(0);


        task.execute();

        prName=(TextView) header.findViewById(R.id.prName);
        prEmail=(TextView) header.findViewById(R.id.prEmail);
        prPhoto=(CircleImageView) header.findViewById(R.id.profileImage);
    //sharedPreferences.getString("emailkey","")!=null&&sharedPreferences.getString("usernamekey","")!=null&&sharedPreferences.getString("imageURLkey","")!=null
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        String userEmail=sharedPreferences.getString("userEmailkey","");
        String userName=sharedPreferences.getString("userNamekey","");
        String userImageURLs=sharedPreferences.getString("userPhotoURikey","");
        String userPhone=sharedPreferences.getString("userMobNumberkey","");
        changed=Integer.parseInt(sharedPreferences.getString("changedpic","0"));
       // FUser=FirebaseAuth.getInstance().getCurrentUser();
       // OnlineUserId=FUser.getUid();
        OneSignal.sendTag("username:",userName);
       // FReference=FirebaseDatabase.getInstance().getReference().child("Users").child(OnlineUserId);
       // FReference.child("user_name").setValue(userName);
       // FReference.child("user_email").setValue(userEmail);
       // FReference.child("user_phone").setValue(userPhone);
        if(!userImageURLs.equals("")&&changed==0) {
            //Uri myuri = Uri.parse(userImageURLs);
           // Toast.makeText(testIt.this,myuri+":parsed",Toast.LENGTH_LONG).show();
            try {
                /*Picasso.with(testIt.this)
                        .load(userImageURLs)
                        .centerCrop()
                        .resize(70, 70)
                        .into(prPhoto);*/
                /*Picasso.with(testIt.this).load(userImageURLs)
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(prPhoto);*/
                com.nostra13.universalimageloader.core.ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
                imageLoader.displayImage(userImageURLs,prPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(userImageURLs.equals("")&&changed==0){
            //Toast.makeText(testIt.this,Uri.parse(userImageURLs)+":parsed",Toast.LENGTH_LONG).show();
            ColorGenerator generator=ColorGenerator.MATERIAL;
            TextDrawable textDrawable=TextDrawable.builder()
                    .beginConfig()
                    .width(70)
                    .height(70)
                    .endConfig()
                    .buildRound(userName.toUpperCase().charAt(0)+"", generator.getColor(userName));
        prPhoto.setImageDrawable(textDrawable);
        }
        else {
            //Toast.makeText(testIt.this,Uri.parse(userImageURLs)+":parsed",Toast.LENGTH_LONG).show();
            //prPhoto.setImageURI(Uri.parse(userImageURLs));
            Picasso.with(testIt.this)
                    .load(userImageURLs)
                    .centerCrop()
                    .resize(70, 70)
                    .into(prPhoto);
        }
        prName.setText(userName);
        if(userEmail.equals("")) {
            if(userPhone.equals("")) {
                prEmail.setText("Email/Phone not linked yet!");
                prEmail.setTextColor(getResources().getColor(R.color.errorColor));
                prEmail.setClickable(true);
                prEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "Please provide Your Email/Phone!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(testIt.this, verify_number.class));
                    }
                });
            }
            else
                prEmail.setText(userPhone);
        }
        else
            prEmail.setText(userEmail);
       supportMapFragment=((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment));
       supportMapFragment.getMapAsync(testIt.this);
            t1=findViewById(R.id.longitudeText);
            t2=findViewById(R.id.latitudeText);
            t3=findViewById(R.id.speedText);
        updateLocation=findViewById(R.id.getLocation);
        myTracker=new GPSTracker(testIt.this);
        updateLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myTracker.cangetLocation()){
                        myTracker.getLocation();
                        /*if(snackbar.isShown()){
                            snackbar.dismiss();}*/
                        l1=new Location("A");
                        l1.setLatitude(myTracker.getLatitude());
                        l1.setLongitude(myTracker.getLongitude());
                        snackbar=Snackbar.make(view,"lat:"+myTracker.getLatitude()+" long:"+myTracker.getLongitude()+"\nspeed: 0km/h",Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(getResources().getColor(R.color.whitecolor));
                        snackbar.setAction("dismiss", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        View sbView=snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.colorWhatsapp));
                        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE).show();
                       // t1.setText(myTracker.getLongitude()+"");
                       // t2.setText(myTracker.getLatitude()+"");
                        //onMapReady(googleMap);
                        setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
                    }
                    else{
                        myTracker.showSettingAlert();
                    }
                }
            });
        //getActionBar().setCustomView(t);//setActionBar(t);
    // AIzaSyB2_305KWe0wmWX1TWX_AJDBBDh-d2pe_4 map-api key
        /*FReference=FirebaseDatabase.getInstance().getReference().child("Users").child(OnlinUserId);
        if(FUser!=null){
            FReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    FReference.child("online").onDisconnect().setValue(false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onBackPressed() {
       if(mDrawer.isDrawerOpen(GravityCompat.START)){
           mDrawer.closeDrawer(GravityCompat.START);
       }
       else
       {
           finish();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }
    public void setnavigationViewListener(){
        NavigationView navigationView=(NavigationView) findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment=null;

        switch(item.getItemId()){
            case R.id.dashboard:{
                startActivity(new Intent(this,Dashboard.class));
                break;
            }
            case R.id.device:{
                break;
            }
            case R.id.vehicle:{
                break;
            }
            case R.id.history:{
                break;
            }
            case R.id.status:{
                startActivity(new Intent(this,ForwardTo.class));
                break;
            }
            case R.id.logout: {
                getApplicationContext().deleteDatabase(DATABASE_NAME);
                sharedPreferences.edit().clear().commit();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            }
            case R.id.friendlist: {
                /*Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACT);*/
                startActivity(new Intent(testIt.this,Friends.class));
                break;
            }
            case R.id.aboutus:{
                startActivity(new Intent(testIt.this,Aboutus.class));
                break;
            }
            default: break;
        }

       /* FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, fragment);
        transaction.commit();
       */ mDrawer.closeDrawer(GravityCompat.START);
       // mDrawer.openDrawer(GravityCompat.END);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
    if(id==R.id.idaddfriend_option){
        Intent intent = new Intent(testIt.this,Friends.class);
        startActivity(intent);
    }
    if(id==R.id.idshareIt){
        /*Intent myShareIntent=new Intent(Intent.ACTION_SEND);
        String subject="Subject of Sharing";
        String body="Download this Awesome SASH App For Safety and Security of you,Your friends and for your family member link:http://www.ababab.com/abab";
        myShareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        myShareIntent.putExtra(Intent.EXTRA_TEXT,body);
        myShareIntent.setType("text/plain");
        startActivity(Intent.createChooser(myShareIntent,"Share SSHS Using"));*/
        ApplicationInfo app =getApplicationContext().getApplicationInfo();
        String filePath=app.sourceDir;

        File FinalAPK=getApkFile(this,"com.example.akash.myhighway");
        this.grantUriPermission(this.getPackageName(),Uri.parse(filePath), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.getContentResolver().takePersistableUriPermission(Uri.parse(filePath), Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(filePath)));
        startActivity(Intent.createChooser(intent,"Share SASH using"));
    }
    if(id==R.id.idinfo){
        Intent myInfoIntent=new Intent(Intent.ACTION_SHOW_APP_INFO);
        startActivity(myInfoIntent);
    }
    if(id==R.id.idlogout){
        getApplicationContext().deleteDatabase(DATABASE_NAME);
        sharedPreferences.edit().clear().commit();
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    //if(id==R.id.myactionbar){

    //}
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT) {
            /*if (requestCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor contactCursor = getContentResolver().query(contactData,
                        new String[]{ContactsContract.Contacts._ID}, null, null,
                        null);
                String id = null;
                if (contactCursor.moveToFirst()) {
                    id = contactCursor.getString(contactCursor
                            .getColumnIndex(ContactsContract.Contacts._ID));
                }
                contactCursor.close();
                String phoneNumber = null;
                Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? ",
                        new String[]{id}, null);
                if (phoneCursor.moveToFirst()) {
                    phoneNumber = phoneCursor
                            .getString(phoneCursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phoneCursor.close();
            }*/
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    prPhoto.setImageURI(resultUri);
                    uploadImage(resultUri,"140420107001");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if(requestCode==CROP_FROM_CAMERA){
            Bitmap croppedImage=null;
          Uri photoTempUri= data.getData();
          croppedImage=BitmapFactory.decodeFile(photoTempUri.getPath());
            try {
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                croppedImage.compress(Bitmap.CompressFormat.JPEG,100,stream);
                Toast.makeText(testIt.this,""+photoTempUri,Toast.LENGTH_LONG).show();
                //uploadImage(photoTempUri,"140420107001");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(requestCode==CHANGE_PROFILE_PICTURE){
            if(resultCode==RESULT_OK) {
                if(data!=null) {
                    Uri imageuri = data.getData();
                    try {
            /*   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
               prPhoto.setImageBitmap(bitmap);*/
                        //prPhoto.setImageURI(imageuri);
                    } catch (Exception e) {
                        Toast.makeText(testIt.this, "Exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //this is for read image even after restart mobile
                    this.grantUriPermission(this.getPackageName(), imageuri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    this.getContentResolver().takePersistableUriPermission(imageuri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    editor.putString("userPhotoURikey", String.valueOf(imageuri));
                    try {
                        CropImage.activity(imageuri)
                                .start(this);
                        //doCrop(imageuri);
                        //uploadImage(imageuri,"140420107001");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    changed += 1;
                    editor.putString("changedpic", changed + "");
                    editor.commit();
                    Toast.makeText(testIt.this, changed + "" + imageuri.toString(), Toast.LENGTH_LONG).show();
                }
                }
            }
    }




    //croping image
    private void doCrop(Uri mImageCaptureUri) {
       // this.mImageCaptureUri = mImageCaptureUri;
        this.grantUriPermission(this.getPackageName(), mImageCaptureUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.getContentResolver().takePersistableUriPermission(mImageCaptureUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType("image/*");
       // intent.setDataAndType(mImageCaptureUri, "image/*");
      // List<ResolveInfo> list = getPackageManager().queryIntentActivities(
              // intent, 0);
       // int size = list.size();

        if (false) {
            // Toast.makeText(this,
            // "Can not find image crop section",Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Toast.makeText(this,
            // "image crop section started..",Toast.LENGTH_SHORT).show();

            intent.setData(mImageCaptureUri);

            // Bitmap bitmap_1 = readBitmap(mImageCaptureUri);
            Bitmap tempBitmap=null;
            try {
                tempBitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), mImageCaptureUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }// = bitmap_1;
           /// new MydataLoader().execute(function.sendimage);
            // changed

            intent.putExtra("crop", true);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_FROM_CAMERA);
            // Log.e("size",""+size);
            /*if (size >= 1) {
                // Toast.makeText(MerchantLogin.this,
                // "Cropimage got called with size"+ size,
                // Toast.LENGTH_SHORT).show();
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));

              //  startActivityForResult(i, CROP_FROM_CAMERA);

            } else {
                // Toast.makeText(MerchantLogin.this,
                // "Cropimage not get called", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
    public boolean uploadImage(Uri uri, final String id) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = null;
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        //Setting image to ImageView
        //image.setImageBitmap(bitmap);
        //final ProgressDialog progDialog = new ProgressDialog(testIt.this);
        //progDialog.setMessage("Uploading, please wait...");
        //progDialog.show();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String uploadingUsingURL="https://myhighway.000webhostapp.com/api/upload.php";
        final String path=uri.getPath();
        final String extension=path.substring(path.lastIndexOf("."));
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        final String userId=id;
        RequestQueue requestQueue= Volley.newRequestQueue(testIt.this);
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, uploadingUsingURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               // progDialog.dismiss();
                Toast.makeText(testIt.this, response, Toast.LENGTH_LONG).show();/*
                if(response.equals("success")){
                    Toast.makeText(testIt.this, "Uploaded Successful", Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(testIt.this, "Some error occurred!......", Toast.LENGTH_LONG).show();
                }*/
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(testIt.this, "Some error occurred -> "+error, Toast.LENGTH_LONG).show();;
            }
        }){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                parameters.put("extension",extension);
                parameters.put("id",userId);

                return parameters;
            }
        };


        requestQueue.add(request);

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setBuildingsEnabled(true);
        this.googleMap.setOnInfoWindowClickListener(this);
        this.googleMap.setInfoWindowAdapter(this);
        UiSettings uiSettings=this.googleMap.getUiSettings();
        //uiSettings.setAllGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(true);
        if(myTracker.cangetLocation()){
            myTracker.getLocation();
            LatLng previousUpdateLocation = new LatLng(myTracker.getLatitude(), myTracker.getLongitude());
            this.googleMap.addMarker(new MarkerOptions().position(previousUpdateLocation).title("Current Location1"));
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(previousUpdateLocation));
        }
        buildGoogleApiClient();
        try {
            this.googleMap.setMyLocationEnabled(true);
        }
        catch (SecurityException e){
            Toast.makeText(testIt.this,"Security Exception:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(testIt.this,"Provide GPS Permission",Toast.LENGTH_SHORT).show();
        }
    }
    public void setLocationOnMap(double lat,double lon){
        this.googleMap.clear();
        LatLng current = new LatLng(lat,lon);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.setMinZoomPreference(10);  //for see into city
        CameraPosition cameraPosition= new CameraPosition.Builder()
                .target(current)
                .zoom(17)
                .bearing(90)
                .tilt(80)
                .build();
        try {
            googleMap.addCircle(new CircleOptions()
            .center(current)
            .radius(PROXIMITY_RADIUS)
            .strokeColor(Color.RED)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        Marker marker=this.googleMap.addMarker(new MarkerOptions().position(current).title("Current Place"));
        marker.showInfoWindow();
        build_retrofit_and_get_response("restaurant");
        //this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        this.googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),2000,null);

    }

    //place type in google map
    /*accounting,airport,amusement_park,aquarium,art_gallery,atm,bakery,bank,bar,beauty_salon,bicycle_store,book_store,bowling_alley
    bus_station,cafe,campground,car_dealer,car_rental,car_repair,car_wash,casino,cemetery,church,city_hall,clothing_store,convenience_store
    courthouse,dentist,department_store,doctor,electrician,electronics_store,embassy,fire_station,florist,funeral_home,furniture_store
    gas_station,gym,hair_care,hardware_store,hindu_temple,home_goods_store,hospital,insurance_agency,jewelry_store,laundry,lawyer
    library,liquor_store,local_government_office,locksmith,lodging,meal_delivery,meal_takeaway,mosque,movie_rental,movie_theater,moving_company
    museum,night_club,painter,park,parking,pet_store,pharmacy,physiotherapist,plumber,police,post_office,real_estate_agency,restaurant
    roofing_contractor,rv_park,school,shoe_store,shopping_mall,spa,stadium,storage,store,subway_station,supermarket,synagogue,taxi_stand
    train_station,transit_station,travel_agency,veterinary_care,zoo*/

    // zoom level by google by default

    /*
    1: World
    5: Landmass/continent
    10: City
    15: Streets
    20: Buildings   */

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getNearbyPlaces(type, myTracker.getLatitude()+","+myTracker.getLongitude(), PROXIMITY_RADIUS);
        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call,Response<Example> response) {
                final Response<Example> myresponse = response;
                Log.d("url:",response.toString());
                //mMap.clear();
                // This loop will go through all the results and add marker on each location.
                final int total=response.body().getResults().size();
                 final MarkerOptions markerOptions[] = new MarkerOptions[total];
                 final String placeImages[]=new String[total];
                //final Bitmap bitmapImage[] = new Bitmap[response.body().getResults().size()];
                for (int i = 0; i < total; i++) {
                    Double lat = myresponse.body().getResults().get(i).getGeometry().getLocation().getLat();
                    Double lng = myresponse.body().getResults().get(i).getGeometry().getLocation().getLng();
                    final String placeName = myresponse.body().getResults().get(i).getName();
                    String vicinity = myresponse.body().getResults().get(i).getVicinity();
                    placeImages[i] = myresponse.body().getResults().get(i).getIcon();
                    Log.d(placeName,placeImages[i]);
                    markerOptions[i] = new MarkerOptions();
                    LatLng latLng = new LatLng(lat, lng);
                    // Position of Marker on Map
                    markerOptions[i].position(latLng);
                    l2=new Location("B");
                    l2.setLatitude(lat);
                    l2.setLongitude(lng);

                    // Adding Title to the Marker
                    markerOptions[i].title(placeName);
                   // markerOptions[i].icon(BitmapDescriptorFactory.fromResource(R.drawable.com_facebook_profile_picture_blank_square));
                    final int j=i;


                    /*googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                *//*
                Here you will inflate the custom layout that you want to use for marker. I have inflate the custom view according to my requirements.
               *//*
                            View v = getLayoutInflater().inflate(R.layout.custom_marker, null);
                            ImageView imageView = (ImageView) v.findViewById(R.id.imgView_map_info_content);
                            imageView.setImageURI(Uri.parse(placeImages[j]));
                            return v;
                        }
                    });*/

                    /*Bitmap theBitmap = Glide.
                            with(testIt.this)
                            .load("http://....")
                            .asBitmap()
                            .into(100, 100)
                            .get();*/
                    // Adding colour to the marker
                    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    // Adding Marker to the Camera.
                    double distance=l2.distanceTo(l1);
                    distance=distance/1000;
                    String str=String.format("%2.2f",distance);
                    markerOptions[i].zIndex(90);
                    markerOptions[i].describeContents();
                    markerOptions[i].snippet(str+"kms : "+vicinity);


                    Marker markertemp=googleMap.addMarker(markerOptions[i]);
                    PicassoMarker marker = new PicassoMarker(markertemp);
                    //"https://instagram.fbom1-2.fna.fbcdn.net/vp/4194fe04f692b50fb164ff8d4779fe1d/5B24DAA7/t51.2885-19/s150x150/22801907_169073640345040_1768792490271309824_n.jpg"
                    Picasso.with(testIt.this).load(placeImages[i]).into(marker);
                    /*class getBitmapThread extends Thread {
                        Bitmap bitmap = null;
                        String url = "";

                        getBitmapThread(String url) {
                            this.url = url;
                        }

                        public Bitmap getImage() {
                            return bitmap;
                        }

                        public void run() {

                        }
                    }*/

                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }

               // final MarkerOptions markerOptions1[]=markerOptions;

                Log.d("length:",markerOptions.length+"");
              //  Log.d("length1:",markerOptions1.length+"");
             //for (int i = 0; i < total; i++) {
                   /*final int j = i;
                    Bitmap bitmapImage = null;
                    Picasso.with(testIt.this)
                            .load(Uri.parse(placeImages[i]))
                            .resize(25, 25)
                            .into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    markerOptions1[j].icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                                    Log.d("Icon:",markerOptions1[j].getIcon().toString());
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                //markerOptions1[j].icon(BitmapDescriptorFactory.fromResource(R.drawable.com_facebook_profile_picture_blank_square));
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {

                                }
                            });
                    markerOptions[i].icon(markerOptions1[i].getIcon());
*/
                    // move map camera
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                 //markerOptions[i].icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                 //Log.d("Icon:",markerOptions[j].getIcon().toString());

            //  }
                Toast.makeText(testIt.this, "" + response.body().getResults().size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(testIt.this,"Failer error:"+t.toString(),Toast.LENGTH_SHORT).show();
            }
            });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Gps access for Better result!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
            if(grantResults[1]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Contact access for Better result!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
            if(grantResults[2]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide send SMS for Better result!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
            if(grantResults[3]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Permission for Read External Storage!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
            if(grantResults[4]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Permission for Write External Storage!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
            if(grantResults[5]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Permission for read Network status!",Toast.LENGTH_SHORT).show();
                (new testIt.MyAsyncTask()).execute();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void editImage(View view){
            Toast.makeText(testIt.this,"hello",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent,"Select Profile Picture"),CHANGE_PROFILE_PICTURE);
    }
    private HashMap<String,String> getAllInstalled(Context context){
        HashMap<String,String> allInstalledApkPath=new HashMap<>();
        PackageManager packageManager=context.getPackageManager();
        List<PackageInfo> PackageInfoList=packageManager.getInstalledPackages(PackageManager.SIGNATURE_MATCH);
        if(isValid(PackageInfoList)){
        for(PackageInfo packageInfo :PackageInfoList){
            ApplicationInfo applicationInfo;
            applicationInfo = packageInfo.applicationInfo;
            String packagename = applicationInfo.packageName;
            String versionName = packageInfo.versionName;
            int versioncode = packageInfo.versionCode;
            File apkFile = new File(applicationInfo.publicSourceDir);
            if (apkFile.exists()) {
                allInstalledApkPath.put(packagename, apkFile.getAbsolutePath());
            }
        }
        }
        return allInstalledApkPath;
    }
    public boolean isValid(List<PackageInfo> infolist){
        return (infolist!=null&&!infolist.isEmpty());
    }
    public File getApkFile(Context context,String PackageName){
    HashMap<String,String> AllInstalledapkPath=getAllInstalled(context);
    File ApkFile= new File(AllInstalledapkPath.get(PackageName));
    if(ApkFile.exists()){
        return ApkFile;
    }
    return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //FReference.child("online").setValue(true);
    }

    @Override
    protected void onStop() {
        myTracker.stopUsingGPS();
       // FReference.child("online").setValue(false);
        super.onStop();
    }


    /*@Override
    public void onLocationChanged(Location location) {
        Toast.makeText(testIt.this,"Location changed",Toast.LENGTH_SHORT);
      *//*  t1.setText(myTracker.getLongitude()+"");
        t2.setText(myTracker.getLatitude()+"");
        if(location.hasSpeed()){
            t3.setText(location.getSpeed()*(3600/1000)+"");
                myTracker.getLocation();
                setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
                onMapReady(googleMap);
        }
        else{
            //Toast.makeText(testIt.this,"Due to Network based location can't get Speed",Toast.LENGTH_SHORT).show();
            t3.setText("0 km/h");
    }*//*
    }*/

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.showInfoWindow();
       Toast.makeText(this, ""+marker.getTitle(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v= getLayoutInflater().inflate(R.layout.custom_marker,null);
        ImageView imageIcon=v.findViewById(R.id.imgView_map_info_content);
        TextView textView=v.findViewById(R.id.info_infowindow);
        imageIcon.setImageResource(R.drawable.profile);
        textView.setText(marker.getSnippet());
        return v;
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
            ActivityCompat.requestPermissions(testIt.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.SEND_SMS,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.ACCESS_NETWORK_STATE},1);
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






    //for GoogleApi implement
    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // mLocationRequest=new LocationRequest();
       // mLocationRequest.setInterval(1000);
       // mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(testIt.this,"APi error: suspended connection",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Toast.makeText(testIt.this,"APi error:"+connectionResult.getErrorMessage().toString(),Toast.LENGTH_SHORT).show();
    }

}
