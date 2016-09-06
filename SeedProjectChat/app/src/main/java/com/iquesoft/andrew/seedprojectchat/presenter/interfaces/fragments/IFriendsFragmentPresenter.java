package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import rx.subjects.PublishSubject;

/**
 * Created by Andrew on 8/23/2016.
 */

public interface IFriendsFragmentPresenter extends BaseFragmentPresenter<IFriendsFragment> {
    PublishSubject<BackendlessCollection<BackendlessUser>> getBackendlessUsers();
}
