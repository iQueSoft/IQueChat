package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.ILoginFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ILoginFragment;

import javax.inject.Inject;

/**
 * Created by Andrew on 17.08.2016.
 */

public class LoginFragmentPresenter implements ILoginFragmentPresenter {
    private ILoginFragment view;

    @Inject
    public LoginFragmentPresenter(){}


    @Override
    public void init(ILoginFragment view) {
        this.view = view;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoginButtonClicked(String userEMail, String password, boolean rememberLogin)
    {
//        String identity = identityField.getText().toString();
//        String password = passwordField.getText().toString();
//        boolean rememberLogin = rememberLoginBox.isChecked();

        Backendless.UserService.login( userEMail, password, new DefaultBackendlessCallback<BackendlessUser>(view.getActivityContext())
        {
            public void handleResponse( BackendlessUser backendlessUser )
            {
                super.handleResponse( backendlessUser );
            }
        }, rememberLogin );
    }

}
