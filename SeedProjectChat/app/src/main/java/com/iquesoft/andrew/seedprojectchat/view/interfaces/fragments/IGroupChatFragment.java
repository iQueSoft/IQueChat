package com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.iquesoft.andrew.seedprojectchat.model.Messages;

import java.util.List;

/**
 * Created by andru on 9/23/2016.
 */

public interface IGroupChatFragment extends MvpView {
    void showNewMessage(Messages messages);
    void setMessageAdapter(List<Messages> messages);
}
