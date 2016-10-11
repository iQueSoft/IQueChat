package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by andru on 9/15/2016.
 */

public interface IChatWithFriendFragmentPresenter {
    boolean onSendMessage(EditText messageField, Context context);
}
