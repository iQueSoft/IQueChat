package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.ChatFragmentAdapter;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.di.components.IMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.GroupChatFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IGroupChatFragment;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * Created by andru on 9/23/2016.
 */

public class GroupChatFragment extends BaseFragment implements IGroupChatFragment {

    @InjectPresenter
    GroupChatFragmentPresenter presenter;

    @BindView(R.id.messageEdit)
    EditText messageEdit;
    @BindView(R.id.messagesContainer)
    RecyclerView recyclerMessages;
    @BindView(R.id.swipe_refresh_message)
    SwipyRefreshLayout swipeRefreshMessage;

    private MainActivity mainActivity;
    private ChatFragmentAdapter adapter;
    private GroupChat curentGroupChat;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        mainActivity = (MainActivity) getActivity();
        if (presenter.isGroupChatNull()){
            presenter.setCurentGroupChat(curentGroupChat);
        }
    }


    @OnClick(R.id.chatSendButton)
    public void sendClick (){
        presenter.onSendMessage(messageEdit);
    }

    public void setCurentGroupChat(GroupChat groupChat){
        curentGroupChat = groupChat;
    }

    @Override
    public void showNewMessage(Messages messages){
        adapter.insert(messages);
        recyclerMessages.smoothScrollToPosition(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.group_chat_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_leave){
            if (Backendless.UserService.CurrentUser().getUserId().equals(curentGroupChat.getOwnerId())){
                curentGroupChat.removeAsync(new BackendlessCallback<Long>() {
                    @Override
                    public void handleResponse(Long aLong) {
                    }
                });
            } else {
                for (BackendlessUser user : curentGroupChat.getUsers()){
                    if (user.getUserId().equals(Backendless.UserService.CurrentUser().getUserId())){
                        curentGroupChat.getUsers().remove(user);
                        curentGroupChat.saveAsync(new BackendlessCallback<GroupChat>() {
                            @Override
                            public void handleResponse(GroupChat groupChat) {
                            }
                        });
                        mainActivity.setGroupChatContainer();
                        break;
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setMessageAdapter(List<Messages> messages) {
        adapter = new ChatFragmentAdapter(messages, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerMessages.setLayoutManager(linearLayoutManager);
        recyclerMessages.setItemAnimator(new ScaleInAnimator(new OvershootInterpolator(1f)));
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(adapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerMessages.setAdapter(scaleInAnimationAdapter);
    }
}
