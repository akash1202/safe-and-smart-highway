package com.example.akash.myhighway;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Vishal on 12/6/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{
    private static final String TAG="MyFirebaseInstanceID";

    @Override
    public void onTokenRefresh() {
        String refereshedToken= FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("chat");
        Log.d(TAG,"New Token:"+refereshedToken);
    }
}
