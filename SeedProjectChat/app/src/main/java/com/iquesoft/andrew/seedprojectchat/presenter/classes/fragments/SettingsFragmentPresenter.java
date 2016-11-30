package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ISettingsFragment;

/**
 * Created by andru on 10/20/2016.
 */
@InjectViewState
public class SettingsFragmentPresenter extends MvpPresenter<ISettingsFragment> {

    private BackendlessUser curentUser = Backendless.UserService.CurrentUser();

    public BackendlessUser getCurentUser() {
        return curentUser;
    }

    public void saveUserInfo(EditText etUserEmail, EditText etUsername, Context context){
        curentUser.setProperty(ChatUser.EMAIL_KEY, etUserEmail.getText().toString());
        curentUser.setProperty(ChatUser.NAME, etUsername.getText().toString());
        Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, fault.getDetail(), Toast.LENGTH_LONG).show();
                super.handleFault(fault);
            }

            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Toast.makeText(context, "Update sucsesful", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveNewPassword(EditText etNewPassword, EditText etRepeatNewPassword, Context context){
        if (etNewPassword.getText().toString().equals(etRepeatNewPassword.getText().toString())){
            curentUser.setProperty(ChatUser.PASSWORD_KEY, etNewPassword.getText().toString());
            Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(context, fault.getDetail(), Toast.LENGTH_LONG).show();
                    super.handleFault(fault);
                }

                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(context, "Update sucsesful", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            etNewPassword.setError("Passwords do not match");
        }
    }
}
