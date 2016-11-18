package com.iquesoft.andrew.seedprojectchat.di.components;

import com.iquesoft.andrew.seedprojectchat.adapters.MainFragmentAdapter;
import com.iquesoft.andrew.seedprojectchat.di.modules.MainActivityModule;
import com.iquesoft.andrew.seedprojectchat.di.scope.ActivityScope;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ChatWithFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ContainerFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FindFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.FriendsFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.GroupChatContainer;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.GroupChatFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.InviteToFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.MyInvateFragment;

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
    void inject(FindFriendFragment findFriendFragment);
    void inject(InviteToFriendFragment inviteToFriendFragment);
    void inject(ContainerFriendFragment containerFriendFragment);
    void inject(MyInvateFragment myInvateFragment);
    void inject(ChatWithFriendFragment chatWithFriendFragment);
    void inject(GroupChatContainer groupChatContainer);
    void inject(GroupChatFragment groupChatFragment);
    void inject(MainFragmentAdapter mainFragmentAdapter);
}
