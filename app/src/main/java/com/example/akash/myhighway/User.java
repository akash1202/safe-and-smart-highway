package com.example.akash.myhighway;

/**
 * Created by vishal on 18/12/17.
 */

public class User {



    private String userId;          //Unique Id as a Key
    private String userName;
    private String userEmail;
    private String userphotoURi;
    private String userMobNumber;
    private String userDevicenumber;
    private double lastLocationLong;
    private double lastLocationLat;

    public String getUserId() {   return userId;    }

    public void setUserId(String userId) {  this.userId = userId;    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserphotoURi() {
        return userphotoURi;
    }

    public void setUserphotoURi(String userphotoURi) {
        this.userphotoURi = userphotoURi;
    }

    public String getUserMobNumber() {
        return userMobNumber;
    }

    public void setUserMobNumber(String userMobNumber) {
        this.userMobNumber = userMobNumber;
    }

    public String getUserDevicenumber() {
        return userDevicenumber;
    }

    public void setUserDevicenumber(String userDevicenumber) { this.userDevicenumber = userDevicenumber;    }

    public double getLastLocationLong() {  return lastLocationLong;   }

    public void setLastLocationLong(double lastLocationLong) {   this.lastLocationLong = lastLocationLong;    }

    public double getLastLocationLat() {    return lastLocationLat;    }

    public void setLastLocationLat(double lastLocationLat) {    this.lastLocationLat = lastLocationLat;    }
}
