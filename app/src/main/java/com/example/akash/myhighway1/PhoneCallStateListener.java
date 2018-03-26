package com.example.akash.myhighway1;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by Vishal on 2/15/2018.
 */

public class PhoneCallStateListener extends PhoneStateListener {
    Context context;
    public PhoneCallStateListener(Context context){
        this.context=context;
    }
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(state== TelephonyManager.CALL_STATE_RINGING){

        }
        super.onCallStateChanged(state, incomingNumber);
    }
}
