package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFindFriendFragment;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by andru on 8/31/2016.
 */

public interface IFindFriendFragmentPresenter extends BaseFragmentPresenter<IFindFriendFragment> {
    PublishSubject<List<BackendlessUser>> getBackendlessUsers(String username, List<Friends> friendsList);
}
