package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iquesoft.andrew.seedprojectchat.Network.ApiCall;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.MainFragmentAdapter;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class MainFragment extends BaseFragment implements IMainFragment {

    @BindView(R.id.main_screen_recycler_view)
    RecyclerView recyclerMain;

    private View rootView;
    private MainFragmentAdapter adapter;
    private ArrayList<Object> objectArrayList = new ArrayList<>();


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiCall.getCurentFriendList().flatMap(response -> Observable.from(response)
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getUpdated().getTime())
                        .compareTo(messages.getUpdated().getTime())))
                .subscribe(response -> {
                    Observable.from(response).subscribe(friends -> {
                        objectArrayList.add(friends);
                    });
                    asd();
                });
    }

    private void asd(){
        ApiCall.getGroupChatList()
                .flatMap(response -> Observable.from(response)
                        .toSortedList((messages, messages2) -> Long.valueOf(messages2.getUpdated().getTime())
                                .compareTo(messages.getUpdated().getTime())))
                .subscribe(response -> {
                    Observable.from(response).subscribe(friends -> objectArrayList.add(friends));
                    for (Object o : objectArrayList) {
                        Log.d("groupChat", o.toString());
                    }
                    setRecyclerAdapter(objectArrayList);
                });
    }

    public void setRecyclerAdapter(List<Object> objectArrayList) {
        adapter = new MainFragmentAdapter(objectArrayList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMain.setLayoutManager(layoutManager);
        recyclerMain.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }
}
