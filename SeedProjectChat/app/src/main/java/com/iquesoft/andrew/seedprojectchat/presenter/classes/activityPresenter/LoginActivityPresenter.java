package com.iquesoft.andrew.seedprojectchat.presenter.classes.activityPresenter;

import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activityPsesenter.ILoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activityView.ILoginActivity;

import javax.inject.Inject;

/**
 * Created by Andrew on 16.08.2016.
 */

public class LoginActivityPresenter implements ILoginActivityPresenter{

    private ILoginActivity view;

    @Inject
    public LoginActivityPresenter(ILoginActivity view){
        this.view = view;
    }

    @Override
    public void onBackPressed() {
        view.popFragmentFromStack();
    }


    @Override
    public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
