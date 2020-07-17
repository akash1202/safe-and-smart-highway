package com.example.akash.myhighway1;


import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.example.akash.myhighway1.provider.AccountManager;
import com.example.akash.myhighway1.provider.DatabaseUtils;
import com.example.akash.myhighway1.reset.RetrofitTransformer;
import com.example.akash.myhighway1.user.DaggerUserComponent;
import com.example.akash.myhighway1.user.UserComponent;
import com.example.akash.myhighway1.user.UserModule;
import com.example.akash.myhighway1.utils.FontUtils;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.io.File;

import javax.inject.Inject;

import retrofit2.Retrofit;
import timber.log.Timber;


public class MyHighwayApp extends MultiDexApplication {
    ///FlowManager.init(new FlowConfig.Builder(this).build());
    //FlowManager.init(new FlowConfig.Builder(this).build());

    public static final String defaultFont = "Roboto-Regular.ttf";
    public static final String FONT = defaultFont;
    public static final String FONT_BOLD = defaultFont;
    public static final String FONT_ITALIC = defaultFont;

    public static final String FONT_SEMI_BOLD = defaultFont;
    public static final String FONT_SEMI_LIGHT = defaultFont;

    public static final String HOME_DIR_PATH = Environment.getExternalStorageDirectory().getPath() + "/MyHighway";
    public static final String NO_MEDIA_DIR_PATH = HOME_DIR_PATH + "/.Share";

    private ApplicationComponent appComponent;
    private UserComponent usercomponent;

    @Inject
    AccountManager accountManager;

    @Inject
    AppLifeCycleObserver activityLifeCycleObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        //        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build();
//        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

        appComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
        appComponent.inject(this);

        createHomeDir();
        createNoMediaDir();

        FontUtils.setDefaultFont(this, "DEFAULT", FONT);
        FontUtils.setDefaultFont(this, "DEFAULT_BOLD", FONT);
        FontUtils.setDefaultFont(this, "MONOSPACE", FONT);

        if (accountManager.isLoggedIn()) {
            createUserComponent(accountManager.getUserToken());
        }

        //ImageBindingAdapter.init(this);
        RetrofitTransformer.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
//        else {
//            Timber.plant(new CrashlyticsTree());
//        }

        if (!isLatestVersion()) {
            onVersionUpgrade(getCurrentVersion(), BuildConfig.VERSION_CODE);
            setToLatestVersion();
        }

        registerActivityLifecycleCallbacks(activityLifeCycleObserver);
//        JobManager.create(this).addJobCreator(new SyncJobCreator());
    }

    public static void createHomeDir() {
        new File(HOME_DIR_PATH).mkdirs();
    }

    public static void createNoMediaDir() {
        new File(NO_MEDIA_DIR_PATH).mkdirs();
    }

    public UserComponent createUserComponent(String sessioonId) {
        usercomponent = DaggerUserComponent.builder()
                .applicationComponent(appComponent).userModule(new UserModule(sessioonId)).build();
//        Utils.setCrashlyticsUser(user);
        return usercomponent;
    }

    public ApplicationComponent appComponent() {
        return appComponent;
    }

    public UserComponent userComponent() {
        return usercomponent;
    }

    public void releasrUserCOmponent() {
        usercomponent = null;
    }

    public static MyHighwayApp myHighwayApp(Context context) {
        return (MyHighwayApp) context.getApplicationContext();
    }

    public boolean isLatestVersion() {
        return getCurrentVersion() == BuildConfig.VERSION_CODE;
    }

    public int getCurrentVersion() {   //app updated is checked here using database version
        return DatabaseUtils.getPref(this, DatabaseUtils.CURRENT_VERSION, 0);
    }

    public void setToLatestVersion() {
        DatabaseUtils.setPref(this, DatabaseUtils.CURRENT_VERSION, BuildConfig.VERSION_CODE);
    }

    private void onVersionUpgrade(int oldVersion, int newVersion) {
        AccountManager accountManager = appComponent.accountManager();
        if (accountManager.isLoggedIn()) {
            accountManager.setFCMRegistered(false);
            //accountManager.registerFCM();
        }
    }
}
