package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.iquesoft.andrew.seedprojectchat.model.Messages;

import java.util.List;

/**
 * Created by andru on 9/15/2016.
 */

public interface IChatWithFriendFragment extends MvpView {
    void setUserAdapter(List<Messages> messagesList);
    void updateLastVisibleMessage(Messages message);
}
