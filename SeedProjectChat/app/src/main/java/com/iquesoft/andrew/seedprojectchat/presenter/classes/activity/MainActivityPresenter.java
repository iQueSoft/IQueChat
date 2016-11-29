package com.iquesoft.andrew.seedprojectchat.presenter.classes.activity;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.Network.ApiCall;
import com.iquesoft.andrew.seedprojectchat.common.DefaultsBackendlessKey;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.activity.IMainActivityPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.activity.IMainActivity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;

/**
 * Created by Andrew on 18.08.2016.
 */
@InjectViewState
public class MainActivityPresenter extends MvpPresenter<IMainActivity> implements IMainActivityPresenter {

    @Override
    protected void onFirstViewAttach() {
        subscribeToMultipleChanelPushNotification(new LinkedList<>());
        super.onFirstViewAttach();
    }

    private void subscribeToMultipleChanelPushNotification(List<String> listId){
        ApiCall.getGroupChatList().subscribe(response -> {
            Observable.from(response).subscribe(respons -> listId.add(respons.getObjectId()));
            ApiCall.getCurentFriendList().subscribe(friendses ->{
                Observable.from(friendses).subscribe(respons -> listId.add(respons.getObjectId()));
                subscribePushNotification(listId);
            });
        });
    }

    private void subscribePushNotification(List<String> chanels){
        Date date = new Date();
        date.setTime(date.getTime() + 31536000L);
        Backendless.Messaging.registerDevice(DefaultsBackendlessKey.GOOGLE_PROJECT_ID, chanels, date, new BackendlessCallback<Void>() {
            @Override
            public void handleResponse(Void aVoid) {
                Log.d("register", "register ok");
            }
        });
    }
}
