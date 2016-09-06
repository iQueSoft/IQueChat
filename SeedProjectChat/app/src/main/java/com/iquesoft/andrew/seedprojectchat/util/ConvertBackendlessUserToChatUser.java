package com.iquesoft.andrew.seedprojectchat.util;

import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by andru on 8/31/2016.
 */

public class ConvertBackendlessUserToChatUser {

    public PublishSubject<ArrayList<ChatUser>> transformationBackendlessUserToChatUser(PublishSubject<BackendlessCollection<BackendlessUser>> usersPublishSubject) {
        ArrayList<ChatUser> thisUsers = new ArrayList<>();
        PublishSubject <ArrayList<ChatUser>> chatUsers = PublishSubject.create();
        chatUsers.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        usersPublishSubject.subscribe(backendlessUsers -> {
            for (BackendlessUser user : backendlessUsers.getData()) {
                ChatUser chatUser = new ChatUser();
                chatUser.setEmail(user.getEmail());
                Boolean online = (Boolean) user.getProperty("online");
                chatUser.setOnline(online);
                try {
                    chatUser.setName(user.getProperty("name").toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    chatUser.setPhoto(user.getProperty("photo").toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    chatUser.setDevice_id(user.getProperty("device_id").toString());
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                thisUsers.add(chatUser);
            }
            chatUsers.onNext(thisUsers);
        });
        return chatUsers;
    }
}
