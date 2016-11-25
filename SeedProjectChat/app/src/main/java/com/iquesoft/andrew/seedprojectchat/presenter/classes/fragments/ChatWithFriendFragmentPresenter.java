package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IChatWithFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.MessageUtil;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatWithFriendFragment;

import rx.Observable;

/**
 * Created by andru on 9/15/2016.
 */
@InjectViewState
public class ChatWithFriendFragmentPresenter extends MvpPresenter<IChatWithFriendFragment> implements IChatWithFriendFragmentPresenter {

    private rx.Subscription subscribeUpdateNewMessages;
    private Friends friends;

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    @Override
    public void attachView(IChatWithFriendFragment view) {
        MessageUtil.subscribe(friends.getObjectId());
        subscribeUpdateNewMessages = MessageUtil.obsSaveNewMessage(friends.getMessages().get(friends.getMessages().size() - 1).getTimestamp()).subscribe(response -> {
            getFriends().getMessages().add(response);
            getFriends().saveAsync(new DefaultBackendlessCallback<>());
            MessageUtil.sendPushNotification(response, friends.getObjectId());
            getViewState().updateLastVisibleMessage(response);
        });
        super.attachView(view);
    }

    @Override
    public void detachView(IChatWithFriendFragment view) {
        try {
            MessageUtil.getSubscription().cancelSubscription();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            subscribeUpdateNewMessages.unsubscribe();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        super.detachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Observable.from(getFriends().getMessages())
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                .subscribe(response -> getViewState().setUserAdapter(response));
    }

    public void refreshMessageList(){
        String usernameQuery = "objectId =" + "'" + friends.getObjectId() + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(usernameQuery);
        Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>() {
            @Override
            public void handleResponse(BackendlessCollection<Friends> friendsBackendlessCollection) {
                Observable.just(friendsBackendlessCollection.getData().get(0).getMessages()).flatMap(Observable::from).toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                        .subscribe(response -> {
                            getViewState().setUserAdapter(response);
                            friends.setMessages(response);
                        });
            }
        });
    }

}
