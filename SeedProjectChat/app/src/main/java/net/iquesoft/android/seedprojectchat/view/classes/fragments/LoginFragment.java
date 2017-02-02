package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.di.components.ILoginActivityComponent;
import net.iquesoft.android.seedprojectchat.model.ChatUser;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.LoginFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.AnswersEvents;
import net.iquesoft.android.seedprojectchat.util.UpdateCurentUser;
import net.iquesoft.android.seedprojectchat.util.ValidateUtil;
import net.iquesoft.android.seedprojectchat.view.classes.activity.LoginActivity;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.ILoginFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements ILoginFragment, GoogleApiClient.OnConnectionFailedListener {
    @InjectPresenter
    LoginFragmentPresenter presenter;
    @Inject
    ValidateUtil validateUtil;
    @Inject
    UpdateCurentUser updateCurentUser;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.password_tv)
    EditText mPasswordView;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.login_progress)
    View mProgressView;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.login_form)
    View mLoginFormView;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.remember_password)
    CheckBox rememberPassword;

    private View rootView;
    private LoginActivity loginActivity;
    private GoogleSignInOptions gSignInOptions;
    private GoogleApiClient gApiClient;
    private final int SIGN_IN_INTENT_ID = 1;
    private String TAG = "google+";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gSignInOptions = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestEmail().requestProfile().requestId().requestIdToken( "102599222640-d9omj04c5tvnhgdk72n75de38t1epegt.apps.googleusercontent.com" )
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        GoogleApiClient.Builder apiCliBuilder = new GoogleApiClient.Builder( getActivity() );
        gApiClient = apiCliBuilder
                .enableAutoManage( getActivity(), this )
                .addApi( Auth.GOOGLE_SIGN_IN_API, gSignInOptions ).build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_login, container, false);
            ButterKnife.bind(this, rootView);
        }
        presenter.autoLogin();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(ILoginActivityComponent.class).inject(this);
        loginActivity = (LoginActivity) getActivity();
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.sign_in_button)
    void loginClick(){
        attemptLogin();
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.email_register_button)
    void registerClick(){
        loginActivity.setRegisterFragment();
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.tv_recovery)
    void recoveryClick(){
        showRestorePasswordDialog();
    }


    @OnClick(net.iquesoft.android.seedprojectchat.R.id.but_facebook)
    public void loginWithFacebook(){
        presenter.loginWithFacebook((LoginActivity) getActivity());
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.but_twitter)
    public void loginWithTwitter(){
        presenter.loginWithTwitter((LoginActivity) getActivity(), rememberPassword.isChecked());
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.but_google)
    public void googleLogin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent( gApiClient );
        this.startActivityForResult( signInIntent, SIGN_IN_INTENT_ID );
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if( requestCode == SIGN_IN_INTENT_ID)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess())
            {
                AnswersEvents.getInstance().login("gmail");
                presenter.loginInBackendless( result.getSignInAccount(), (LoginActivity) getActivity() , rememberPassword.isChecked());
            }
            else
            {

            }
        }
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
            mPasswordView.setError(getString(net.iquesoft.android.seedprojectchat.R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(net.iquesoft.android.seedprojectchat.R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!validateUtil.isEmailValid(email)) {
            mEmailView.setError(getString(net.iquesoft.android.seedprojectchat.R.string.error_invalid_email));
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
            presenter.onLoginButtonClicked(mEmailView.getText().toString(), mPasswordView.getText().toString(), rememberPassword.isChecked(), (LoginActivity) getActivity(), updateCurentUser);
        }
    }

    public void setCurentUser(BackendlessUser loggedInUser){
        Thread thread = new Thread(()->{
            Backendless.UserService.setCurrentUser(loggedInUser);
            String deviceId = Build.SERIAL;
            if( deviceId.isEmpty() )
            {
                Toast.makeText( getActivity(), "Could not retrieve DEVICE ID", Toast.LENGTH_SHORT ).show();
                return;
            } else {
                loggedInUser.setProperty(ChatUser.DEVICEID, deviceId);
                loggedInUser.setProperty(ChatUser.ONLINE, true);
                updateCurentUser.update(loggedInUser);
            }
        });
        thread.start();
        startActivity(new Intent(getActivity(), MainActivity.class));
        showProgress(false);
        loginActivity.finish();
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

    private void showRestorePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_restore_password, null);
        builder.setView(view);
        EditText restorePassword = (EditText) view.findViewById(net.iquesoft.android.seedprojectchat.R.id.email_recovery);
        Button recoveryButton = (Button) view.findViewById(net.iquesoft.android.seedprojectchat.R.id.button_recovery);
        AlertDialog dialog = builder.create();
        recoveryButton.setOnClickListener(view1 -> {
            if (validateUtil.isEmailValid(restorePassword.getText().toString())){
                presenter.onRestorePasswordButtonClicked(restorePassword.getText().toString(), getActivity());
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.toString());
    }
}
