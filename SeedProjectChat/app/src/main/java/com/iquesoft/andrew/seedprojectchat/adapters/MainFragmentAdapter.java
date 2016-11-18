package com.iquesoft.andrew.seedprojectchat.adapters;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.model.BaseChatModel;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.util.StringToMapUtils;
import com.iquesoft.andrew.seedprojectchat.view.classes.activity.MainActivity;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.ChatWithFriendFragment;
import com.iquesoft.andrew.seedprojectchat.view.classes.fragments.GroupChatFragment;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andru on 11/11/2016.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    @Inject
    ChatWithFriendFragment chatWithFriendFragment;

    @Inject
    GroupChatFragment groupChatFragment;

    private List<BaseChatModel> objectList;
    private String curentUserId = Backendless.UserService.CurrentUser().getObjectId();
    private MainActivity mainActivity;
    private Friends friends = null;
    private GroupChat groupChat = null;
    private BackendlessUser user = null;

    public MainFragmentAdapter(List<BaseChatModel> objectList, MainActivity mainActivity) {
        this.objectList = null;
        this.objectList = objectList;
        this.mainActivity = mainActivity;
        mainActivity.getComponent().inject(MainFragmentAdapter.this);
    }

    @Override
    public MainFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_group_chat_container_raw, parent, false);
        return new MainFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainFragmentAdapter.ViewHolder holder, int position) {
        try {
            friends = null;
            friends = (Friends) objectList.get(position);
            isFriend(holder);
        } catch (Throwable e) {
            groupChat = (GroupChat) objectList.get(position);
            isGroupChat(holder);
        }

    }

    private void isGroupChat(MainFragmentAdapter.ViewHolder holder) {
        if (groupChat != null) {
            Observable.from(groupChat.getMessages()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .toSortedList((messages1, messages2) -> Long.valueOf(messages2.getTimestamp().getTime())
                            .compareTo(messages1.getTimestamp().getTime()))
                    .subscribe(response -> {
                        groupChatAdapter(holder, response.get(0));
                    });
            holder.tvChatName.setText(groupChat.getChatName());
        }
    }

    private void groupChatAdapter(MainFragmentAdapter.ViewHolder holder, Messages messages) {
        if (messages.getPublisher_id().equals(groupChat.getOwner().getUserId())) {
            user = groupChat.getOwner();
            setUserPhoto(holder);
        } else {
            Observable.from(groupChat.getUsers()).filter(response -> messages.getPublisher_id().equals(response.getUserId()))
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        user = response;
                        setUserPhoto(holder);
                    });
        }
        setLastMessage(holder, messages);
        holder.relativeLayout.setOnClickListener(v -> {
            groupChatFragment.setCurentGroupChat((GroupChat) objectList.get(holder.getAdapterPosition()));
            mainActivity.replaceFragment(groupChatFragment);
        });
    }

    private void isFriend(MainFragmentAdapter.ViewHolder holder) {
        if (friends != null) {
            Observable.from(friends.getMessages()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .toSortedList((messages1, messages2) -> Long.valueOf(messages2.getTimestamp().getTime())
                            .compareTo(messages1.getTimestamp().getTime()))
                    .subscribe(response -> {
                        friendAdapter(holder, response.get(0));
                    });
            if (curentUserId.equals(friends.getUser_one().getUserId())) {
                holder.tvChatName.setText(friends.getUser_two().getProperty(ChatUser.NAME).toString());
            } else {
                holder.tvChatName.setText(friends.getUser_one().getProperty(ChatUser.NAME).toString());
            }

        }
    }

    private void friendAdapter(MainFragmentAdapter.ViewHolder holder, Messages messages) {
        if (messages.getPublisher_id().equals(friends.getUser_one().getUserId())) {
            user = friends.getUser_one();
        } else {
            user = friends.getUser_two();
        }
        setLastMessage(holder, messages);
        setUserPhoto(holder);
        holder.relativeLayout.setOnClickListener(v -> {
            chatWithFriendFragment.setFriend((Friends) objectList.get(holder.getAdapterPosition()));
            mainActivity.replaceFragment(chatWithFriendFragment);
        });
    }

    private void setUserPhoto(MainFragmentAdapter.ViewHolder holder){
        try {
            Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
            Picasso.with(mainActivity.getBaseContext()).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.cimUserImage);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            if (user.getProperty(ChatUser.ONLINE).toString().equals("true")) {
                holder.cimOnline.setImageDrawable(mainActivity.getBaseContext().getResources().getDrawable(R.drawable.online));
            } else {
                holder.cimOnline.setImageDrawable(mainActivity.getBaseContext().getResources().getDrawable(R.drawable.offline));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void setLastMessage(MainFragmentAdapter.ViewHolder holder, Messages messages){
        holder.tvLastMessageDate.setText(messages.getTimestamp().toString());
        Map<String, String> messageData = StringToMapUtils.splitToMap(StringEscapeUtils.unescapeJava(messages.getData()), ", ", "=");
        if (messageData.containsKey("message")){
            holder.tvLastMessage.setText(messageData.get("message"));
            messageData.remove("message");
            if (messageData.size() != 0){
                setMessageImage(holder, messageData);
            }
        } else {
            setMessageImage(holder, messageData);
        }
    }

    public void setMessageImage(MainFragmentAdapter.ViewHolder holder, Map<String, String> messageImageMap){
        MessageDataAdapter adapter = new MessageDataAdapter(messageImageMap, mainActivity.getBaseContext(), mainActivity.getSupportFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivity.getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        CircularImageView cimUserImage;
        CircularImageView cimOnline;
        TextView tvChatName;
        EmojiconTextView tvLastMessage;
        TextView tvLastMessageDate;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_group_chat_container_raw);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.relLayout);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            cimOnline = (CircularImageView) itemView.findViewById(R.id.cim_online);
            tvChatName = (TextView) itemView.findViewById(R.id.tv_group_chat_name);
            tvLastMessage = (EmojiconTextView) itemView.findViewById(R.id.tv_last_message);
            tvLastMessageDate = (TextView) itemView.findViewById(R.id.tv_last_message_date);
        }
    }

    // Insert a new item to the RecyclerView
//    public void insert(BackendlessUser user, int position) {
//        users.add(position, user);
//        notifyItemInserted(position);
//    }

    // Remove a RecyclerView item containing the Data object
//    public void remove(int index) {
//        users.remove(index);
//        notifyItemRemoved(index);
//    }
}
