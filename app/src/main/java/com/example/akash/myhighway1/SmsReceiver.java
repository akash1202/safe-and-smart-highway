package com.example.akash.myhighway1;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vishal on 1/8/2018.
 */

public class SmsReceiver extends BroadcastReceiver {
    SharedPreferences sharedPreferences;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences.Editor editor;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        SmsMessage[] messages=null;
        String content="";
        if(bundle!=null){
            Object[] pdus=(Object[]) bundle.get("pdus");
            messages=new SmsMessage[pdus.length];
            for (int i=0;i<messages.length;i++){
                messages[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
                String sender=messages[i].getOriginatingAddress();
                content+="\rFrom:"+sender+"\nMessage:";
                String body=messages[i].getMessageBody().toString();
                content+=body+"\r";
                Log.d("Message:",content);
                Toast.makeText(context,""+content,Toast.LENGTH_LONG).show();
                sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
                editor=sharedPreferences.edit();
                String rec=sharedPreferences.getString("forwardTo","");
                if(!rec.equals(""))
                sendSMS(context,rec,body);
            }
        }
    }
    public void sendSMS(Context context,String to,String content){
        PendingIntent sentIntent=PendingIntent.getBroadcast(context,0,new Intent("SMS_SENT"),0);
        PendingIntent deliveryIntent=PendingIntent.getBroadcast(context,0,new Intent("SMS_DELIVERED"),0);
        SmsManager manager=SmsManager.getDefault();
        manager.sendTextMessage(to,null,content,null,null);
    }
}
