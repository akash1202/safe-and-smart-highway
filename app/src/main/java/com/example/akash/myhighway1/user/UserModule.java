package com.example.akash.myhighway1.user;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    private String sessionId;

    public UserModule(String sessionId) {
        this.sessionId = sessionId;
    }

    @Provides
    @UserScope
    String provideSessionId() {
        return sessionId;
    }


}
