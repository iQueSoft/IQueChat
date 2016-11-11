package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iquesoft.andrew.seedprojectchat.Network.ApiCall;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.Collections;

import rx.Observable;

public class MainFragment extends BaseFragment implements IMainFragment{

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiCall.getCurentFriendList().flatMap(response -> Observable.from(response)
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getUpdated().getTime())
                        .compareTo(messages.getUpdated().getTime())))
                .subscribe(response -> Observable.from(response).subscribe(friends -> Log.d("friend", friends.getUpdated().toString())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
