package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.iquesoft.andrew.seedprojectchat.common.BaseFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IMyInvateFragment;

import java.util.List;

import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/5/2016.
 */

public interface IMyInviteFragmentPresenter extends BaseFragmentPresenter<IMyInvateFragment> {
    BehaviorSubject<List<Friends>> getMyInviteFriendsList();
}
