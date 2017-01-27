package net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments;

import net.iquesoft.android.seedprojectchat.model.Friends;

import java.util.List;

import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public interface IMyInviteFragmentPresenter {
    PublishSubject<List<Friends>> getMyInviteFriendsList();
}
