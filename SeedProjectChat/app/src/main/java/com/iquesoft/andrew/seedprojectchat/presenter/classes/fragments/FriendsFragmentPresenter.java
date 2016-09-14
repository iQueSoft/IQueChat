package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IFriendsFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import static com.iquesoft.andrew.seedprojectchat.R.string.username;

/**
 * Created by Andrew on 8/23/2016.
 */

public class FriendsFragmentPresenter implements IFriendsFragmentPresenter {

    private IFriendsFragment view;
    private PublishSubject<BackendlessCollection<BackendlessUser>> usersObservable;
    private BehaviorSubject<List<Friends>> friendsObservable;


    @Inject
    public FriendsFragmentPresenter() {
    }

    @Override
    public void init(IFriendsFragment view) {
        this.view = view;
    }

    @Override
    public PublishSubject<BackendlessCollection<BackendlessUser>> getBackendlessUsers() {
        usersObservable = PublishSubject.create();
        String usernamequery = "'%" + username + "%'";
        String whereClause = "name LIKE" + usernamequery;
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );
        usersObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Thread backendlessUsersThread = new Thread(() -> {
            Backendless.Data.of(BackendlessUser.class).find(new DefaultBackendlessCallback<BackendlessCollection<BackendlessUser>>(view.getActivityContext()) {
                @Override
                public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                    super.handleResponse(response);
                    usersObservable.onNext(response);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    super.handleFault(fault);
                }
            });
        });
        backendlessUsersThread.start();
        return usersObservable;
    }

    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        friendsObservable = BehaviorSubject.create();
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        StringBuilder whereClause = new StringBuilder();
        whereClause.append( "(user_one.objectId='" ).append( Backendless.UserService.CurrentUser().getProperty("objectId")).append( "'" );
        whereClause.append(" or ");
        whereClause.append("user_two.objectId='" ).append( Backendless.UserService.CurrentUser().getProperty("objectId")).append( "')" );
        whereClause.append(" and ");
        whereClause.append("status = '2'");
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause.toString() );
        Thread friendsThread = new Thread(() -> {
            Friends.findAsync(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Friends>>(view.getActivityContext()){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    super.handleResponse(response);
                    Log.i("friend", response.getData().toString());
                    friendsObservable.onNext(response.getData());
                }
            });
        });
        friendsThread.start();
        return friendsObservable;
    }
}
