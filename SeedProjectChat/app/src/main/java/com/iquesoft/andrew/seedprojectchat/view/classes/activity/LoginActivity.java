package com.iquesoft.andrew.seedprojectchat.view.classes.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseActivity;
import com.iquesoft.andrew.seedprojectchat.di.IHasComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.DaggerILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.di.components.ISeedProjectChatComponent;
import com.iquesoft.andrew.seedprojectchat.di.modules.LoginActivityModule;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.activity.LoginActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.LoginFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.RegisterFragment;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.ILoginActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements ILoginActivity, IHasComponent<ILoginActivityComponent>{

    private ILoginActivityComponent loginActivityComponent;

    @Inject
    LoginActivityPresenter presenter;

    @Inject
    LoginFragment loginFragment;

    @Inject
    RegisterFragment registerFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    // UI references.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        setLoginFragment();
    }

    public void setLoginFragment(){
        replaceFragment(loginFragment, "loginFragment");
    }

    public void setRegisterFragment(){
        replaceFragment(registerFragment, "registerFragment");
    }

    private void replaceFragment(Fragment fragment, String TAG){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, TAG);
        fragmentTransaction.addToBackStack("backpressed stack");
        fragmentTransaction.commit();
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

