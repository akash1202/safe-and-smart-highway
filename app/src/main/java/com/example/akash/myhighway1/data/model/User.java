package com.example.akash.myhighway1.data.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vishal on 18/12/17.
 */

public class User {
    private String userId;          //Unique Id as a Key
    private String userName;
    private String userEmail;
    private String userPhotoURi;
    private String userMobNumber;
    private String userDeviceNumber;
    private double lastLocationLong;
    private double lastLocationLat;
    SharedPreferences sharedPreferences;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences.Editor editor;
    Context context;

    public User(Context context){
        this.context=context;
        sharedPreferences=this.context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        this.userId=sharedPreferences.getString("userIdkey","");
        this.userName=sharedPreferences.getString("userNamekey","");
        this.userEmail=sharedPreferences.getString("userEmailkey","");
        this.userPhotoURi=sharedPreferences.getString("userPhotoURikey","");
        this.userMobNumber=sharedPreferences.getString("userMobNumberkey","");
        this.userDeviceNumber=sharedPreferences.getString("userDeviceNumberkey","");
        this.lastLocationLong=Double.parseDouble(sharedPreferences.getString("lastLocationLongkey","0"));
        this.lastLocationLat=Double.parseDouble(sharedPreferences.getString("lastLocationLatkey","0"));
    }
    public User(Context context,String userId,String userName,String userEmail,String userPhotoURi,String userMobNumber,String userDeviceNumber,double lastLocationLong,double lastLocationLat){
        this.context=context;
        this.userId=userId;
        this.userName=userName;
        this.userEmail=userEmail;
        this.userPhotoURi=userPhotoURi;
        this.userMobNumber=userMobNumber;
        this.userDeviceNumber=userDeviceNumber;
        this.lastLocationLong=lastLocationLong;
        this.lastLocationLat=lastLocationLat;
        sharedPreferences=this.context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public String getUserId() {
        this.userId=sharedPreferences.getString("userIdkey","");
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        editor.putString("userIdkey",userId);
        editor.commit();
    }

    public String getUserName() {
        this.userName=sharedPreferences.getString("userNamekey","");
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        editor.putString("userNamekey",userName);
        editor.commit();
    }

    public String getUserEmail() {
        this.userEmail=sharedPreferences.getString("userEmailkey","");
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        editor.putString("userEmailkey",userEmail);
        editor.commit();
    }

    public String getUserPhotoURi() {
        this.userPhotoURi=sharedPreferences.getString("userPhotoURikey","");
        return userPhotoURi;
    }

    public void setUserPhotoURi(String userPhotoURi) {
        this.userPhotoURi = userPhotoURi;
        editor.putString("userPhotoURikey",userPhotoURi);
        editor.commit();
    }

    public String getUserMobNumber() {
        this.userMobNumber=sharedPreferences.getString("userMobNumberkey","");
        return userMobNumber;
    }

    public void setUserMobNumber(String userMobNumber) {
        this.userMobNumber = userMobNumber;
        editor.putString("userMobNumberkey",userMobNumber);
        editor.commit();
    }

    public String getUserDeviceNumber() {
        this.userDeviceNumber=sharedPreferences.getString("userDeviceNumberkey","");
        return userDeviceNumber;
    }

    public void setUserDeviceNumber(String userDeviceNumber) { 
        this.userDeviceNumber = userDeviceNumber;
        editor.putString("userDeviceNumberkey",userDeviceNumber);
        editor.commit();
    }

    public double getLastLocationLong() {
        this.lastLocationLong=Double.parseDouble(sharedPreferences.getString("lastLocationLongkey","0"));
        return lastLocationLong;
    }

    public void setLastLocationLong(double lastLocationLong) {
        this.lastLocationLong = lastLocationLong;
        editor.putString("lastLocationLongkey", String.valueOf(lastLocationLong));
        editor.commit();
    }

    public double getLastLocationLat() {
        this.lastLocationLat=Double.parseDouble(sharedPreferences.getString("lastLocationLatkey","0"));
        return lastLocationLat;
    }

    public void setLastLocationLat(double lastLocationLat) {
        this.lastLocationLat = lastLocationLat;
        editor.putString("lastLocationLatkey", String.valueOf(lastLocationLat));
        editor.commit();
    }
}
