package net.iquesoft.android.seedprojectchat.presenter.classes.fragments;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.persistence.BackendlessDataQuery;
import net.iquesoft.android.seedprojectchat.adapters.ChatFragmentAdapter;
import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.Friends;
import net.iquesoft.android.seedprojectchat.model.Messages;
import net.iquesoft.android.seedprojectchat.presenter.interfaces.fragments.IChatWithFriendFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.MessageUtil;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IChatWithFriendFragment;

import rx.Observable;

@InjectViewState
public class ChatWithFriendFragmentPresenter extends MvpPresenter<IChatWithFriendFragment> implements IChatWithFriendFragmentPresenter {

    private rx.Subscription subscribeUpdateNewMessages;
    private Friends friends;

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    @Override
    public void attachView(IChatWithFriendFragment view) {
        MessageUtil.subscribe(friends.getObjectId());
        subscribeUpdateNewMessages = MessageUtil
                .obsSaveNewMessage(friends.getMessages().get(friends.getMessages().size() - 1).getTimestamp())
                .subscribe(response -> {
            getFriends().getMessages().add(response);
            getFriends().saveAsync(new DefaultBackendlessCallback<>());
            MessageUtil.sendPushNotification(response, friends.getObjectId());
            getViewState().updateLastVisibleMessage(response);
        });
        super.attachView(view);
    }

    @Override
    public void detachView(IChatWithFriendFragment view) {
        try {
            MessageUtil.getSubscription().cancelSubscription();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        try {
            subscribeUpdateNewMessages.unsubscribe();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        super.detachView(view);
    }

    public void setReadClick(ChatFragmentAdapter adapter){
        for (Messages messages : getFriends().getMessages()){
            if (!messages.getPublisher_id().equals(Backendless.UserService.CurrentUser().getUserId())){
                messages.setRead(true);
            }
        }
        getFriends().saveAsync(new DefaultBackendlessCallback<Friends>(){
            @Override
            public void handleResponse(Friends response) {
                adapter.notifyDataSetChanged();
                super.handleResponse(response);
            }
        });
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Observable.from(getFriends().getMessages())
                .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                .subscribe(response -> getViewState().setUserAdapter(response));
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
