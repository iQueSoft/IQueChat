package net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments;

import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.model.GroupChat;

import java.util.List;

import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public interface IGroupChatContainerPresenter {
    BehaviorSubject<List<Friends>> getCurentFriendList();
    PublishSubject<List<GroupChat>> getGroupChatList();
}
