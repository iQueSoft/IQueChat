package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IRegisterFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IRegisterFragment;

import javax.inject.Inject;

/**
 * Created by Andrew on 17.08.2016.
 */

public class RegisterFragmentPresenter implements IRegisterFragmentPresenter {

    private IRegisterFragment view;
    private ChatUser chatUser;


    @Inject
    public RegisterFragmentPresenter(){

    }

    @Override
    public void init(IRegisterFragment view) {
        this.view = view;
    }

    public void onRegisterButtonClicked(TextView emailText, TextView nameText, TextView passwordText)
    {
        String email = null, name = null, password = null;

        if( !emailText.getText().toString().isEmpty() & view.getValidateUtil().isEmailValid(emailText.getText().toString()) )
        {
            email = emailText.getText().toString();
        } else {
            emailText.setError("Field 'email' cannot be empty or invalid email format");
        }

        if( !nameText.getText().toString().isEmpty())
        {
            name = nameText.getText().toString();
        } else {
            nameText.setError("Field 'Username' cannot be empty");
        }

        if( !passwordText.getText().toString().isEmpty() & view.getValidateUtil().isPasswordValid(passwordText.getText().toString()))
        {
            password = passwordText.getText().toString();
        } else {
            passwordText.setError("Field 'password' cannot be empty or contain less 4 characters");
        }

        chatUser = new ChatUser();

        if( email != null )
        {
            chatUser.setEmail( email );
        }

        if( name != null )
        {
            chatUser.setName( name );
        }

        if( password != null )
        {
            chatUser.setPassword( password );
        }

        Backendless.UserService.register( chatUser, new DefaultBackendlessCallback<BackendlessUser>(view.getActivityContext())
        {
            @Override
            public void handleResponse( BackendlessUser response )
            {
                super.handleResponse( response );
                Log.i("response", response.toString());
                showToast("You sucsesfull registred");
                view.getLoginActivity().setLoginFragment();
            }
        } );
    }

    private void showToast( String msg )
    {
        Toast.makeText( view.getActivityContext(), msg, Toast.LENGTH_LONG ).show();
    }
}
