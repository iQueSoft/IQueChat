package com.iquesoft.andrew.seedprojectchat.di.components;

import com.iquesoft.andrew.seedprojectchat.app.SeedProjectChatApp;
import com.iquesoft.andrew.seedprojectchat.di.modules.SeedProjectChatModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Andrew on 16.08.2016.
 */
@Singleton
@Component(
        modules = {
                SeedProjectChatModule.class
        }
)
public interface ISeedProjectChatComponent {
    void inject(SeedProjectChatApp app);
}
