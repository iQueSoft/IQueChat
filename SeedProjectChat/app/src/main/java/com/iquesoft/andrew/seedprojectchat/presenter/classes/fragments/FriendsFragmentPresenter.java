package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IFriendsFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IFriendsFragment;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by Andrew on 8/23/2016.
 */
@InjectViewState
public class FriendsFragmentPresenter extends MvpPresenter<IFriendsFragment> implements IFriendsFragmentPresenter {

    private Subscription subscription;

    @Override
    public void attachView(IFriendsFragment view) {
        subscription = getCurentFriendList().subscribe(response -> getViewState().setUserAdapter(response));
        super.attachView(view);
    }

    @Override
    public void detachView(IFriendsFragment view) {
        subscription.unsubscribe();
        super.detachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
    }

    @Override
    public boolean isInRestoreState(IFriendsFragment view) {
        return super.isInRestoreState(view);
    }

    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        BehaviorSubject<List<Friends>> friendsObservable = BehaviorSubject.create();
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
            Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>(){
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    Log.i("friend", response.getData().toString());
                    friendsObservable.onNext(response.getData());
                }
            });
        });
        friendsThread.start();
        return friendsObservable;
    }
}
