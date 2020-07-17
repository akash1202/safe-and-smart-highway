package com.example.akash.myhighway1.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DatabaseUtils {
    public static final String CURRENT_VERSION = "current_version";
    public static final String CURRENT_NOTIFICATION_TIME = "current_notification_time";
    public static final String CURRENT_VISIT_TIME = "current_login_time";

    public static void setPref(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val).apply();
    }

    public static int getPref(Context c, String pref, int val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref, val);
    }

    public static void setPref(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val).apply();
    }

    public static long getPref(Context c, String pref, long val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref, val);
    }

}
