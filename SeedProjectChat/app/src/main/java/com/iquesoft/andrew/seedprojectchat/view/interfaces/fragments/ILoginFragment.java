package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.backendless.BackendlessUser;

/**
 * Created by Andrew on 17.08.2016.
 */

public interface ILoginFragment extends MvpView {
    void showProgress(final boolean show);
    void setCurentUser(BackendlessUser loggedInUser);
}
