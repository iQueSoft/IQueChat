package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.backendless.Backendless;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.SettingsFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.ISettingsFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.FileUtil;

import static android.app.Activity.RESULT_OK;
import static com.iquesoft.andrew.seedprojectchat.view.classes.fragments.RegisterFragment.GALLERY_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements ISettingsFragment {

    @InjectPresenter
    SettingsFragmentPresenter presenter;

    private View rootView;

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
        try {
            Uri uri = Uri.parse(presenter.getCurentUser().getProperty(ChatUser.PHOTO).toString());
            Picasso.with(getActivity()).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(cimUserImage);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        etUserEmail.setText(presenter.getCurentUser().getEmail());
        etUsername.setText(presenter.getCurentUser().getProperty(ChatUser.NAME).toString());
        super.onActivityCreated(savedInstanceState);
    }

    public void photoSelector() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        File image = null;
        try {
            image = FileUtil.from(getActivity(), imageReturnedIntent.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    presenter.uploadUserPhoto(image, cimUserImage, Backendless.UserService.CurrentUser().getEmail(), getActivity());
                }
        }
    }

    @OnClick(R.id.cim_user_image)
    public void clickChangeUserPhoto(){
        photoSelector();
    }

    @OnClick(R.id.button_save_user_info)
    public void clickSaveUserInfo(){
        presenter.saveUserInfo(etUserEmail, etUsername, getActivity());
    }

    @OnClick(R.id.button_save_new_pass)
    public void clickSaveNewPass(){
        presenter.saveNewPassword(etNewPassword, etRepeatNewPassword, getActivity());
    }


}
