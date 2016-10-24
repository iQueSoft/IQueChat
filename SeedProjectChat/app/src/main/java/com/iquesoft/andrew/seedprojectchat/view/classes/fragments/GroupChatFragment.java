package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.backendless.Backendless;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * Created by andru on 9/23/2016.
 */

public class GroupChatFragment extends BaseFragment implements IGroupChatFragment, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener  {

    @InjectPresenter
    GroupChatFragmentPresenter presenter;

    @BindView(R.id.messageEdit)
    EmojiconEditText messageEdit;
    @BindView(R.id.messagesContainer)
    RecyclerView recyclerMessages;
    @BindView(R.id.swipe_refresh_message)
    SwipyRefreshLayout swipeRefreshMessage;
    @BindView(R.id.emj_container)
    FrameLayout emojiContainer;

    private Boolean emjFlag = false;
    private MainActivity mainActivity;
    private ChatFragmentAdapter adapter;
    private GroupChat curentGroupChat;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        Log.d("check", "OnCreate");
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

    private void setEmojiconFragment(boolean useSystemDefault) {
        messageEdit.setUseSystemDefault(useSystemDefault);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.emj_container, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }




    @OnClick(R.id.chatSendButton)
    public void sendClick (){
        try {
            presenter.onSendMessage(messageEdit);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        String curentUserId = Backendless.UserService.CurrentUser().getUserId();
        menu.clear();
        inflater.inflate(R.menu.group_chat_fragment_menu, menu);
        if (curentUserId.equals(presenter.getCurentGroupChat().getOwner().getUserId())){
            MenuItem clearHistory = menu.findItem(R.id.action_clear_history);
            clearHistory.setVisible(true);
            menu.findItem(R.id.action_delete_chat).setVisible(true);
        } else {
            MenuItem clearHistory = menu.findItem(R.id.action_clear_history);
            clearHistory.setVisible(false);
            menu.findItem(R.id.action_delete_chat).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_leave:
                presenter.liveChat(mainActivity);
                break;
            case R.id.action_edit_chat:
                presenter.createDialogGroupChat(getActivity(), getActivity().getLayoutInflater());
                break;
            case R.id.action_clear_history:
                presenter.getCurentGroupChat().getMessages().clear();
                presenter.getCurentGroupChat().saveAsync(new BackendlessCallback<GroupChat>() {
                    @Override
                    public void handleResponse(GroupChat groupChat) {
                        adapter.getMessageList().clear();
                        adapter.notifyDataSetChanged();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_emoji)
    public void emojiClick(){
        if (!emjFlag){
            emjFlag = true;
            emojiContainer.setVisibility(View.VISIBLE);
            setEmojiconFragment(false);
        } else {
            emjFlag = false;
            emojiContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        presenter.saveCurentChat();
        super.onPause();
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

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(messageEdit, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(messageEdit);
    }
}
