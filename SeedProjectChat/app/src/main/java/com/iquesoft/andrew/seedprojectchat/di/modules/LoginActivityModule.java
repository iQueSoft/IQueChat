package com.iquesoft.andrew.seedprojectchat.di.modules;

import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.ILoginActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 16.08.2016.
 */

@Module
public class LoginActivityModule {

    ILoginActivity view;

    public LoginActivityModule(ILoginActivity view){
        this.view = view;
    }

    @Provides
    ILoginActivity provideView(){
        return view;
    }
}
