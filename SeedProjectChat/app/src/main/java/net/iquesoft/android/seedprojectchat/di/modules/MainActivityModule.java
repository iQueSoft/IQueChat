package net.iquesoft.android.seedprojectchat.di.modules;

import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.FindFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.GroupChatContainerPresenter;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.GroupChatFragmentPresenter;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.InviteToFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.MyInviteFragmentPresenter;
import net.iquesoft.android.seedprojectchat.view.interfaces.activity.IMainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    IMainActivity view;

    public MainActivityModule(IMainActivity view){
        this.view = view;
    }

    @Provides
    IMainActivity provideMainActivityView(){
        return view;
    }

    @Provides
    FindFriendFragmentPresenter provideFindFriendFragmentPresenter(){
        return new FindFriendFragmentPresenter();
    }

    @Provides
    InviteToFriendFragmentPresenter provideInviteToFriendFragmentPresenter(){
        return new InviteToFriendFragmentPresenter();
    }

    @Provides
    MyInviteFragmentPresenter provideMyInviteFragmentPresenter(){
        return new MyInviteFragmentPresenter();
    }


    @Provides
    GroupChatContainerPresenter provideGroupChatContainerPresenter(){
        return new GroupChatContainerPresenter();
    }

    @Provides
    GroupChatFragmentPresenter provideGroupChatFragmentPresenter(){
        return new GroupChatFragmentPresenter();
    }
}
