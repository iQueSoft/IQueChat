package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by andru on 8/31/2016.
 */

public interface IFindFriendFragmentPresenter {
    PublishSubject<List<BackendlessUser>> getBackendlessUsers(String username, List<Friends> friendsList);
}
