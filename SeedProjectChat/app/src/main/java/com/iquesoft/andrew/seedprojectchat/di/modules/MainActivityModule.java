package com.iquesoft.andrew.seedprojectchat.di.modules;

import com.iquesoft.andrew.seedprojectchat.presenter.classes.activity.MainActivityPresenter;
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
}
