package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IInviteToFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IInviteToFriendFragment;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/1/2016.
 */

public class InviteToFriendFragmentPresenter implements IInviteToFriendFragmentPresenter {

    private IInviteToFriendFragment view;
    private BehaviorSubject<List<Friends>> friendsObservable;

    @Inject
    public InviteToFriendFragmentPresenter(){

    }

    @Override
    public void init(IInviteToFriendFragment view) {
        this.view = view;
    }

    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        friendsObservable = BehaviorSubject.create();
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "user_two.objectId='" ).append( Backendless.UserService.CurrentUser().getProperty("objectId")).append( "'" );
        whereClause.append(" and ");
        whereClause.append("status = '1'");
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
        Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(view.getActivityContext()){
            @Override
            public void handleResponse(BackendlessCollection<Friends> response) {
                super.handleResponse(response);
                Log.i("friend", response.getData().toString());
                friendsObservable.onNext(response.getData());
            }
        });
        return friendsObservable;
    }
}
