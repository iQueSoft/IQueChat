package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import net.iquesoft.android.seedprojectchat.model.Messages;

import java.util.List;

public interface IGroupChatFragment extends MvpView {
    void showNewMessage(Messages messages);
    void setMessageAdapter(List<Messages> messages);
}
