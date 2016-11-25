package com.iquesoft.andrew.seedprojectchat.Network;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 10.11.2016.
 */

public class ApiCall {

    public static synchronized BehaviorSubject<List<Friends>> getCurentFriendList() {
        BehaviorSubject<List<Friends>> friendsObservable = BehaviorSubject.create();
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        String whereClause = "(user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
                " or " + "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "')" +
                " and " + "status = '2'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>() {
            @Override
            public void handleResponse(BackendlessCollection<Friends> response) {
                Log.i("friend", response.getData().toString());
                friendsObservable.onNext(response.getData());
            }
        });
        return friendsObservable;
    }

    public static synchronized PublishSubject<List<GroupChat>> getGroupChatList() {
        PublishSubject<List<GroupChat>> publishSubject = PublishSubject.create();
        String whereClause = "owner.objectId='" + Backendless.UserService.CurrentUser().getObjectId() + "'" + " or " +
                "users.objectId='" + Backendless.UserService.CurrentUser().getObjectId() + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setPageSize(100);
        dataQuery.setWhereClause(whereClause);
        GroupChat.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<GroupChat>>() {
            @Override
            public void handleResponse(BackendlessCollection<GroupChat> response) {
                publishSubject.onNext(response.getData());
            }
        });
        return publishSubject;
    }
}
