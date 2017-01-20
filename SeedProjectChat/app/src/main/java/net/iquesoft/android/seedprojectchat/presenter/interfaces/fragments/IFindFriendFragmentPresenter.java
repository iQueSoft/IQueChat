package net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments;

import com.backendless.BackendlessUser;
import net.iquesoft.android.seedprojectchat.model.Friends;

import java.util.List;

import rx.subjects.PublishSubject;

public interface IFindFriendFragmentPresenter {
    PublishSubject<List<BackendlessUser>> getBackendlessUsers(String username, List<Friends> friendsList);
}
