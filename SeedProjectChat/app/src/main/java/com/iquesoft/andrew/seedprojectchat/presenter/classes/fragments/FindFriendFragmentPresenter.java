package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IFindFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFindFriendFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 8/31/2016.
 */
@InjectViewState
public class FindFriendFragmentPresenter extends MvpPresenter<IFindFriendFragment> implements IFindFriendFragmentPresenter {

    private PublishSubject<List<BackendlessUser>> usersObservable;

    @Override
    public PublishSubject<List<BackendlessUser>> getBackendlessUsers(String username, List<Friends> friendsList) {
        usersObservable = PublishSubject.create();
        String usernamequery = "name LIKE" +
                "'%" + username + "%'" +
                " or " +
                "email LIKE" +
                "'%" + username + "%'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(usernamequery);
            Backendless.Data.of(BackendlessUser.class).find(dataQuery, new BackendlessCallback<BackendlessCollection<BackendlessUser>>() {
                @Override
                public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                    usersObservable.onNext(response.getData());
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    super.handleFault(fault);
                }
            });


        return checkBackendlessUser(friendsList);
    }

    private PublishSubject<List<BackendlessUser>> checkBackendlessUser(List<Friends> friendsList){
        PublishSubject<List<BackendlessUser>> usersObs = PublishSubject.create();
        ArrayList<BackendlessUser> users = new ArrayList<>();
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
