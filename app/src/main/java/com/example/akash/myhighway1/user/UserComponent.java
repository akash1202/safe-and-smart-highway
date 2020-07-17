package com.example.akash.myhighway1.user;

import com.example.akash.myhighway1.ApplicationComponent;
import com.example.akash.myhighway1.provider.AccountManager;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;

@UserScope
@Component(dependencies = {ApplicationComponent.class}, modules = {UserModule.class})
public interface UserComponent {
    AccountManager accountManager();
}
