package com.example.akash.myhighway1.reset;

import android.content.Context;
import android.content.Intent;

import com.example.akash.myhighway1.R;
import com.example.akash.myhighway1.receiver.TokenExpiredReceiver;
import com.example.akash.myhighway1.user.AppUpdateAlertActivity;

import java.io.IOException;

import javax.net.ssl.SSLPeerUnverifiedException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RetrofitTransformer<T> implements ObservableTransformer<T, T> {
    private static Context appContext;
    private static String networkError, serverError;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
        networkError = context.getString(R.string.msg_network_error);
        serverError = context.getString(R.string.msg_server_error);
    }

    public void checkInitialization() {
        if (appContext == null) {
            throw new RuntimeException("RetrofitTransformer is not Initialized");
        }
    }

    public RetrofitTransformer() {
        checkInitialization();
    }

    public String getNetworkError() {
        return networkError;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        try {

            return upstream.onErrorResumeNext(throwable -> {
                return Observable.create(subscriber -> {
                    String errorMessage;
                    if (throwable instanceof SSLPeerUnverifiedException) {
                        errorMessage = serverError;
                    } else if (throwable instanceof IOException) {
                        errorMessage = throwable.getLocalizedMessage();
                    } else {
                        errorMessage = throwable.getLocalizedMessage();
                        Timber.e(throwable, "server error");
                    }
                    subscriber.onError(new RuntimeException(errorMessage, throwable));
                });
            }).doOnNext(t -> {
                if (t instanceof RestResponse<?>) {
                    RestResponse<?> restResponse = (RestResponse<?>) t;
                    if (!restResponse.isFlag()) {
                        if (restResponse.tokenExpired()) {
                            appContext.sendBroadcast(new Intent(TokenExpiredReceiver.ACTION_TOKEN_EXPIRED));
                        } else if (restResponse.updateAPK()) {
                            if (!AppUpdateAlertActivity.isActivityVisible) {
                                appContext.startActivity(AppUpdateAlertActivity.getIntent(appContext, restResponse.getMessage(), true));
                            }
                            throw new RuntimeException("Update Available");
                        }
                        throw new RuntimeException(restResponse.getMessage());
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
