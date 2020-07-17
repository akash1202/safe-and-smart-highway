package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.akash.myhighway1.provider.AccountManager;
import com.example.akash.myhighway1.provider.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    Context context();

    Api api();

    SharedPreferences sharedpreferences();

    AccountManager accountManager();

    DatabaseHelper databaseHelper();

    void inject(MyHighwayApp myHighwayApp);
}
