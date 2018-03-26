package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Vishal on 12/6/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG="MyFirebaseInstanceID";
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public void onTokenRefresh() {
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        editor.putString("token",refreshedToken).commit();
        FirebaseMessaging.getInstance().subscribeToTopic("chat");
        Log.d(TAG,"New Token:"+refreshedToken);
    }
}
