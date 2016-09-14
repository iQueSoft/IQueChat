package com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.Subscription;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.services.messaging.MessageStatus;
import com.backendless.services.messaging.PublishStatusEnum;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.common.DefaultMessages;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.interfaces.fragments.IChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatFragment;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.subjects.PublishSubject;

/**
 * Created by andru on 9/7/2016.
 */

public class ChatFragmentPresenter implements IChatFragmentPresenter {

    private PublishOptions publishOptions;
    private SubscriptionOptions subscriptionOptions;
    private Subscription subscription;
    private PublishSubject<List<Message>> messages = PublishSubject.create();

    private IChatFragment view;

    @Inject
    public ChatFragmentPresenter() {
        publishOptions = new PublishOptions();
        publishOptions.setPublisherId(Backendless.UserService.CurrentUser().getObjectId());
        subscriptionOptions = new SubscriptionOptions();
    }

    public void setSubtopic(String subtopic){
        publishOptions = new PublishOptions();
        publishOptions.setSubtopic(subtopic);
        publishOptions.setPublisherId(Backendless.UserService.CurrentUser().getObjectId());
        subscriptionOptions = new SubscriptionOptions();
        subscriptionOptions.setSubtopic(subtopic);
    }

    @Override
    public void init(IChatFragment view) {
        this.view = view;
    }

    public void onResume() {
        subscribe();
    }

    private void subscribe() {
        Thread myThread = new Thread(() -> {
            Backendless.Messaging.subscribe(DefaultMessages.DEGAULT_CHANEL, new DefaultBackendlessCallback<List<Message>>(view.getActivityContext()) {
                @Override
                public void handleResponse(List<Message> response) {
                    Message message1 = response.get(0);
                    Thread t = new Thread(()->{
                        Log.i("thread", Thread.currentThread().getName());
                        if (message1.getPublisherId().equals(Backendless.UserService.CurrentUser().getObjectId())) {
                            Messages message = new Messages();
                            message.setMessage_id(message1.getMessageId());
                            message.setData(message1.getData().toString());
                            message.setPublisher_id(message1.getPublisherId());
                            Date date = new Date(message1.getTimestamp());
                            message.setTimestamp(date);
                            message.setHeader(message1.getHeaders().get("android-ticker-text"));
                            message.saveAsync(new DefaultBackendlessCallback<Messages>(view.getActivityContext()));
                        }
                    });
                    t.start();
                    messages.onNext(response);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(view.getActivityContext(), fault.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, subscriptionOptions, new DefaultBackendlessCallback<Subscription>(view.getActivityContext()) {
                @Override
                public void handleResponse(Subscription response) {
                    super.handleResponse(response);
                    subscription = response;
                }
            });
        });
        myThread.start();
    }

    @Override
    public PublishSubject<List<Messages>> getMessageList() {
        PublishSubject<List<Messages>> publishSubject = PublishSubject.create();
        QueryOptions queryOptions = new QueryOptions(100, 0);
        BackendlessDataQuery dataQuery = new BackendlessDataQuery(queryOptions);
        Thread messageListThread = new Thread(() -> {
            Backendless.Data.of(Messages.class).find(dataQuery, new DefaultBackendlessCallback<BackendlessCollection<Messages>>(view.getActivityContext()) {
                @Override
                public void handleResponse(BackendlessCollection<Messages> response) {
                    super.handleResponse(response);
                    publishSubject.onNext(response.getData());
                }
            });
        });
        messageListThread.start();
        return publishSubject;
    }

    @Override
    public PublishSubject<List<Message>> onReceiveMessage() {
        return messages;
    }


    @Override
    public boolean onSendMessage(EditText messageField) {
        String message = messageField.getText().toString();
        Thread sendThread = new Thread(() -> {
            Backendless.Messaging.publish((Object) message, publishOptions, new DefaultBackendlessCallback<MessageStatus>(view.getActivityContext()) {
                @Override
                public void handleResponse(MessageStatus response) {
                    super.handleResponse(response);

                    PublishStatusEnum messageStatus = response.getStatus();

                    if (messageStatus == PublishStatusEnum.SCHEDULED) {
                        messageField.setText("");
                    } else {
                        Toast.makeText(view.getActivityContext(), "Message status: " + messageStatus.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        sendThread.start();
        return true;
    }
}
