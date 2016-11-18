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
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.messaging.PublishOptions;
import com.backendless.persistence.BackendlessDataQuery;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.GroupChatFriendListAdapter;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IGroupChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.MessageUtil;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IGroupChatFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by andru on 9/23/2016.
 */
@InjectViewState
public class GroupChatFragmentPresenter extends MvpPresenter<IGroupChatFragment> implements IGroupChatFragmentPresenter {

    private GroupChatFriendListAdapter adapter;
    private PublishOptions publishOptions;
    private GroupChat curentGroupChat;
    private rx.Subscription messageSubscription;
    private rx.Subscription sortMessage;

    public void setCurentGroupChat(GroupChat groupChat) {
        curentGroupChat = groupChat;
    }

    public Boolean isGroupChatNull() {
        return curentGroupChat == null;
    }

    public GroupChat getCurentGroupChat() {
        return curentGroupChat;
    }

    @Inject
    public GroupChatFragmentPresenter() {
        publishOptions = new PublishOptions();
        publishOptions.setPublisherId(Backendless.UserService.CurrentUser().getObjectId());
    }

    public PublishOptions getPublishOptions() {
        return publishOptions;
    }

    @Override
    public void attachView(IGroupChatFragment view) {
        Log.d("check", "attach");
        MessageUtil.subscribe(curentGroupChat.getObjectId());
        if (curentGroupChat.getMessages() != null){
            sortMessage = Observable.from(curentGroupChat.getMessages())
                    .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                    .subscribe(response -> {
                        getViewState().setMessageAdapter(response);
                    });
        }

                messageSubscription = MessageUtil.obsSaveNewMessage(curentGroupChat.getMessages().get(curentGroupChat.getMessages().size() - 1).getTimestamp()).subscribe(response -> {
                    MessageUtil.sendPushNotification(response, curentGroupChat.getObjectId());
                    curentGroupChat.getMessages().add(response);
                    curentGroupChat.saveAsync(new DefaultBackendlessCallback<>());
                    getViewState().showNewMessage(response);
                });
        super.attachView(view);
    }

    private BehaviorSubject<List<Friends>> getCurentFriendList() {
        BehaviorSubject<List<Friends>> friendsObservable = BehaviorSubject.create();
        String whereClause = "(user_one.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "'" +
                " or " + "user_two.objectId='" + Backendless.UserService.CurrentUser().getProperty("objectId") + "')" +
                " and " + "status = '2'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(whereClause);
            Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>() {
                @Override
                public void handleResponse(BackendlessCollection<Friends> response) {
                    Log.i("friend", response.getData().toString());
                    friendsObservable.onNext(response.getData());
                }
            });
        return friendsObservable;
    }

    public void createDialogGroupChat(@NonNull Context context, @NonNull LayoutInflater inflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(R.layout.crete_group_chat_dialog, null);
        builder.setView(view);
        EditText chatName = (EditText) view.findViewById(R.id.group_chat_name);
        RecyclerView recyclerAddUserToGroupChat = (RecyclerView) view.findViewById(R.id.recycler_add_user_to_group_chat);
        FloatingActionButton fabCreateGroupChat = (FloatingActionButton) view.findViewById(R.id.fab_create_group_chat);
        if (!getCurentFriendList().subscribe().isUnsubscribed()) {
            getCurentFriendList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> setFriendAdapter(response, recyclerAddUserToGroupChat, context));
        }
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
        fabCreateGroupChat.setOnClickListener(v -> {
            ArrayList<BackendlessUser> users = new ArrayList<>();
            if (adapter.getFriendsList() != null) {
                for (Friends friend : adapter.getFriendsList()) {
                    if (friend.getUser_one().getUserId().equals(Backendless.UserService.CurrentUser().getUserId())) {
                        if (friend.getSelected()) {
                            users.add(friend.getUser_two());
                        }
                    } else {
                        if (friend.getSelected()) {
                            users.add(friend.getUser_one());
                        }
                    }
                }
            }
            curentGroupChat.setUsers(users);
            if (chatName.getText().length() != 0) {
                curentGroupChat.setChatName(chatName.getText().toString());
            }
            curentGroupChat.saveAsync(new BackendlessCallback<GroupChat>() {
                @Override
                public void handleResponse(GroupChat groupChat) {

                }
            });
        });
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

    public void saveCurentChat() {
        curentGroupChat.saveAsync(new DefaultBackendlessCallback<>());
    }

    @Override
    public void detachView(IGroupChatFragment view) {
        Log.d("check", "detach");
        if (curentGroupChat.getMessages() != null){
            sortMessage.unsubscribe();
        }
        messageSubscription.unsubscribe();
        try {
            MessageUtil.getSubscription().cancelSubscription();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.detachView(view);
    }

    @StateStrategyType(SkipStrategy.class)
    public void liveChat(MainActivity mainActivity) {
        String myId = Backendless.UserService.CurrentUser().getUserId();
        if (myId.equals(curentGroupChat.getOwnerId())) {
            if (curentGroupChat.getUsers().size() != 0){
                curentGroupChat.setOwner(curentGroupChat.getUsers().get(0));
            } else {
                curentGroupChat.setOwner(null);
            }
            curentGroupChat.saveAsync(new DefaultBackendlessCallback<GroupChat>(){
                @Override
                public void handleResponse(GroupChat response) {
                    mainActivity.setGroupChatContainer();
                    super.handleResponse(response);
                }
            });
        } else {
            for (BackendlessUser user : curentGroupChat.getUsers()) {
                if (user.getUserId().equals(myId)) {
                    curentGroupChat.getUsers().remove(user);
                    curentGroupChat.saveAsync(new BackendlessCallback<GroupChat>() {
                        @Override
                        public void handleResponse(GroupChat groupChat) {
                            mainActivity.setGroupChatContainer();
                        }

                    });
                    break;
                }
            }
        }
    }

//    private void subscribe() {
//            Backendless.Messaging.subscribe(curentGroupChat.getObjectId(), new BackendlessCallback<List<Message>>() {
//                @Override
//                public void handleResponse(List<Message> response) {
//                    messages.onNext(response);
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//
//                }
//            }, subscriptionOptions, new BackendlessCallback<Subscription>() {
//                @Override
//                public void handleResponse(Subscription response) {
//                    subscription = response;
//                }
//            });
//    }
}
