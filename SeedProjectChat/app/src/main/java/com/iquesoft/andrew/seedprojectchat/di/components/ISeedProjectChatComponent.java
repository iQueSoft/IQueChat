package com.iquesoft.andrew.seedprojectchat.di.components;

import com.iquesoft.andrew.seedprojectchat.app.SeedProjectChatApp;
import com.iquesoft.andrew.seedprojectchat.di.modules.SeedProjectChatModule;
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
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.MainFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.MyInvateFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.RegisterFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.SettingsFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.TermsAndConditionsFragment;

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

    LoginFragmentPresenter loginFragmentPresenter();
    LoginFragment loginFragment();
    RegisterFragmentPresenter registerFragmentPresenter();
    RegisterFragment registerFragment();
    ValidateUtil validateUtil();
    FriendsFragment provideFriendFragment();
    FindFriendFragment provideFindFriendFragment();
    ConvertBackendlessUserToChatUser provideConvertBackendlessUserToChatUser();
    InviteToFriendFragment provideInviteToFriendsFragment();
    ContainerFriendFragment provideContainerFriendFragment();
    MyInvateFragment provideMyInvateFragment();
    UpdateCurentUser provideUpdateCurentUser();
    ChatWithFriendFragment provideChatWithFriendFragment();
    GroupChatContainer provideGroupChatContainer();
    GroupChatFragment provideGroupChatFragment();
    SettingsFragment provideSettingsFragment();
    TermsAndConditionsFragment provideTermsAndConditionsFragment();
    AboutUsFragment provideAboutUsFragment();
    MainFragment provideMainFragment();
}
