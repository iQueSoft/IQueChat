package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ILoginFragment;

/**
 * Created by Andrew on 17.08.2016.
 */

public interface ILoginFragmentPresenter extends BaseFragmentPresenter<ILoginFragment> {
    void onCreate();
    void onLoginButtonClicked(String userEMail, String password, boolean rememberLogin);
}
