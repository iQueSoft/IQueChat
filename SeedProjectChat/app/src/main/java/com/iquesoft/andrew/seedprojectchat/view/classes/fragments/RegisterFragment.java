package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.di.components.ILoginActivityComponent;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.RegisterFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.ValidateUtil;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.LoginActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IRegisterFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrew on 17.08.2016.
 */

public class RegisterFragment extends BaseFragment implements IRegisterFragment {

    @Inject
    RegisterFragmentPresenter presenter;

    @Inject
    ValidateUtil validateUtil;

    @BindView(R.id.e_mail_tv)
    TextView eMailTV;
    @BindView(R.id.username_tv)
    TextView usernameTV;
    @BindView(R.id.password_tv)
    TextView passwordTV;

    private View rootView;
    private LoginActivity loginActivity;

    @OnClick(R.id.register_button)
    void registerButtonClick(View view){
        presenter.onRegisterButtonClicked(eMailTV, usernameTV, passwordTV);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_registration, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(ILoginActivityComponent.class).inject(this);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
    }


    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public ValidateUtil getValidateUtil() {
        return validateUtil;
    }

    @Override
    public LoginActivity getLoginActivity() {
        return loginActivity;
    }


}
