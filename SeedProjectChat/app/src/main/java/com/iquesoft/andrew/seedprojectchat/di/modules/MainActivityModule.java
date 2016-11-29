package com.iquesoft.andrew.seedprojectchat.di.modules;

import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.FindFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.GroupChatContainerPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.GroupChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.InviteToFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.MyInviteFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.IMainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 18.08.2016.
 */

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

//    @Provides
//    MainActivityPresenter provideMainActivityPresenter(IMainActivity view){
//        return new MainActivityPresenter(view);
//    }

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
