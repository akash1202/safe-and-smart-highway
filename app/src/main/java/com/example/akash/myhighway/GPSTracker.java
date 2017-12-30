package com.example.akash.myhighway;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.security.Permission;

/**
 * Created by vishal on 11/12/17.
 */

public class GPSTracker extends Service implements LocationListener
 {
     Context mcontext;
     public boolean isGPSEnabled=false;
     public boolean isNetworkEnabled=false;
     public static boolean cangetLocation=false;
     Location location=null;
     double longitude;
     double latitude;
     private long MIN_DISTANCE=10;  //for 10 meter
     private long MIN_TIME=1000*60*1; //for 1 minute
     LocationManager locationManager;
    public GPSTracker(Context context) {
            this.mcontext=context;
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
                        if (isNetworkEnabled) {
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
                           if (location == null){
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
                    Toast.makeText(this,"provide GPS status access permission for better result",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"provide Network status access permission for better result",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Exception for Location:"+e.getMessage(),Toast.LENGTH_LONG).show();
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
 }
