package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.GroupChatFriendListAdapter;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IGroupChatContainerPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IGroupChatContainer;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 9/17/2016.
 */
@InjectViewState
public class GroupChatContainerPresenter extends MvpPresenter<IGroupChatContainer> implements IGroupChatContainerPresenter {

    private GroupChatFriendListAdapter adapter;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getGroupChatList().subscribe(response -> getViewState().setGroupChatContainerAdapter(response));
    }

    public PublishSubject<List<GroupChat>> getGroupChatList(){
        PublishSubject<List<GroupChat>> publishSubject = PublishSubject.create();
        String whereClause = "owner.objectId='" + Backendless.UserService.CurrentUser().getObjectId() + "'" + " or " +
                "users.objectId='" + Backendless.UserService.CurrentUser().getObjectId() + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
        GroupChat.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<GroupChat>>(){
            @Override
            public void handleResponse(BackendlessCollection<GroupChat> response) {
                publishSubject.onNext(response.getData());
            }
        });
        return publishSubject;
    }

    public void createDialogGroupChat(@NonNull Context context, @NonNull LayoutInflater inflater){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.crete_group_chat_dialog, null);
        builder.setView(view);
        EditText chatName = (EditText) view.findViewById(R.id.group_chat_name);
        RecyclerView recyclerAddUserToGroupChat = (RecyclerView) view.findViewById(R.id.recycler_add_user_to_group_chat);
        FloatingActionButton fabCreateGroupChat = (FloatingActionButton) view.findViewById(R.id.fab_create_group_chat);
        if (!getCurentFriendList().subscribe().isUnsubscribed()){
            getCurentFriendList().subscribe(response -> setFriendAdapter(response, recyclerAddUserToGroupChat, context));
        }
        fabCreateGroupChat.setOnClickListener(v -> {
            ArrayList<BackendlessUser> users = new ArrayList<>();
            for (Friends friend : adapter.getFriendsList()){
                if (friend.getUser_one().getUserId().equals(Backendless.UserService.CurrentUser().getUserId()))
                {
                    if (friend.getSelected()){
                        users.add(friend.getUser_two());
                    }
                } else {
                    if (friend.getSelected()){
                        users.add(friend.getUser_one());
                    }
                }
            }
            createGroupChat(chatName.getText().toString(), users);
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setFriendAdapter(List<Friends> friends, RecyclerView recyclerAddUserToGroupChat, Context context) {
        adapter = new GroupChatFriendListAdapter(friends, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerAddUserToGroupChat.setLayoutManager(linearLayoutManager);
        recyclerAddUserToGroupChat.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(true);
        scaleInAnimationAdapter.setDuration(500);
        recyclerAddUserToGroupChat.setAdapter(scaleInAnimationAdapter);
    }

    public void createGroupChat(String chanel, List<BackendlessUser> users){
        GroupChat groupChat = new GroupChat();
        groupChat.setChanel(chanel);
        groupChat.setOwner(Backendless.UserService.CurrentUser());
        groupChat.setUsers(users);
        groupChat.saveAsync(new BackendlessCallback<GroupChat>() {
            @Override
            public void handleResponse(GroupChat groupChat) {

            }
        });
    }

    public BehaviorSubject<List<Friends>> getCurentFriendList(){
        BehaviorSubject<List<Friends>> friendsObservable = BehaviorSubject.create();
        friendsObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        String whereClause = "(user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
                " or " + "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "')" +
                " and " + "status = '2'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
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
