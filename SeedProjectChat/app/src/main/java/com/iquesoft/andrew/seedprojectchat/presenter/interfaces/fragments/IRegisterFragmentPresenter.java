package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import android.graphics.Bitmap;
import android.widget.TextView;

import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IRegisterFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by Andrew on 17.08.2016.
 */

public interface IRegisterFragmentPresenter extends BaseFragmentPresenter<IRegisterFragment> {
    void onRegisterButtonClicked(TextView emailText, TextView nameText, TextView passwordText);
    void uploadUserPhoto(Bitmap bitmap, CircularImageView circleImageView, String userEMail);
}
