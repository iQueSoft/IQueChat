package net.iquesoft.android.seedprojectchat.view.interfaces.fragments;

import com.arellomobile.mvp.MvpView;
import com.backendless.BackendlessUser;

import java.util.List;

public interface IFindFriendFragment extends MvpView {
    void setUserAdapter(List<BackendlessUser> users);
    void setProgressBarVisible();
}
