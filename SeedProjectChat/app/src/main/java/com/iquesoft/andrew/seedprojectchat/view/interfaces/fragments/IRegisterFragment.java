package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import android.content.Context;

import com.iquesoft.andrew.seedprojectchat.util.ValidateUtil;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.LoginActivity;

/**
 * Created by Andrew on 17.08.2016.
 */

public interface IRegisterFragment {
    Context getActivityContext();
    ValidateUtil getValidateUtil();
    LoginActivity getLoginActivity();
}
