package com.example.akash.myhighway1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vishal on 12/6/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private final String TAG="FirebaseMessaging";
    private int number=0,CHANEL_ID=15;
    private Map<String,String> map;
    private HashMap<String,String> hashMap;
    NotificationCompat.Builder notBuilder;
    String imageurl="";
    Intent intent;
    String body1="";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    Log.d(TAG,"From:"+remoteMessage.getFrom());
    map=null;
    if(remoteMessage.getData().size()>0){
        Log.d(TAG,"Data:"+remoteMessage.getData());
    }
    if(remoteMessage.getNotification()!=null){
        try {
            map=remoteMessage.getData();
            Log.d("data:",map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"Body"+remoteMessage.getNotification().getBody());
        hashMap = new HashMap<String, String>(map);
      //  Toast.makeText(this,remoteMessage.getNotification().getBody()+"",Toast.LENGTH_LONG).show();
        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),hashMap);
        }
        number=1;
    }

    private void sendNotification(String title,String body,HashMap<String,String> map) {
//        NotificationCompat notificationCompat=
        try{
        body1=body;
        intent=new Intent();
        if(map!=null) {
            intent.putExtra("latitude", map.get("latitude"));
            intent.putExtra("longitude", map.get("longitude"));
            imageurl=map.get("userimage");
            if(imageurl!=null) {
                if (!imageurl.equals(""))
                    intent.putExtra("imageurl",imageurl);
            }
        }
        intent.setClass(getBaseContext(),chatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        //PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_ONE_SHOT);
        /*TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri soundOfNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notBuilder = new NotificationCompat.Builder(this)
                    //.setSmallIcon(R.mipmap.ic_launcher_round)//.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.final_icon))
                    .setAutoCancel(false)
                    .setColor(ContextCompat.getColor(this, R.color.colorWhatsapp))
                    .setContentTitle("SSHS")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(soundOfNotification)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body));

       /*  if(imageurl!=null){
        ImageLoader imgLoader=ImageLoader.getInstance();
        imgLoader.loadImage(imageurl,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //super.onLoadingComplete(imageUri, view, loadedImage);
                notBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(loadedImage).setSummaryText(body1));
            }
        });
         }
            //NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(0,notBuilder.build());
            notificationManagerCompat.notify(0, notBuilder.build());*/
        }catch (Exception e){
            Log.d("akashnoti",e.toString());
        }
    }
}
