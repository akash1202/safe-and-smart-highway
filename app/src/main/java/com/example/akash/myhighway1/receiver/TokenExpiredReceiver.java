package com.example.akash.myhighway1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.akash.myhighway1.LoginActivity;
import com.example.akash.myhighway1.MyHighwayApp;
import com.example.akash.myhighway1.provider.AccountManager;

public class TokenExpiredReceiver extends BroadcastReceiver {

    public static final String ACTION_TOKEN_EXPIRED = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_TOKEN_EXPIRED)) {
            AccountManager accountManager = MyHighwayApp.myHighwayApp(context).appComponent().accountManager();
            if (accountManager.isLoggedIn()) {
                accountManager.logout();
                //send user to Login Activity if Token Expires
                Intent login = new Intent(context, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(login);
            }
        }
    }
}
