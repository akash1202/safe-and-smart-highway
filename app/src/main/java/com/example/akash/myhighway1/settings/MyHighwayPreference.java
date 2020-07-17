package com.example.akash.myhighway1.settings;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyHighwayPreference {
    private final SharedPreferences sharedPreferences;

    @Inject
    public MyHighwayPreference(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }


}
