package com.example.akash.myhighway1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Vishal on 12/6/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private final String TAG="FirebaseMessaging";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG,"From:"+remoteMessage.getFrom());
    if(remoteMessage.getData().size()>0){
        Log.d(TAG,"Data:"+remoteMessage.getData());
    }
    if(remoteMessage.getNotification()!=null){
        Log.d(TAG,"Body"+remoteMessage.getNotification().getBody());
        sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String body) {
//        NotificationCompat notificationCompat=
        Intent intent=new Intent(this,testIt.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri soundOfNotification= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notBuilder= new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setColor(getColor(R.color.colorPrimary))
                .setContentTitle("Message From Admin")
                .setContentText(body)
                .setSound(soundOfNotification)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notBuilder.build());
    }
}
