package com.example.akash.myhighway1;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AppLifeCycleObserver implements Application.ActivityLifecycleCallbacks {

    MyHighwayApp myHighwayApp;
    Context context;
    private final List<Activity> currentActivities = new ArrayList<>();

    @Inject
    AppLifeCycleObserver(Context context) {
        this.context = context;
        myHighwayApp = MyHighwayApp.myHighwayApp(context);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (currentActivities.isEmpty()) {
            onForeGround();
        }
        currentActivities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        currentActivities.remove(activity);
        if (currentActivities.isEmpty()) {
            onBackground();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    private void onForeGround() {
        //accountmanager.setLastestVisitTime(context)
    }

    private void onBackground() {
        //accountManager.setLatestLastVisitTime(context);
    }
}
