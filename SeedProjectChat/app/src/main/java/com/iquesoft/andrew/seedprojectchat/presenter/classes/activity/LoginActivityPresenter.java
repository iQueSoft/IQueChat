package com.iquesoft.andrew.seedprojectchat.presenter.classes.activity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activity.ILoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.ILoginActivity;

/**
 * Created by Andrew on 16.08.2016.
 */
@InjectViewState
public class LoginActivityPresenter extends MvpPresenter<ILoginActivity> implements ILoginActivityPresenter {

    @Override
    public void onBackPressed() {
        getViewState().popFragmentFromStack();
    }
}
