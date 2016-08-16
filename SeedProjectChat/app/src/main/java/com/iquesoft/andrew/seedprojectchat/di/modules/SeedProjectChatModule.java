package com.iquesoft.andrew.seedprojectchat.di.modules;

import android.app.Application;

import com.iquesoft.andrew.seedprojectchat.app.SeedProjectChatApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 16.08.2016.
 */
@Module
public class SeedProjectChatModule {

    private final SeedProjectChatApp app;

    public SeedProjectChatModule(SeedProjectChatApp app){
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return app;
    }
}
