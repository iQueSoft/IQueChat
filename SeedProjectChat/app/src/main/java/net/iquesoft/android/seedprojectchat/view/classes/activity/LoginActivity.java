package net.iquesoft.android.seedprojectchat.view.classes.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.arellomobile.mvp.presenter.InjectPresenter;

import net.iquesoft.android.seedprojectchat.common.BaseActivity;
import net.iquesoft.android.seedprojectchat.di.IHasComponent;
import net.iquesoft.android.seedprojectchat.di.components.DaggerILoginActivityComponent;
import net.iquesoft.android.seedprojectchat.di.components.ILoginActivityComponent;
import net.iquesoft.android.seedprojectchat.di.components.ISeedProjectChatComponent;
import net.iquesoft.android.seedprojectchat.di.modules.LoginActivityModule;
import net.iquesoft.android.seedprojectchat.presenter.classes.activity.LoginActivityPresenter;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.LoginFragment;
import net.iquesoft.android.seedprojectchat.view.classes.fragments.RegisterFragment;
import net.iquesoft.android.seedprojectchat.view.interfaces.activity.ILoginActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements ILoginActivity, IHasComponent<ILoginActivityComponent>{

    private ILoginActivityComponent loginActivityComponent;

    @InjectPresenter
    LoginActivityPresenter presenter;

    @Inject
    LoginFragment loginFragment;

    @Inject
    RegisterFragment registerFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(net.iquesoft.android.seedprojectchat.R.layout.activity_login);
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
        fragmentTransaction.replace(net.iquesoft.android.seedprojectchat.R.id.container, fragment, TAG);
        fragmentTransaction.addToBackStack("backpressed stack");
        fragmentTransaction.commit();
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

