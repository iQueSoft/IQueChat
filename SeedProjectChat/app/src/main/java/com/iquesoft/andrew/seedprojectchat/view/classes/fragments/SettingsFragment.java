package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ISettingsFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements ISettingsFragment {

    private View rootView;
    private BackendlessUser curentUser;

    @BindView(R.id.cim_user_image)
    CircularImageView cimUserImage;
    @BindView(R.id.et_user_email)
    EditText etUserEmail;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_repeat_new_password)
    EditText etRepeatNewPassword;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_settings, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        curentUser = Backendless.UserService.CurrentUser();
        Uri uri = Uri.parse(curentUser.getProperty(ChatUser.PHOTO).toString());
        Picasso.with(getActivity()).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(cimUserImage);
        etUserEmail.setText(curentUser.getEmail());
        etUsername.setText(curentUser.getProperty(ChatUser.NAME).toString());
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick(R.id.button_save_user_info)
    public void clickSaveUserInfo(){
        saveUserInfo();
    }

    @OnClick(R.id.button_save_new_pass)
    public void clickSaveNewPass(){
        saveNewPassword();
    }

    public void saveUserInfo(){
        curentUser.setProperty(ChatUser.EMAIL_KEY, etUserEmail.getText().toString());
        curentUser.setProperty(ChatUser.NAME, etUsername.getText().toString());
        Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(), fault.getDetail(), Toast.LENGTH_LONG).show();
                super.handleFault(fault);
            }

            @Override
            public void handleResponse(BackendlessUser backendlessUser) {
                Toast.makeText(getActivity(), "Update sucsesful", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void saveNewPassword(){
        if (etNewPassword.getText().toString().equals(etRepeatNewPassword.getText().toString())){
            curentUser.setProperty(ChatUser.PASSWORD_KEY, etNewPassword.getText().toString());
            Backendless.UserService.update(curentUser, new BackendlessCallback<BackendlessUser>(){
                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(getActivity(), fault.getDetail(), Toast.LENGTH_LONG).show();
                    super.handleFault(fault);
                }

                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Toast.makeText(getActivity(), "Update sucsesful", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            etNewPassword.setError("Passwords do not match");
        }
    }
}
