package com.iquesoft.andrew.seedprojectchat.di.modules;

import com.iquesoft.andrew.seedprojectchat.presenter.classes.activity.MainActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.FindFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.FriendsFragmentPresenter;
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

    @Provides
    MainActivityPresenter provideMainActivityPresenter(IMainActivity view){
        return new MainActivityPresenter(view);
    }

    @Provides
    FriendsFragmentPresenter provideFriendsFragmentPresenter(){
        return new FriendsFragmentPresenter();
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
}
