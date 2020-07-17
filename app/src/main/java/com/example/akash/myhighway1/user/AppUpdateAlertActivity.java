package com.example.akash.myhighway1.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.example.akash.myhighway1.BaseActivity;
import com.example.akash.myhighway1.R;
import com.example.akash.myhighway1.utils.Utils;

public class AppUpdateAlertActivity extends BaseActivity {

    public static final String EXTRA_MESSAGE = "";
    public static final String EXTRA_IS_COMPULSORY = "";

    public boolean isCompulsory;
    public static boolean isActivityVisible = false;

    public static Intent getIntent(Context context, String messageText, boolean isCompulsory) {
        Intent intent = new Intent(context, AppUpdateAlertActivity.class);
        intent.putExtra(EXTRA_MESSAGE, messageText);
        intent.putExtra(EXTRA_IS_COMPULSORY, isCompulsory);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static Intent getIntent(Context context, boolean isCompulsory) {
        Intent intent = new Intent(context, AppUpdateAlertActivity.class);
        intent.putExtra(EXTRA_IS_COMPULSORY, isCompulsory);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActivityVisible = true;
        String message = getIntent().getStringExtra(EXTRA_MESSAGE);
        isCompulsory = getIntent().getBooleanExtra(EXTRA_IS_COMPULSORY, false);
        showAlertDialog(message, isCompulsory);
    }

    private void showAlertDialog(String message, boolean isCompulsory) {
        if (TextUtils.isEmpty(message)) message = getString(R.string.msg_app_update_alert);
        new AlertDialog.Builder(this).setTitle(R.string.title_app_update_alert)
                .setMessage(message)
                .setPositiveButton(R.string.action_update, (dialog, which) -> {
                    Utils.openAppPageInPlayStore(this);
                    finish();
                }).setOnDismissListener(dialog1 -> finish())
                .setCancelable(!isCompulsory)
                .show();
    }

}
