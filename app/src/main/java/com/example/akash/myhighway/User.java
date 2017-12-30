package com.example.akash.myhighway;

/**
 * Created by vishal on 18/12/17.
 */

public class User {
    private String userName;
    private String userEmail;
    private String userphotoURi;
    private String userMobNumber;
    private String userDevicenumber;
    private String lastLocation;

    public String getUserMobNumber() {
        return userMobNumber;
    }

    public void setUserMobNumber(String userMobNumber) {
        this.userMobNumber = userMobNumber;
    }

    public String getUserDevicenumber() {
        return userDevicenumber;
    }

    public void setUserDevicenumber(String userDevicenumber) {
        this.userDevicenumber = userDevicenumber;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

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
}
