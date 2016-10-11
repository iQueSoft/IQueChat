package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.iquesoft.andrew.seedprojectchat.model.Friends;

import java.util.List;

import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/5/2016.
 */

public interface IMyInviteFragmentPresenter {
    BehaviorSubject<List<Friends>> getMyInviteFriendsList();
}
