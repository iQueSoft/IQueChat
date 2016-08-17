package com.iquesoft.andrew.seedprojectchat.presenter.classes.activity;

import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activity.ILoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.ILoginActivity;

import javax.inject.Inject;

/**
 * Created by Andrew on 16.08.2016.
 */

public class LoginActivityPresenter implements ILoginActivityPresenter {

    private ILoginActivity view;

    @Inject
    public LoginActivityPresenter(ILoginActivity view){
        this.view = view;
    }

    @Override
    public void onBackPressed() {
        view.popFragmentFromStack();
    }
}
