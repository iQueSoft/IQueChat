package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IMyInviteFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IMyInvateFragment;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/5/2016.
 */

public class MyInviteFragmentPresenter implements IMyInviteFragmentPresenter {

    private IMyInvateFragment view;
    private BehaviorSubject<List<Friends>> myInviteFriendsBS;

    @Inject
    public MyInviteFragmentPresenter(){
    }

    @Override
    public void init(IMyInvateFragment view) {
        this.view = view;
    }

    public BehaviorSubject<List<Friends>> getMyInviteFriendsList(){
        myInviteFriendsBS = BehaviorSubject.create();
        myInviteFriendsBS.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "user_one.objectId='" ).append( Backendless.UserService.CurrentUser().getProperty("objectId")).append( "'" );
        whereClause.append(" and ");
        whereClause.append("status = '1'");
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
        Thread myInviteFriendsListThread = new Thread(() -> {
            Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(view.getActivityContext()){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    super.handleResponse(response);
                    Log.i("friend", response.getData().toString());
                    myInviteFriendsBS.onNext(response.getData());
                }
            });
        });
        myInviteFriendsListThread.start();
        return myInviteFriendsBS;
    }

}
