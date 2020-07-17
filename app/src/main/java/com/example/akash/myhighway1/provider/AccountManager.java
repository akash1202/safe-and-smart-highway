package com.example.akash.myhighway1.provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.akash.myhighway1.ApplicationComponent;
import com.example.akash.myhighway1.MyHighwayApp;
import com.example.akash.myhighway1.data.model.User;
import com.example.akash.myhighway1.user.UserComponent;
import com.google.gson.Gson;

import javax.inject.Inject;

public class AccountManager {

    private static final String IS_LOGGED_IN = "logged_in";
    private static final String IS_FCM_REGISTERED = "fcm_registered";
    private static final String KEY_IS_FCM_REGISTERED = "is_fcm_registered";
    private static final String MY_PREFS_NAME = "mypref";
    private static final String KEY_USER_TOKEN = "user_tooken";
    private static final String USER_INFORMATION = "user_information";

    private SharedPreferences sharedPreferences;
    private Context context;

    @Inject
    public AccountManager(Context context, SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    public void saveUser() {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, true).apply();
        MyHighwayApp.myHighwayApp(context).createUserComponent(getUserToken());
    }

    public void setUserToken(String token) {
        sharedPreferences.edit().putString(KEY_USER_TOKEN, token).apply();
    }

    public String getUserToken() {
        return sharedPreferences.getString(KEY_USER_TOKEN, null);
    }

    public User getUser() {
        String json = sharedPreferences.getString(USER_INFORMATION, null);
        return new Gson().fromJson(json, User.class);
    }


    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
        setFCMRegistered(false);
        UserComponent userComponent = MyHighwayApp.myHighwayApp(context).userComponent();
        ApplicationComponent applicationComponent = MyHighwayApp.myHighwayApp(context).appComponent();
        applicationComponent.databaseHelper().resetTables();
        MyHighwayApp.myHighwayApp(context).releasrUserCOmponent();
    }

    /*
            public void registerFCM() {
            Log.d("--", "registerFCM.. ");
            Observable.create((Subscriber<? super Boolean> subscriber) -> {
                try {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    Response<Object> response = OfferBoxApp.offerApp(context).appComponent().api()
                            .registerPushNotification(new RegisterFCMRequest(context, token));
                    Timber.d("true----->", "" + JsonUtils.toJson(response));
                    subscriber.onNext(response.isFlag());
                } catch (Exception e) {
                    Timber.d("False------->", "" + e.toString());
                    subscriber.onError(e);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(this::setFCMRegistered, Throwable::printStackTrace);
        }
    */
    public void setFCMRegistered(boolean isRegistered) {
        sharedPreferences.edit().putBoolean(KEY_IS_FCM_REGISTERED, isRegistered).apply();
    }

    public boolean isFCMRegistered() {
        return sharedPreferences.getBoolean(KEY_IS_FCM_REGISTERED, false);
    }


}
