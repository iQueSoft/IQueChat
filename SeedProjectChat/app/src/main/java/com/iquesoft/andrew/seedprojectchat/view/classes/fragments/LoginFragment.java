package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.di.components.ILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.LoginFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.UpdateCurentUser;
import com.iquesoft.andrew.seedprojectchat.util.ValidateUtil;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.LoginActivity;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ILoginFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 17.08.2016.
 */

public class LoginFragment extends BaseFragment implements ILoginFragment {
    @Inject
    LoginFragmentPresenter presenter;
    @Inject
    ValidateUtil validateUtil;
    @Inject
    UpdateCurentUser updateCurentUser;

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password_tv)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.remember_password)
    CheckBox rememberPassword;

    private View rootView;
    private LoginActivity loginActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoLogin();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_login, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(ILoginActivityComponent.class).inject(this);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
    }

    @OnClick(R.id.sign_in_button)
    void loginClick(View view){
        attemptLogin();
    }

    @OnClick(R.id.email_register_button)
    void registerClick(View view){
        loginActivity.setRegisterFragment();
    }

    @OnClick(R.id.tv_recovery)
    void recoveryClick(View view){
        showRestorePasswordDialog();
    }

    @Override
    public UpdateCurentUser getUpdateCurentUser(){
        return updateCurentUser;
    }

    private void autoLogin(){
        Thread autoLoginThread = new Thread(() -> {
            Backendless.UserService.isValidLogin(new DefaultBackendlessCallback<Boolean>(getActivityContext())
            {
                @Override
                public void handleResponse( Boolean isValidLogin )
                {
                    if( isValidLogin && Backendless.UserService.CurrentUser() == null )
                    {
                        String currentUserId = Backendless.UserService.loggedInUser();

                        if( !currentUserId.equals( "" ) )
                        {
                            Backendless.UserService.findById( currentUserId, new DefaultBackendlessCallback<BackendlessUser>( getActivityContext())
                            {
                                @Override
                                public void handleResponse( BackendlessUser currentUser )
                                {
                                    super.handleResponse( currentUser );
                                    Thread thread = new Thread(()->{
                                        Backendless.UserService.setCurrentUser( currentUser );
                                        String deviceId = Build.SERIAL;
                                        if( deviceId.isEmpty() )
                                        {
                                            Toast.makeText( getActivity(), "Could not retrieve DEVICE ID", Toast.LENGTH_SHORT ).show();
                                            return;
                                        } else {
                                            currentUser.setProperty(ChatUser.DEVICEID, deviceId);
                                            currentUser.setProperty(ChatUser.ONLINE, true);
                                            updateCurentUser.update(currentUser, getActivity());
                                        }
                                    });
                                    thread.start();
                                    startActivity(new Intent(getActivityContext(), MainActivity.class));
                                    showProgress(false);
                                    loginActivity.finish();
                                }
                            } );
                        }
                    }

                    super.handleResponse( isValidLogin );
                }
            });
        });
        autoLoginThread.start();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !validateUtil.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!validateUtil.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            presenter.onLoginButtonClicked(mEmailView.getText().toString(), mPasswordView.getText().toString(), rememberPassword.isChecked());
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public LoginActivity getLoginActivity() {
        return loginActivity;
    }


    private void showRestorePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_restore_password, null);
        builder.setView(view);
        EditText restorePassword = (EditText) view.findViewById(R.id.email_recovery);
        Button recoveryButton = (Button) view.findViewById(R.id.button_recovery);
        AlertDialog dialog = builder.create();
        recoveryButton.setOnClickListener(view1 -> {
            if (validateUtil.isEmailValid(restorePassword.getText().toString())){
                presenter.onRestorePasswordButtonClicked(restorePassword.getText().toString());
                dialog.cancel();
            } else {
                restorePassword.setError("Incorrect eMail");
                restorePassword.requestFocus();
            }
        });
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }
}
