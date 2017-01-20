package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import net.iquesoft.android.seedprojectchat.model.Messages;

import java.util.List;

public interface IChatWithFriendFragment extends MvpView {
    void setUserAdapter(List<Messages> messagesList);
    void updateLastVisibleMessage(Messages message);
}
