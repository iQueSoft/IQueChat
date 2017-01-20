package net.iquesoft.android.seedprojectchat.view.classes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.backendless.exceptions.BackendlessFault;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import net.iquesoft.android.seedprojectchat.Manifest;
import net.iquesoft.android.seedprojectchat.adapters.ChatFragmentAdapter;
import net.iquesoft.android.seedprojectchat.adapters.PreviewPhotoAdapter;
import net.iquesoft.android.seedprojectchat.common.BaseFragment;
import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.common.DefaultsBackendlessKey;
import net.iquesoft.android.seedprojectchat.di.components.IMainActivityComponent;
import net.iquesoft.android.seedprojectchat.model.GroupChat;
import net.iquesoft.android.seedprojectchat.model.Messages;
import net.iquesoft.android.seedprojectchat.presenter.classes.fragments.GroupChatFragmentPresenter;
import net.iquesoft.android.seedprojectchat.util.MessageUtil;
import net.iquesoft.android.seedprojectchat.view.classes.activity.MainActivity;
import net.iquesoft.android.seedprojectchat.view.interfaces.fragments.IGroupChatFragment;

import java.util.ArrayList;
import java.util.List;

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

public class GroupChatFragment extends BaseFragment implements IGroupChatFragment, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @InjectPresenter
    GroupChatFragmentPresenter presenter;

    @BindView(net.iquesoft.android.seedprojectchat.R.id.messageEdit)
    EmojiconEditText messageEdit;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.messagesContainer)
    RecyclerView recyclerMessages;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.swipe_refresh_message)
    SwipyRefreshLayout swipeRefreshMessage;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.emj_container)
    FrameLayout emojiContainer;
    @BindView(net.iquesoft.android.seedprojectchat.R.id.scroll_files)
    RecyclerView recyclerPhotoPreview;

    private PreviewPhotoAdapter previewPhotoAdapter;
    private Boolean emjFlag = false;
    private MainActivity mainActivity;
    private ChatFragmentAdapter adapter;
    private GroupChat curentGroupChat;
    private View rootView;
    private ArrayList<String> photoPaths = new ArrayList<>();
    private ArrayList<String> serverPhotoPaths = new ArrayList<>();
    private ArrayList<String> docPaths = new ArrayList<>();
    private ArrayList<String> serverDocPaths = new ArrayList<>();
    private final int PERMISSION_REQUEST_CODE = 1;

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
            rootView = inflater.inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_chat, container, false);
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
        Backendless.Messaging.registerDevice(DefaultsBackendlessKey.GOOGLE_PROJECT_ID, curentGroupChat.getObjectId(), new DefaultBackendlessCallback<Void>(){
            @Override
            public void handleResponse(Void response) {
                Log.d("registerDevice", "response ok");
                super.handleResponse(response);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("registerDevice", "response fail");
                super.handleFault(fault);
            }
        });
        swipeRefreshMessage.setOnRefreshListener(direction -> {
            presenter.refreshMessageList();
            swipeRefreshMessage .setRefreshing(false);
        });
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        messageEdit.setUseSystemDefault(useSystemDefault);
        getChildFragmentManager()
                .beginTransaction()
                .replace(net.iquesoft.android.seedprojectchat.R.id.emj_container, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.button_choice_image)
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ArrayList<String> ll = new ArrayList<>();
            FilePickerBuilder.getInstance().setMaxCount(10)
                    .setSelectedFiles(ll)
                    .setActivityTheme(net.iquesoft.android.seedprojectchat.R.style.MyTheme)
                    .pickDocument(this);
        } else {
            requestPermission(1);
        }
    }

    public void photoSelector() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ArrayList<String> ll = new ArrayList<>();
            FilePickerBuilder.getInstance().setMaxCount(10)
                    .setSelectedFiles(ll)
                    .setActivityTheme(net.iquesoft.android.seedprojectchat.R.style.MyTheme)
                    .pickPhoto(this);
        } else {
            requestPermission(2);
        }
    }

    private void requestPermission(int requestCode){
        ActivityCompat.requestPermissions(getActivity(),
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                documentSelector();
                break;
            case 2:
                photoSelector();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.chatSendButton)
    public void sendClick (){
        MessageUtil.sendClick(serverPhotoPaths, serverDocPaths, getActivity(), photoPaths, docPaths,
                messageEdit, presenter.getCurentGroupChat().getObjectId(), previewPhotoAdapter);
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.messageEdit)
    public void setReadClick(){
        presenter.setRead(adapter, true);
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
        inflater.inflate(net.iquesoft.android.seedprojectchat.R.menu.group_chat_fragment_menu, menu);
        String ownerId = presenter.getCurentGroupChat().getOwner().getUserId();
        if (curentUserId.equals(ownerId)){
            MenuItem clearHistory = menu.findItem(net.iquesoft.android.seedprojectchat.R.id.action_clear_history);
            clearHistory.setVisible(true);
            menu.findItem(net.iquesoft.android.seedprojectchat.R.id.action_delete_chat).setVisible(true);
        } else {
            MenuItem clearHistory = menu.findItem(net.iquesoft.android.seedprojectchat.R.id.action_clear_history);
            clearHistory.setVisible(false);
            menu.findItem(net.iquesoft.android.seedprojectchat.R.id.action_delete_chat).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case net.iquesoft.android.seedprojectchat.R.id.action_leave:
                presenter.liveChat(mainActivity);
                break;
            case net.iquesoft.android.seedprojectchat.R.id.action_delete_chat:
                presenter.getCurentGroupChat().removeAsync(new DefaultBackendlessCallback<>());
                break;
            case net.iquesoft.android.seedprojectchat.R.id.action_edit_chat:
                presenter.createDialogGroupChat(getActivity(), getActivity().getLayoutInflater());
                break;
            case net.iquesoft.android.seedprojectchat.R.id.action_clear_history:
                presenter.clearHistory(adapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(net.iquesoft.android.seedprojectchat.R.id.button_emoji)
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
        adapter = new ChatFragmentAdapter(messages, getActivity(), getActivity().getSupportFragmentManager());
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
