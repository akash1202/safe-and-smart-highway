package com.example.akash.myhighway1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

/**
 * Created by Vishal on 2/15/2018.
 */

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephony= (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        PhoneCallStateListener stateListener =new PhoneCallStateListener(context){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
            }
        };
    }
}
