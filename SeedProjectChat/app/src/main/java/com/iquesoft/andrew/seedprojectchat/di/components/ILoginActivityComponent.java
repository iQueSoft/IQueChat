package com.iquesoft.andrew.seedprojectchat.di.components;

import com.iquesoft.andrew.seedprojectchat.di.modules.LoginActivityModule;
import com.iquesoft.andrew.seedprojectchat.di.scope.ActivityScope;
import com.iquesoft.andrew.seedprojectchat.view.classes.activityView.LoginActivity;

import dagger.Component;

/**
 * Created by Andrew on 16.08.2016.
 */
@ActivityScope
@Component(
        dependencies = ISeedProjectChatComponent.class,
        modules = LoginActivityModule.class
)
public interface ILoginActivityComponent {
    void inject(LoginActivity activity);
}
