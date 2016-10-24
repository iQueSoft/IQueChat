package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.content.Context;
import android.text.TextUtils;
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
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IChatWithFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatWithFriendFragment;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private rx.Subscription subscribeUpdateNewMessages;
    private Friends friends;
    private Context context;

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
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
    public void attachView(IChatWithFriendFragment view) {
        subscribe();
        subscribeUpdateNewMessages = messages.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).flatMap(Observable::from).flatMap(response -> {
            Messages messages = new Messages();
            messages.setHeader(response.getHeaders().get("android-ticker-text"));
            Date date = new Date(response.getTimestamp());
            messages.setTimestamp(date);
            messages.setPublisher_id(response.getPublisherId());
            Map<String, String> mm = splitToMap(response.getData().toString(), ", ", "=");
            Log.d("mm", mm.get("message"));
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
        super.attachView(view);
    }

    public static Map<String, String> splitToMap(String source, String entriesSeparator, String keyValueSeparator) {
        Map<String, String> map = new HashMap<String, String>();
        String[] entries = source.replace("{", "").replace("}","").split(entriesSeparator);
        for (String entry : entries) {
            if (!TextUtils.isEmpty(entry) && entry.contains(keyValueSeparator)) {
                String[] keyValue = entry.split(keyValueSeparator);
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return map;
    }

    @Override
    public void detachView(IChatWithFriendFragment view) {
        subscription.cancelSubscription();
        subscribeUpdateNewMessages.unsubscribe();
        super.detachView(view);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Observable.from(getFriends().getMessages())
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                .subscribe(response -> getViewState().setUserAdapter(response));
    }


    public void subscribe() {
            Backendless.Messaging.subscribe(friends.getObjectId(), new BackendlessCallback<List<Message>>() {
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
    }

    @Override
    public boolean onSendMessage(EditText messageField, Context context) {
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(messageField.getText().toString());
        Message message = new Message();
        Long tsLong = System.currentTimeMillis()/1000;
        message.setTimestamp(tsLong);
        message.setData(toServerUnicodeEncoded);
        message.setPublisherId(Backendless.UserService.CurrentUser().getUserId());
        Map<String, String> map = new HashMap<>();
        map.put("message", toServerUnicodeEncoded);
        map.put("image", "imgURI");
        Thread sendThread = new Thread(() -> {
            Backendless.Messaging.publish(friends.getObjectId(), map, publishOptions, new DefaultBackendlessCallback<MessageStatus>(context) {
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
                        .subscribe(response -> {
                            getViewState().setUserAdapter(response);
                            friends.setMessages(response);
                        });
            }
        });
    }

}
