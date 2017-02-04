package net.iquesoft.android.seedprojectchat.util;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.Message;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushPolicyEnum;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.services.messaging.MessageStatus;
import com.backendless.services.messaging.PublishStatusEnum;

import net.iquesoft.android.seedprojectchat.adapters.PreviewPhotoAdapter;
import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.Messages;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MessageUtil {

    private static PublishSubject<List<Message>> messages = PublishSubject.create();
    private static PublishOptions publishOptions = new PublishOptions();
    private static Subscription subscription;

    public static Subscription getSubscription() {
        return subscription;
    }

    public static PublishSubject<List<Message>> getMessages() {
        return messages;
    }

    public static void setMessages(PublishSubject<List<Message>> messages) {
        MessageUtil.messages = messages;
    }

    public static synchronized void subscribe(String chanelName) {
        Backendless.Messaging.subscribe(chanelName, new BackendlessCallback<List<Message>>() {
            @Override
            public void handleResponse(List<Message> response) {
                messages.onNext(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        }, new SubscriptionOptions(), new BackendlessCallback<Subscription>() {
            @Override
            public void handleResponse(Subscription response) {
                subscription = response;
            }
        });
    }

    public static synchronized void onSendMessage(EditText messageField, Map<String, String> message, Context context, PublishOptions publishOptions, String chanelName) {
        Messages messages = new Messages();
        messages.setData(message.toString());
        messages.setTimestamp(new Date());
        messages.setPublisher_id(Backendless.UserService.CurrentUser().getUserId());
        messages.setRead(false);
        Backendless.Messaging.publish(chanelName, messages, publishOptions, new BackendlessCallback<MessageStatus>() {
            @Override
            public void handleResponse(MessageStatus response) {
                PublishStatusEnum messageStatus = response.getStatus();

                if (messageStatus == PublishStatusEnum.SCHEDULED) {
                    messageField.setText("");
                } else {
                    Toast.makeText(context, "Message status: " + messageStatus.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static synchronized Observable<Messages> obsSaveNewMessage(Date lastMessageDate){
       return messages.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::from)
                .flatMap(response -> {
                    Messages message = null;
                    try {
                        message = (Messages) response.getData();
                        message.setHeader(response.getHeaders().get("android-ticker-text"));
                        message.setRead(false);
                        message.setMessage_id(response.getMessageId());
                    } catch (ClassCastException e){
                        e.printStackTrace();
                        return null;
                    }
                    return Observable.just(message);
                }).filter(response -> !response.getTimestamp().equals(lastMessageDate))
                .filter(response -> response != null);
    }

    public static synchronized void sendPushNotification(Messages message, String chanelName) {
        PublishOptions publishOptions = new PublishOptions();
        publishOptions.putHeader(PublishOptions.ANDROID_TICKER_TEXT_TAG, "You have new message");
        publishOptions.putHeader(PublishOptions.ANDROID_CONTENT_TITLE_TAG, "iQueChat");
        publishOptions.putHeader("senderId", message.getPublisher_id());
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.setPushPolicy(PushPolicyEnum.ONLY);
        Backendless.Messaging.publish(chanelName, message.getData(), publishOptions, deliveryOptions, new DefaultBackendlessCallback<>());
    }

    public static synchronized void sendClick(ArrayList<String> serverPhotoPaths, ArrayList<String> serverDocPaths,
                                              Context context, ArrayList<String> photoPaths, ArrayList<String> docPaths,
                                              EmojiconEditText messageEdit, String chanelName, PreviewPhotoAdapter previewPhotoAdapter){
        serverPhotoPaths.clear();
        if (photoPaths.size() != 0){
            UploadFileUtil.getAndCompressImageWithUri(photoPaths,context).subscribe(response -> {
                serverPhotoPaths.add(response);
                if (serverPhotoPaths.size() == photoPaths.size()){
                    photoPaths.clear();
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("message", StringEscapeUtils.escapeJava(messageEdit.getText().toString() + " "));
                    for (int i = 0; i<serverPhotoPaths.size(); i++){
                        String imageUri = serverPhotoPaths.get(i);
                        messageMap.put("image"+i, imageUri);
                    }
                    onSendMessage(messageEdit, messageMap, context, publishOptions, chanelName);
                    previewPhotoAdapter.clear();
                }
            });
        }else if (docPaths.size() != 0){
            serverDocPaths.clear();
            UploadFileUtil.uploadFilesToServer(docPaths, context).subscribe(response -> {
                serverDocPaths.add(response);
                if (serverDocPaths.size() == docPaths.size()){
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("message", StringEscapeUtils.escapeJava(messageEdit.getText().toString() + " "));
                    for (int i = 0; i<serverDocPaths.size(); i++){
                        String imageUri = serverDocPaths.get(i);
                        messageMap.put("document"+i, imageUri);
                    }
                    Log.d("document", serverDocPaths.toString());
                    MessageUtil.onSendMessage(messageEdit, messageMap, context, publishOptions, chanelName);
                    previewPhotoAdapter.clear();
                }
            });
        } else {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("message", StringEscapeUtils.escapeJava(messageEdit.getText().toString() + " "));
            MessageUtil.onSendMessage(messageEdit, messageMap, context, publishOptions, chanelName);
        }
    }
}
