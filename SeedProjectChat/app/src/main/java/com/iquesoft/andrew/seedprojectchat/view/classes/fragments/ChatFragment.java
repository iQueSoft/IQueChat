package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.ChatFragmentAdapter;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.di.components.IMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.ChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatFragment;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatFragment extends BaseFragment implements IChatFragment {

    @Inject
    ChatFragmentPresenter presenter;

    @BindView(R.id.messageEdit)
    EditText messageEdit;
    @BindView(R.id.messagesContainer)
    RecyclerView recyclerMessages;

    ChatFragmentAdapter adapter;
    private View rootView;
    private Subscription subscription;
    private Subscription newMessageSubscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.getComponent(IMainActivityComponent.class).inject(this);
        presenter.onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.init(this);
        subscription = presenter.getMessageList().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).filter(response -> response.size() != 0).subscribe(this::setUserAdapter);
        newMessageSubscription = presenter.onReceiveMessage().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).flatMap(Observable::from).flatMap(response -> {
            Messages messages = new Messages();
            messages.setHeader(response.getHeaders().get("android-ticker-text"));
            Date date = new Date(response.getTimestamp());
            messages.setTimestamp(date);
            messages.setPublisher_id(response.getPublisherId());
            messages.setData(response.getData().toString());
            messages.setMessage_id(response.getMessageId());
            return Observable.just(messages);
        }).subscribe(response -> {
            adapter.insert(response);
            recyclerMessages.smoothScrollToPosition(0);
        });
    }

    @Override
    public void onPause() {
        subscription.unsubscribe();
        newMessageSubscription.unsubscribe();
        super.onPause();
    }

    @OnClick(R.id.chatSendButton)
    public void sendClick (View view){
        presenter.onSendMessage(messageEdit);
    }

    private void setUserAdapter(List<Messages> users) {
        adapter = new ChatFragmentAdapter(users, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerMessages.setLayoutManager(linearLayoutManager);
        recyclerMessages.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerMessages.setAdapter(scaleInAnimationAdapter);
    }

}
