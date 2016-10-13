package com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments;

import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;

import java.util.List;

import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 9/17/2016.
 */

public interface IGroupChatContainerPresenter {
    BehaviorSubject<List<Friends>> getCurentFriendList();
    PublishSubject<List<GroupChat>> getGroupChatList();
}
