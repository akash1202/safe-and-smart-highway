package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.akash.myhighway1.provider.AuthenticateRequestInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@Singleton
public class ApplicationModule {
    public static final String MAIN_URL = "https://maps.googleapis.com/maps/";
    final Context appContext;

    public ApplicationModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    SharedPreferences providePrefeerences() {
        return PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Provides
    @Singleton
    Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder().baseUrl(MAIN_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder().serializeSpecialFloatingPointValues();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkClient(AuthenticateRequestInterceptor interceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        return okHttpClient;
    }
}
