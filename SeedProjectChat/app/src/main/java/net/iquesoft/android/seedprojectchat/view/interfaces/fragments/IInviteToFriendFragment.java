package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import net.iquesoft.android.seedprojectchat.model.Friends;

import java.util.List;

public interface IInviteToFriendFragment extends MvpView {
    void setUserAdapter(List<Friends> users);
    void setProgressBarVisible();
    void setProgressBarGone();
}
