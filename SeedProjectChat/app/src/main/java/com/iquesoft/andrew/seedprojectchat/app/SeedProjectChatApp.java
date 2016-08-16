package com.iquesoft.andrew.seedprojectchat.app;

import android.app.Application;
import android.content.Context;

import com.iquesoft.andrew.seedprojectchat.di.components.DaggerISeedProjectChatComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ISeedProjectChatComponent;
import com.iquesoft.andrew.seedprojectchat.di.modules.SeedProjectChatModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andrew on 16.08.2016.
 */

public class SeedProjectChatApp extends Application {

    private ISeedProjectChatComponent appComponent;

    public static SeedProjectChatApp get(Context context){
        return (SeedProjectChatApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        buildGraphAndInject();
    }

    public ISeedProjectChatComponent getAppComponent(){
        return appComponent;
    }

    public void buildGraphAndInject() {
        appComponent = DaggerISeedProjectChatComponent.builder()
                .seedProjectChatModule(new SeedProjectChatModule(this))
                .build();
        appComponent.inject(this);
    }
}
