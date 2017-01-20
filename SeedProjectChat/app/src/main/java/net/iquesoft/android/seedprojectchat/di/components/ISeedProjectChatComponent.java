package net.iquesoft.android.seedprojectchat.di.components;

import net.iquesoft.android.seedprojectchat.app.SeedProjectChatApp;
import net.iquesoft.android.seedprojectchat.di.modules.SeedProjectChatModule;
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

import dagger.Component;

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
