package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.Subscription;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.services.messaging.MessageStatus;
import com.backendless.services.messaging.PublishStatusEnum;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.common.DefaultMessages;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IChatWithFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatWithFriendFragment;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 9/15/2016.
 */
@InjectViewState
public class ChatWithFriendFragmentPresenter extends MvpPresenter<IChatWithFriendFragment> implements IChatWithFriendFragmentPresenter {

    private PublishOptions publishOptions;
    private SubscriptionOptions subscriptionOptions;
    private PublishSubject<List<Message>> messages = PublishSubject.create();
    private Subscription subscription;
    private Friends friends;
    private Context context;

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    public void setSubtopic(String subtopic) {
        publishOptions.setSubtopic(subtopic);
        subscriptionOptions.setSubtopic(subtopic);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ChatWithFriendFragmentPresenter() {
        publishOptions = new PublishOptions();
        publishOptions.setPublisherId(Backendless.UserService.CurrentUser().getObjectId());
        subscriptionOptions = new SubscriptionOptions();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        subscribe();
        Observable.from(getFriends().getMessages())
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                .subscribe(response -> getViewState().setUserAdapter(response));
        subscribeUpdateNewMessages();
    }

    private void subscribeUpdateNewMessages(){
        messages.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).flatMap(Observable::from).flatMap(response -> {
            Messages messages = new Messages();
            messages.setHeader(response.getHeaders().get("android-ticker-text"));
            Date date = new Date(response.getTimestamp());
            messages.setTimestamp(date);
            messages.setPublisher_id(response.getPublisherId());
            messages.setData(response.getData().toString());
            messages.setMessage_id(response.getMessageId());
            return Observable.just(messages);
        }).subscribe(response -> {
            getFriends().getMessages().add(response);
            Thread saveThread = new Thread(()->{
                getFriends().saveAsync(new DefaultBackendlessCallback<Friends>(context));
            });
            saveThread.start();
            getViewState().updateLastVisibleMessage(response);
        });
    }

    private void subscribe() {
        Thread myThread = new Thread(() -> {
            Backendless.Messaging.subscribe(DefaultMessages.DEGAULT_CHANEL, new BackendlessCallback<List<Message>>() {
                @Override
                public void handleResponse(List<Message> response) {
                    messages.onNext(response);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Log.d("error", fault.getMessage());
                }
            }, subscriptionOptions, new BackendlessCallback<Subscription>() {
                @Override
                public void handleResponse(Subscription response) {
                    subscription = response;
                }
            });
        });
        myThread.start();
    }

    @Override
    public boolean onSendMessage(EditText messageField, Context context) {
        String message = messageField.getText().toString();
        Thread sendThread = new Thread(() -> {
            Backendless.Messaging.publish((Object) message, publishOptions, new DefaultBackendlessCallback<MessageStatus>(context) {
                @Override
                public void handleResponse(MessageStatus response) {
                    super.handleResponse(response);
                    PublishStatusEnum messageStatus = response.getStatus();
                    if (messageStatus == PublishStatusEnum.SCHEDULED) {
                        messageField.setText("");
                    } else {
                        Toast.makeText(context, "Message status: " + messageStatus.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        sendThread.start();
        return true;
    }

    public void refreshMessageList(){
        String usernameQuery = "objectId =" + "'" + friends.getObjectId() + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause(usernameQuery);
        Friends.findAsync(dataQuery, new BackendlessCallback<BackendlessCollection<Friends>>() {
            @Override
            public void handleResponse(BackendlessCollection<Friends> friendsBackendlessCollection) {
                Observable.just(friendsBackendlessCollection.getData().get(0).getMessages()).flatMap(Observable::from).toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                        .subscribe(response -> getViewState().setUserAdapter(response));
            }
        });
    }

}
