package com.iquesoft.andrew.seedprojectchat.di.modules;

import com.iquesoft.andrew.seedprojectchat.presenter.classes.activityPresenter.LoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activityView.ILoginActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 16.08.2016.
 */

@Module
public class LoginActivityModule {

    ILoginActivity view;

    public LoginActivityModule(ILoginActivity view){
        this.view = view;
    }

    @Provides
    public ILoginActivity provideView(){
        return view;
    }

    @Provides
    public LoginActivityPresenter provideLoginActivityPresenter (ILoginActivity view){
        return new LoginActivityPresenter(view);
    }
}
