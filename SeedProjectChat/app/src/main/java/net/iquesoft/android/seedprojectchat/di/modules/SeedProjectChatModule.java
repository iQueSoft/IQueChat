package net.iquesoft.android.seedprojectchat.di.modules;

import android.app.Application;

import net.iquesoft.android.seedprojectchat.app.SeedProjectChatApp;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.LoginFragmentPresenter;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.RegisterFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.UpdateCurentUser;
import net.iquesoft.android.seedprojectchat.util.ValidateUtil;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.AboutUsFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.ChatWithFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.ContainerFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.FindFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.FriendsFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.GroupChatContainer;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.GroupChatFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.InviteToFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.LoginFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.MainFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.MyInvateFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.RegisterFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.SettingsFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.TermsAndConditionsFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SeedProjectChatModule {

    private final SeedProjectChatApp app;

    public SeedProjectChatModule(SeedProjectChatApp app){
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication(){
        return app;
    }

    @Provides
    @Singleton
    LoginFragmentPresenter provideLoginFragmentPresenter(){
        return new LoginFragmentPresenter();
    }

    @Provides
    @Singleton
    LoginFragment provideLoginFragment(){
        return new LoginFragment();
    }

    @Provides
    @Singleton
    RegisterFragmentPresenter provideRegisterFragmentPresenter(){
        return new RegisterFragmentPresenter();
    }

    @Provides
    @Singleton
    RegisterFragment provideRegisterFragment(){
        return new RegisterFragment();
    }

    @Provides
    @Singleton
    ValidateUtil provideValidateUtil(){
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

    @Provides
    @Singleton
    MainFragment provideMainFragment(){
        return  new MainFragment();
    }
}
