package com.example.akash.myhighway1;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    DatabaseReference databaseReference1;
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
        try {
                if(!isAppIsInBackground(getApplicationContext())) {
                    Log.d("Notification received:","app is not in background");
                    sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getSentTime(), hashMap);
                }
                else{
                        //firebase will handle notification
                    Log.d("Notification received:","app is in background");
                }
            }
        catch (Exception e){
            Log.d("On Message Receive:",e.toString());
        }
        }
        number=1;
    }

    private void sendNotification(String title, String body,Long time, final HashMap<String,String> map) {
//        NotificationCompat notificationCompat=
        try{
        body1=body;
        intent=new Intent();
        Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy HH:mm a");
        date.setTime(time);
        if(map!=null) {
            intent.putExtra("latitude", map.get("latitude"));
            intent.putExtra("longitude", map.get("longitude"));
            intent.putExtra("sender",map.get("sender"));
            intent.putExtra("userimage",map.get("userimage"));
            intent.putExtra("time",date.toString());
            imageurl=map.get("userimage");
            if(imageurl!=null) {
                if (!imageurl.equals(""))
                    intent.putExtra("imageurl",imageurl);
            }
        }
        intent.setClass(getBaseContext(),ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.sshs)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSubText(dateFormat.format(date))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
            /*databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("status","Data changed"+dataSnapshot.getValue().toString());
                    if(dataSnapshot.child(map.get("sender")).child("status").getValue(String.class).equals("solved")){
                        Log.d("status","solved");
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
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
                    .setContentTitle("sshs")
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
            Log.d("notify error notin bg:",e.toString());
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
