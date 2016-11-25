package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.iquesoft.andrew.seedprojectchat.Network.ApiCall;
import com.iquesoft.andrew.seedprojectchat.model.BaseChatModel;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IMainFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by andru on 08.11.2016.
 */
@InjectViewState
public class MainFragmentPresenter extends MvpPresenter<IMainFragment> {

    private ArrayList<BaseChatModel> objectArrayList = new ArrayList<>();

    @Override
    protected void onFirstViewAttach() {
        getFriendListAndGroupChatList();
        super.onFirstViewAttach();
    }

    public void getFriendListAndGroupChatList(){
        ApiCall.getCurentFriendList().subscribe(response -> {
            Observable.from(response).subscribe(friends -> {
                objectArrayList.add(friends);
            });
            getGroupChatListAndAddAdapter();
        });
    }

    private void getGroupChatListAndAddAdapter() {
        ApiCall.getGroupChatList()
                .subscribe(response -> {
                    Observable.from(response).subscribe(friends -> objectArrayList.add(friends));
                    setRecyclerAdapter(objectArrayList);
                });
    }

    private void setRecyclerAdapter(List<BaseChatModel> objectArrayList) {
        Observable.just(objectArrayList)
                .flatMap(response -> Observable.from(response)
                        .filter(resp -> resp.getMessages().size() != 0).filter(resp -> resp.getUpdated() != null)
                        .toSortedList((messages, messages2) -> messages2.getUpdated()
                                .compareTo(messages.getUpdated())))
                .subscribe(response -> {
                    getViewState().setRecyclerMain(response);
                });
    }
}
