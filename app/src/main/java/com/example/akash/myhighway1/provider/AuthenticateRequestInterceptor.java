package com.example.akash.myhighway1.provider;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticateRequestInterceptor implements Interceptor {
    AccountManager accountManager;

    @Inject
    AuthenticateRequestInterceptor(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //provides token in request header  if user loggedin
        if (accountManager.isLoggedIn()) {
            request = request.newBuilder().addHeader("token", accountManager.getUserToken()).build();
        }
        return null;
    }
}
