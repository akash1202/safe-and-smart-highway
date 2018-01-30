package com.example.akash.myhighway1;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.akash.myhighway1.POJO.Example;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class testIt extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{
private static final String TAG="mainActivity";
    private static final int CHANGE_PROFILE_PICTURE = 21;

    Button b1,updateLocation,getPlaceButton;
    FloatingActionButton floatingActionButton;
    TextView t1,t2,t3,prEmail,prName;
    CircleImageView prPhoto;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    FirebaseAuth FAuth;
    FirebaseUser FUser; //current user
    String OnlineUserId;
    DatabaseReference FReference=null;
    GPSTracker myTracker;
    GeoDataClient geoDataClient;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String PREFRENCENAME="AKASHSASH";
    int PICK_CONTACT=3;
    static final int REQUEST_CONTACT=11;
    int changed=0;
    Uri changedprImageUri=null;
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



        prName=header.findViewById(R.id.prName);
        prEmail=header.findViewById(R.id.prEmail);
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
            Uri myuri = Uri.parse(userImageURLs);
            Toast.makeText(testIt.this,myuri+":parsed",Toast.LENGTH_LONG).show();
            try {
                Picasso.with(testIt.this)
                        .load(myuri)
                        .centerCrop()
                        .resize(70, 70)
                        .into(prPhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(userImageURLs.equals("")&&changed==0){
            Toast.makeText(testIt.this,Uri.parse(userImageURLs)+":parsed",Toast.LENGTH_LONG).show();
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
            Toast.makeText(testIt.this,Uri.parse(userImageURLs)+":parsed",Toast.LENGTH_LONG).show();
            prPhoto.setImageURI(Uri.parse(userImageURLs));
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
                        t1.setText(myTracker.getLongitude()+"");
                        t2.setText(myTracker.getLatitude()+"");
                        setLocationOnMap(myTracker.getLatitude(),myTracker.getLongitude());
                        onMapReady(googleMap);
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
        if(requestCode==CHANGE_PROFILE_PICTURE){
            if(resultCode==RESULT_OK) {
                if(data!=null) {
                    Uri imageuri = data.getData();
                    try {
            /*   Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
               prPhoto.setImageBitmap(bitmap);*/
                        prPhoto.setImageURI(imageuri);
                    } catch (Exception e) {
                        Toast.makeText(testIt.this, "Exception:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    //this is for read image even after restart mobile
                    this.grantUriPermission(this.getPackageName(), imageuri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    this.getContentResolver().takePersistableUriPermission(imageuri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    editor.putString("userPhotoURikey", String.valueOf(imageuri));
                    changed += 1;
                    editor.putString("changedpic", changed + "");
                    editor.commit();
                    Toast.makeText(testIt.this, changed + "" + imageuri.toString(), Toast.LENGTH_LONG).show();
                }
                }
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(myTracker.cangetLocation()){
            myTracker.getLocation();
        }
    }
    public void setLocationOnMap(double lat,double lon){
        LatLng current = new LatLng(lat,lon);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings uiSettings=googleMap.getUiSettings();
        //uiSettings.setAllGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomControlsEnabled(true);
        CameraPosition cameraPosition= new CameraPosition.Builder()
                .target(current)
                .zoom(15)
                .bearing(90)
                .tilt(60)
                .build();
        Marker marker=this.googleMap.addMarker(new MarkerOptions().position(current).title("Current Place"));
        marker.showInfoWindow();
        this.googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        build_retrofit_and_get_response("car_repair");
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

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getNearbyPlaces(type, myTracker.getLatitude() + "," + myTracker.getLongitude(), PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                GoogleMap mMap = googleMap;
                //mMap.clear();
                // This loop will go through all the results and add marker on each location.
               try {
                   for (int i = 0; i < response.body().getResults().size(); i++) {
                       Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                       Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                       String placeName = response.body().getResults().get(i).getName();
                       String vicinity = response.body().getResults().get(i).getVicinity();
                       MarkerOptions markerOptions = new MarkerOptions();
                       LatLng latLng = new LatLng(lat, lng);
                       // Position of Marker on Map
                       markerOptions.position(latLng);
                       // Adding Title to the Marker
                       markerOptions.title(placeName + " : " + vicinity);
                       // Adding Marker to the Camera.
                       Marker m = mMap.addMarker(markerOptions);
                       // Adding colour to the marker
                       markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                       // move map camera
                       mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                       mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                   }
                   mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
               }
               catch (Exception e){
                   Log.d("onResponse", "There is an error");
                   e.printStackTrace();
               }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {

            }
            });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
                if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Gps access for Better result!",Toast.LENGTH_SHORT).show();
            //finish();
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


    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(testIt.this,"Location changed",Toast.LENGTH_SHORT);
      /*  t1.setText(myTracker.getLongitude()+"");
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
    }*/
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {


    }

    //for GoogleApi implement
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
