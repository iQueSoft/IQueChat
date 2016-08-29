package com.iquesoft.andrew.seedprojectchat.di.components;

import com.iquesoft.andrew.seedprojectchat.di.modules.MainActivityModule;
import com.iquesoft.andrew.seedprojectchat.di.scope.ActivityScope;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FriendsFragment;

import dagger.Component;

/**
 * Created by Andrew on 18.08.2016.
 */
@ActivityScope
@Component(
        dependencies = ISeedProjectChatComponent.class,
        modules = MainActivityModule.class
)
public interface IMainActivityComponent {
    void inject(MainActivity activity);
    void inject(FriendsFragment friendsFragment);
}
