package net.iquesoft.android.seedprojectchat.di.components;

import net.iquesoft.android.seedprojectchat.adapters.MainFragmentAdapter;
import net.iquesoft.android.seedprojectchat.di.modules.MainActivityModule;
import net.iquesoft.android.seedprojectchat.di.scope.ActivityScope;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.ChatWithFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.ContainerFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.FindFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.FriendsFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.GroupChatContainer;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.GroupChatFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.InviteToFriendFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.MyInvateFragment;

import dagger.Component;

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
