package com.example.akash.myhighway1;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by vishal on 11/12/17.
 */

public class GPSTracker extends Service implements LocationListener,android.location.GpsStatus.Listener
 {
     Context mcontext;
     public boolean isGPSEnabled=false;
     public boolean isNetworkEnabled=false;
     public int FORCE_GPS=0;
     public static boolean cangetLocation=false;
     Location location=null;
     Location lastlocation=null;
     Activity activity;
     double longitude;
     double latitude;
     private int FIRSTTIME=0;
     private long MIN_DISTANCE=5;  //for 1 meter
     private long MIN_TIME=1000*5; //for 1 second
     LocationManager locationManager;
     SharedPreferences sharedPreferences;
     private String PREFRENCENAME="AKASHSASH";
     SharedPreferences.Editor editor;
    public GPSTracker(Context context, Activity activity) {
            this.mcontext=context;
            this.activity=activity;
            sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
            editor=sharedPreferences.edit();
            getLocation();
    }
    public Location getLocation(){
        try{
            if(ContextCompat.checkSelfPermission(mcontext, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED||ContextCompat.checkSelfPermission(mcontext,android.Manifest.permission.ACCESS_NETWORK_STATE)== PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(mcontext,android.Manifest.permission.ACCESS_NETWORK_STATE)== PackageManager.PERMISSION_GRANTED)
                {
                    locationManager = (LocationManager) mcontext.getSystemService(LOCATION_SERVICE);
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!isGPSEnabled && !isNetworkEnabled) {
                        showSettingAlert();
                    } else {
                        cangetLocation = true;
                        if (isNetworkEnabled&&FORCE_GPS==0) {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                    Log.d("using ","Network:"+longitude+","+latitude);
                                }
                            }
                        }
                        if (isGPSEnabled) {
                           if (location == null){  //location==null
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                    Log.d("using ","GPS:"+longitude+","+latitude);
                                }
                            }
                        }
                        }
                    }
                }
                else{
                    Toast.makeText(this.mcontext,"provide GPS status access permission for better result",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this.mcontext,"provide Network status access permission for better result",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this.mcontext,"Exception for Location:"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return location;
    }
        public double getLongitude(){
        if(location!=null)
        longitude=location.getLongitude();
        return longitude;
        }
     public double getLatitude(){
         if(location!=null)
             latitude=location.getLatitude();
         return latitude;
     }
    public boolean cangetLocation(){
        return this.cangetLocation;
    }
    public void showSettingAlert(){
        final AlertDialog.Builder alert= new AlertDialog.Builder(mcontext);

        alert.setTitle("GPS Setting");
        alert.setMessage("GPS is require to Serve you Better!!");

        //On Pressing Setting Button
        alert.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Goto Setting for Enable GPS
                dialogInterface.cancel();
                mcontext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                cangetLocation=true;
            }
        });
        //On pressing Cancel Button
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();  //canceling Prompt
            }
        });
        alert.show();
    }
    public void stopUsingGPS(){
    if(locationManager!=null){
        locationManager.removeUpdates(GPSTracker.this);
    }
 }
     @Override
     public IBinder onBind(Intent intent) {
         return null;
     }

     @Override
     public void onLocationChanged(Location location) {
        //Toast.makeText(mcontext,"Changed...."+location.getSpeed(),Toast.LENGTH_LONG).show();
       // mcontext.getApplicationContext().
         editor.putString("lastLocationLongkey", location.getLongitude()+"");
         editor.putString("lastLocationLatkey", location.getLatitude()+"");
         editor.commit();
         FIRSTTIME+=1;
         if(FIRSTTIME>1) {
             this.lastlocation = this.location;
             this.location = location;
             double distance = this.location.distanceTo(this.lastlocation);
             long duration = TimeUnit.MILLISECONDS.toSeconds(this.location.getTime() - this.lastlocation.getTime());
             String speed = String.format("%.2f", (distance * 3600) / (duration * 1000));
             //Toast.makeText(mcontext, "Changed....:" + speed + "km/h", Toast.LENGTH_LONG).show();
             TextView speedView = (TextView) activity.findViewById(R.id.speedText);
             speedView.setText("" + speed + " km/h");
         }

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

     @Override
     public void onGpsStatusChanged(int i) {

     }

 }
