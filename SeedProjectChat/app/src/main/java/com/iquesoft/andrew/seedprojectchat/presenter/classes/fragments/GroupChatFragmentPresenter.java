package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.Subscription;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.services.messaging.MessageStatus;
import com.backendless.services.messaging.PublishStatusEnum;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IGroupChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IGroupChatFragment;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by andru on 9/23/2016.
 */
@InjectViewState
public class GroupChatFragmentPresenter extends MvpPresenter<IGroupChatFragment> implements IGroupChatFragmentPresenter{

    private PublishOptions publishOptions;
    private SubscriptionOptions subscriptionOptions;
    private Subscription subscription;
    private PublishSubject<List<Message>> messages = PublishSubject.create();
    private GroupChat curentGroupChat;
    private rx.Subscription messageSubscription;
    private rx.Subscription sortMessage;
    private CompositeSubscription compositeSubscription;

    public void setCurentGroupChat(GroupChat groupChat){
        curentGroupChat = groupChat;
    }

    public Boolean isGroupChatNull(){
        return curentGroupChat == null;
    }

    @Override
    public void attachView(IGroupChatFragment view) {
        Log.d("check", "attach");
        subscribe();
        sortMessage = Observable.from(curentGroupChat.getMessages()).toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                .subscribe(response -> {
                    getViewState().setMessageAdapter(response);
                });
        messageSubscription = messages
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .flatMap(response -> {
                    Messages messages = new Messages();
                    messages.setHeader(response.getHeaders().get("android-ticker-text"));
                    Date date = new Date(response.getTimestamp());
                    messages.setTimestamp(date);
                    messages.setPublisher_id(response.getPublisherId());
                    messages.setData(response.getData().toString());
                    messages.setMessage_id(response.getMessageId());
                    return Observable.just(messages);
                }).filter(response -> response != null).subscribe(response -> {
                    curentGroupChat.getMessages().add(response);
                    getViewState().showNewMessage(response);
                });
        compositeSubscription = new CompositeSubscription(messageSubscription, sortMessage);
        super.attachView(view);
    }

    public void saveCurentChat(){
        curentGroupChat.saveAsync(new BackendlessCallback<GroupChat>() {
            @Override
            public void handleResponse(GroupChat groupChat) {
                Log.d("response", groupChat.toString());
            }
        });
    }

    @Override
    public void detachView(IGroupChatFragment view) {
        Log.d("check", "detach");
        compositeSubscription.unsubscribe();
        try {
            subscription.cancelSubscription();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        super.detachView(view);
    }

    public void liveChat(MainActivity mainActivity){
        String myId = Backendless.UserService.CurrentUser().getUserId();
        if (myId.equals(curentGroupChat.getOwnerId())){
            curentGroupChat.removeAsync(new BackendlessCallback<Long>() {
                @Override
                public void handleResponse(Long aLong) {
                }
            });
        } else {
            for (BackendlessUser user : curentGroupChat.getUsers()){
                if (user.getUserId().equals(myId)){
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

    @Inject
    public GroupChatFragmentPresenter() {
        publishOptions = new PublishOptions();
        publishOptions.setPublisherId(Backendless.UserService.CurrentUser().getObjectId());
        subscriptionOptions = new SubscriptionOptions();
    }

    private void subscribe() {
        Thread myThread = new Thread(() -> {
            Backendless.Messaging.subscribe(curentGroupChat.getObjectId(), new BackendlessCallback<List<Message>>() {
                @Override
                public void handleResponse(List<Message> response) {
                    messages.onNext(response);
                }

                @Override
                public void handleFault(BackendlessFault fault) {

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

    public boolean onSendMessage(EditText messageField) {
        String message = messageField.getText().toString();
        Thread sendThread = new Thread(() -> {
            Backendless.Messaging.publish(curentGroupChat.getObjectId(), message, publishOptions, new BackendlessCallback<MessageStatus>() {
                @Override
                public void handleResponse(MessageStatus response) {
                    PublishStatusEnum messageStatus = response.getStatus();

                    if (messageStatus == PublishStatusEnum.SCHEDULED) {
                        messageField.setText("");
                    } else {
                       // Toast.makeText(view.getActivityContext(), "Message status: " + messageStatus.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        sendThread.start();
        return true;
    }
}
