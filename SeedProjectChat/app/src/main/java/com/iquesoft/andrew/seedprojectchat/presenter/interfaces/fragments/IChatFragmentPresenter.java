package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import android.widget.EditText;

import com.backendless.messaging.Message;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatFragment;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by andru on 9/7/2016.
 */

public interface IChatFragmentPresenter extends BaseFragmentPresenter<IChatFragment> {
    PublishSubject<List<Message>> onReceiveMessage();
    boolean onSendMessage(EditText messageField);
    PublishSubject<List<Messages>> getMessageList();
}
