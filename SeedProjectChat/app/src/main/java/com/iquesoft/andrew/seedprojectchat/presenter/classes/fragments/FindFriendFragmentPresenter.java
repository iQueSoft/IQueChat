package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IFindFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFindFriendFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 8/31/2016.
 */

public class FindFriendFragmentPresenter implements IFindFriendFragmentPresenter {

    private IFindFriendFragment view;
    PublishSubject<List<BackendlessUser>> usersObservable;

    @Inject
    public FindFriendFragmentPresenter(){

    }

    @Override
    public void init(IFindFriendFragment view) {
        this.view = view;
    }

    @Override
    public PublishSubject<List<BackendlessUser>> getBackendlessUsers(String username, List<Friends> friendsList) {
        usersObservable = PublishSubject.create();
        usersObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        StringBuilder usernamequery = new StringBuilder();
        usernamequery.append("name LIKE");
        usernamequery.append("'%" + username + "%'");
        usernamequery.append(" or ");
        usernamequery.append("email LIKE");
        usernamequery.append("'%" + username + "%'");
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( usernamequery.toString() );
        Backendless.Data.of(BackendlessUser.class).find(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<BackendlessUser>>(view.getActivityContext()) {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                super.handleResponse(response);
                usersObservable.onNext(response.getData());
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
            }
        });
        ArrayList<BackendlessUser> users = new ArrayList<>();
        PublishSubject<List<BackendlessUser>> usersObs = PublishSubject.create();
        if (friendsList.size() != 0){
            usersObservable.flatMap(Observable::from)
                    .filter(response -> !response.getObjectId().equals(Backendless.UserService.CurrentUser().getProperty("objectId")))
                    .skipWhile(response -> check(response, friendsList))
                    .subscribe(user->{
                        Log.i("user", user.toString());
                        users.add(user);
                        usersObs.onNext(users);
                    });
        } else {
            usersObservable.flatMap(Observable::from)
                    .filter(response -> !response.getObjectId().equals(Backendless.UserService.CurrentUser().getProperty("objectId")))
                    .subscribe(user->{
                        Log.i("user", user.toString());
                        users.add(user);
                        usersObs.onNext(users);
                    });
        }
        return usersObs;
    }

    private Boolean check(BackendlessUser user, List<Friends> friendsList){
        Boolean state = null;
        for (Friends curentFriends : friendsList){
            state = user.getObjectId().equals(curentFriends.getUser_one().getObjectId()) || user.getObjectId().equals(curentFriends.getUser_two().getObjectId());
            if (state){
                break;
            }
        }
        return state;
    }
}
