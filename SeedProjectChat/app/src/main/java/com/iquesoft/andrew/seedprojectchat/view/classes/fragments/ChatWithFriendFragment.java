package com.iquesoft.andrew.seedprojectchat.view.classes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.adapters.ChatFragmentAdapter;
import com.iquesoft.andrew.seedprojectchat.adapters.PreviewPhotoAdapter;
import com.iquesoft.andrew.seedprojectchat.common.BaseFragment;
import com.iquesoft.andrew.seedprojectchat.di.components.IMainActivityComponent;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.presenter.classes.fragments.ChatWithFriendFragmentPresenter;
import com.iquesoft.andrew.seedprojectchat.util.MessageUtil;
import com.iquesoft.andrew.seedprojectchat.util.UploadFileUtil;
import com.iquesoft.andrew.seedprojectchat.view.interfaces.fragments.IChatWithFriendFragment;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;

/**
 * Created by andru on 9/15/2016.
 */

public class ChatWithFriendFragment extends BaseFragment implements IChatWithFriendFragment, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @InjectPresenter
    ChatWithFriendFragmentPresenter presenter;

    @BindView(R.id.messageEdit)
    EmojiconEditText messageEdit;
    @BindView(R.id.messagesContainer)
    RecyclerView recyclerMessages;
    @BindView(R.id.swipe_refresh_message)
    SwipyRefreshLayout swipyRefreshLayout;
    @BindView(R.id.emj_container)
    FrameLayout emojiContainer;
    @BindView(R.id.scroll_files)
    RecyclerView recyclerPhotoPreview;


    private ChatFragmentAdapter adapter;
    private PreviewPhotoAdapter previewPhotoAdapter;
    private View rootView;
    private Friends friend;
    private Boolean emjFlag = false;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> serverPhotoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<String> serverDocPaths = new ArrayList<>();


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
        if (presenter.getFriends() == null) {
            presenter.setFriends(friend);
        }
        swipyRefreshLayout.setOnRefreshListener(direction -> {
            presenter.refreshMessageList();
            swipyRefreshLayout.setRefreshing(false);
        });
    }

    public void setFriend(Friends friend) {
        this.friend = friend;
    }

    @OnClick(R.id.chatSendButton)
    public void sendClick() {
        MessageUtil.sendClick(serverPhotoPaths, serverDocPaths, getActivity(), photoPaths, docPaths, messageEdit, presenter.getFriends().getObjectId(), previewPhotoAdapter);
//        serverPhotoPaths.clear();
//        if (photoPaths.size() != 0){
//            UploadFileUtil.getAndCompressImageWithUri(photoPaths,getActivity()).subscribe(response -> {
//                serverPhotoPaths.add(response);
//                if (serverPhotoPaths.size() == photoPaths.size()){
//                    photoPaths.clear();
//                    Map<String, String> messageMap = new HashMap<>();
//                    messageMap.put("message", messageEdit.getText().toString());
//                    for (int i = 0; i<serverPhotoPaths.size(); i++){
//                        String imageUri = serverPhotoPaths.get(i);
//                        messageMap.put("image"+i, imageUri);
//                    }
//                    MessageUtil.onSendMessage(messageEdit, messageMap, getActivity(), presenter.getPublishOptions(), presenter.getFriends().getObjectId());
//                    previewPhotoAdapter.clear();
//                }
//            });
//        }else if (docPaths.size() != 0){
//           serverDocPaths.clear();
//            UploadFileUtil.uploadFilesToServer(docPaths, getActivity()).subscribe(response -> {
//                serverDocPaths.add(response);
//                if (serverDocPaths.size() == docPaths.size()){
//                    Map<String, String> messageMap = new HashMap<>();
//                    messageMap.put("message", messageEdit.getText().toString());
//                    for (int i = 0; i<serverDocPaths.size(); i++){
//                        String imageUri = serverDocPaths.get(i);
//                        messageMap.put("document"+i, imageUri);
//                    }
//                    Log.d("document", serverDocPaths.toString());
//                    MessageUtil.onSendMessage(messageEdit, messageMap, getActivity(), presenter.getPublishOptions(), presenter.getFriends().getObjectId());
//                    previewPhotoAdapter.clear();
//                }
//            });
//        } else {
//            Map<String, String> messageMap = new HashMap<>();
//            messageMap.put("message", messageEdit.getText().toString());
//            MessageUtil.onSendMessage(messageEdit, messageMap, getActivity(), presenter.getPublishOptions(), presenter.getFriends().getObjectId());
//        }
    }

    public void updateLastVisibleMessage(Messages message) {
        adapter.insert(message);
        recyclerMessages.smoothScrollToPosition(0);
    }

    public void setUserAdapter(List<Messages> messagesList) {
        adapter = new ChatFragmentAdapter(messagesList, getActivity(), getActivity().getSupportFragmentManager());
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

    private void setEmojiconFragment(boolean useSystemDefault) {
        messageEdit.setUseSystemDefault(useSystemDefault);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.emj_container, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @OnClick(R.id.button_choice_image)
    public void choiceImgClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Photo", (dialog, which) -> photoSelector())
                .setNegativeButton("Documents", (dialog, which) -> documentSelector());
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    public void rvPreview(List<String> uriList, Boolean isPhoto){
        previewPhotoAdapter = new PreviewPhotoAdapter(uriList, getActivity(), isPhoto);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerPhotoPreview.setLayoutManager(layoutManager);
        recyclerPhotoPreview.setAdapter(previewPhotoAdapter);
    }

    public void documentSelector() {
        ArrayList<String> ll = new ArrayList<>();
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setSelectedFiles(ll)
                .setActivityTheme(R.style.MyTheme)
                .pickDocument(this);
    }

    public void photoSelector() {
        ArrayList<String> ll = new ArrayList<>();
        FilePickerBuilder.getInstance().setMaxCount(10)
                .setSelectedFiles(ll)
                .setActivityTheme(R.style.MyTheme)
                .pickPhoto(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    photoPaths = new ArrayList<>();
                    photoPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS));
                    rvPreview(photoPaths, true);
                }
                break;
            case FilePickerConst.REQUEST_CODE_DOC:
                if(resultCode== Activity.RESULT_OK && data!=null)
                {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    rvPreview(docPaths, false);
                    Log.d("file", docPaths.toString());
                }
                break;
        }
    }

    @OnClick(R.id.button_emoji)
    public void emojiClick() {
        if (!emjFlag) {
            emjFlag = true;
            emojiContainer.setVisibility(View.VISIBLE);
            setEmojiconFragment(false);
        } else {
            emjFlag = false;
            emojiContainer.setVisibility(View.GONE);
        }
    }
}
