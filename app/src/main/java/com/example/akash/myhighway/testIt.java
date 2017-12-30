package com.example.akash.myhighway;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.security.Permission;

import de.hdodenhof.circleimageview.CircleImageView;

public class testIt extends AppCompatActivity implements OnMapReadyCallback,NavigationView.OnNavigationItemSelectedListener{
private static final String TAG="mainActivity";
    Button b1,updateLocation,getPlaceButton;
    TextView t1,t2,prEmail,prName;
    CircleImageView prPhoto;
    Toolbar toolbar;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mToggle;
    GPSTracker myTracker;
    GeoDataClient geoDataClient;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    int PICK_CONTACT=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_it);
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

        Window window=this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(testIt.this,R.color.colorWhatsapp));

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

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userEmail=getIntent().getStringExtra("email");
        String userName=getIntent().getStringExtra("userName");
        Toast.makeText(this,"mail:"+userEmail+"  name:"+userName,Toast.LENGTH_SHORT).show();
        String userImageURLs=getIntent().getStringExtra("imageURL");
        if(userImageURLs!=null) {
            Uri myuri = Uri.parse(userImageURLs);
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
        prName.setText(userName);
        if(userEmail==null) {
            prEmail.setText("Email not linked yet!");
            prEmail.setTextColor(getResources().getColor(R.color.errorColor));
            prEmail.setClickable(true);
            prEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"Please provide Your Email!! for your Convenience",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            prEmail.setText(userEmail);
       supportMapFragment=((SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment));
       supportMapFragment.getMapAsync(testIt.this);
            t1=findViewById(R.id.longitudeText);
            t2=findViewById(R.id.latitudeText);
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
    }


    @Override
    public void onBackPressed() {
       if(mDrawer.isDrawerOpen(GravityCompat.START)){
           mDrawer.closeDrawer(GravityCompat.START);
       }
       else
           super.onBackPressed();
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
                fragment=new DefaultContentFragment();
                break;
            }
            case R.id.logout: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            }
            case R.id.friendlist: {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACT);
                break;
            }
            case R.id.aboutus:{
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
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,PICK_CONTACT);
    }
    if(id==R.id.idshareIt){
        Intent myShareIntent=new Intent(Intent.ACTION_SEND);
        String subject="Subject of Sharing";
        String body="this is Body written by Akash";
        myShareIntent.putExtra(Intent.EXTRA_SUBJECT,subject);
        myShareIntent.putExtra(Intent.EXTRA_TEXT,body);
        startActivity(Intent.createChooser(myShareIntent,"Share My Highway message to"));
    }
    if(id==R.id.idinfo){
        Intent myInfoIntent=new Intent(Intent.ACTION_SHOW_APP_INFO);
        startActivity(myInfoIntent);
    }
    if(id==R.id.idlogout){
        FirebaseAuth.getInstance().signOut();
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
            if (requestCode == Activity.RESULT_OK) {
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
        this.googleMap.addMarker(new MarkerOptions().position(current).title("Current Place"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,9));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]== PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Provide Gps access for Better result!",Toast.LENGTH_SHORT).show();
            finish();
            onStop();
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        myTracker.stopUsingGPS();
        super.onStop();
    }


}
