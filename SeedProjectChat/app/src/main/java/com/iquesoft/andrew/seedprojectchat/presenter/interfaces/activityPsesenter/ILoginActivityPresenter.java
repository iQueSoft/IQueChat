package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activityPsesenter;

/**
 * Created by Andrew on 16.08.2016.
 */

public interface ILoginActivityPresenter {
    boolean isPasswordValid(String password);
    boolean isEmailValid(String email);
    void onBackPressed();
}
