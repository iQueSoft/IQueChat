package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IInviteToFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IInviteToFriendFragment;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/1/2016.
 */
@InjectViewState
public class InviteToFriendFragmentPresenter extends MvpPresenter<IInviteToFriendFragment> implements IInviteToFriendFragmentPresenter {

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getCurentFriendList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(response -> getViewState().setUserAdapter(response));
    }

    private BehaviorSubject<List<Friends>> getCurentFriendList(){
        BehaviorSubject<List<Friends>> friendsObservable;
        friendsObservable = BehaviorSubject.create();
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        String whereClause = "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
                " and " +
                "status = '1'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        Thread getFriendListThread = new Thread(() -> {
            Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>(){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    Log.i("friend", response.getData().toString());
                    friendsObservable.onNext(response.getData());
                }
            });
        });
        getFriendListThread.start();
        return friendsObservable;
    }
}
