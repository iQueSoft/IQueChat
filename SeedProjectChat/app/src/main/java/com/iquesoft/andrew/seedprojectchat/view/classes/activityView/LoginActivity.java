package com.iquesoft.andrew.seedprojectchat.view.classes.activityView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseActivity;
import com.iquesoft.andrew.seedprojectchat.di.IHasComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.DaggerILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ISeedProjectChatComponent;
import com.iquesoft.andrew.seedprojectchat.di.modules.LoginActivityModule;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.activityPresenter.LoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activityView.ILoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginActivity, IHasComponent<ILoginActivityComponent>{

    private ILoginActivityComponent loginActivityComponent;

    @Inject
    LoginActivityPresenter presenter;

    FragmentManager fragmentManager;
    // UI references.
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
//    @BindView(R.id.sign_in_button)
//    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        // Set up the login form.

//        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
//                attemptLogin();
//                return true;
//        });

//        mEmailSignInButton.setOnClickListener(view -> attemptLogin());

    }

    @OnClick(R.id.sign_in_button)
    void loginClick(View view){
        attemptLogin();
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
        if (!TextUtils.isEmpty(password) && !presenter.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!presenter.isEmailValid(email)) {
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
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

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
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public void popFragmentFromStack() {
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            presenter.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void setupComponent(ISeedProjectChatComponent appComponent) {
        loginActivityComponent = DaggerILoginActivityComponent.builder()
                .iSeedProjectChatComponent(appComponent)
                .loginActivityModule(new LoginActivityModule(this))
                .build();
        loginActivityComponent.inject(this);
    }

    @Override
    public ILoginActivityComponent getComponent() {
        return loginActivityComponent;
    }
}

