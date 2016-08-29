package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Intent;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.ILoginFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
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
    public void onLoginButtonClicked(String userEMail, String password, boolean rememberLogin)
    {

        Backendless.UserService.login( userEMail, password, new DefaultBackendlessCallback<BackendlessUser>(view.getActivityContext())
        {
            public void handleResponse( BackendlessUser backendlessUser )
            {
                super.handleResponse( backendlessUser );
                view.getActivityContext().startActivity(new Intent(view.getActivityContext(), MainActivity.class));
                view.showProgress(false);
                view.getLoginActivity().finish();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
                view.showProgress(false);
            }
        }, rememberLogin );
    }

    @Override
    public void onRestorePasswordButtonClicked(String eMail)
    {
        Backendless.UserService.restorePassword( eMail, new DefaultBackendlessCallback<Void>(view.getActivityContext())
        {
            @Override
            public void handleResponse( Void response )
            {
                super.handleResponse( response );
                Toast.makeText(view.getActivityContext(), "Please check you eMail", Toast.LENGTH_LONG).show();
            }
        } );
    }
}
