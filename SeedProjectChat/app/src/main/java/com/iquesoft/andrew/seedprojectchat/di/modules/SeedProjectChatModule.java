package com.iquesoft.andrew.seedprojectchat.di.modules;

import android.app.Application;

import com.iquesoft.andrew.seedprojectchat.app.SeedProjectChatApp;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.LoginFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.RegisterFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.ConvertBackendlessUserToChatUser;
import com.iquesoft.andrew.seedprojectchat.util.UpdateCurentUser;
import com.iquesoft.andrew.seedprojectchat.util.ValidateUtil;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.AboutUsFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ChatWithFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ContainerFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FindFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FriendsFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.GroupChatContainer;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.GroupChatFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.InviteToFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.LoginFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.MyInvateFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.RegisterFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.SettingsFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.TermsAndConditionsFragment;

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

    @Provides
    @Singleton
    public LoginFragmentPresenter provideLoginFragmentPresenter(){
        return new LoginFragmentPresenter();
    }

    @Provides
    @Singleton
    public LoginFragment provideLoginFragment(){
        return new LoginFragment();
    }

    @Provides
    @Singleton
    public RegisterFragmentPresenter provideRegisterFragmentPresenter(){
        return new RegisterFragmentPresenter();
    }

    @Provides
    @Singleton
    public RegisterFragment provideRegisterFragment(){
        return new RegisterFragment();
    }

    @Provides
    @Singleton
    public ValidateUtil provideValidateUtil(){
        return new ValidateUtil();
    }

    @Provides
    @Singleton
    FriendsFragment provideFriendFragment(){
        return new FriendsFragment();
    }

    @Provides
    @Singleton
    FindFriendFragment provideFindFriendFragment(){
        return new FindFriendFragment();
    }

    @Provides
    @Singleton
    ConvertBackendlessUserToChatUser provideConvertBackendlessUserToChatUser(){
        return new ConvertBackendlessUserToChatUser();
    }

    @Provides
    @Singleton
    InviteToFriendFragment provideInviteToFriendsFragment(){
        return new InviteToFriendFragment();
    }

    @Provides
    @Singleton
    ContainerFriendFragment provideContainerFriendFragment(){
        return new ContainerFriendFragment();
    }

    @Provides
    @Singleton
    MyInvateFragment provideMyInvateFragment(){
        return new MyInvateFragment();
    }

    @Provides
    @Singleton
    UpdateCurentUser provideUpdateCurentUser(){
        return new UpdateCurentUser();
    }

    @Provides
    ChatWithFriendFragment provideChatWithFriendFragment(){
        return new ChatWithFriendFragment();
    }

    @Provides
    @Singleton
    GroupChatContainer provideGroupChatContainer(){
        return new GroupChatContainer();
    }

    @Provides
    GroupChatFragment provideGroupChatFragment(){
        return new GroupChatFragment();
    }

    @Provides
    @Singleton
    SettingsFragment provideSettingsFragment(){
        return new SettingsFragment();
    }

    @Provides
    @Singleton
    TermsAndConditionsFragment provideTermsAndConditionsFragment(){
        return new TermsAndConditionsFragment();
    }

    @Provides
    @Singleton
    AboutUsFragment provideAboutUsFragment(){
        return new AboutUsFragment();
    }

}
