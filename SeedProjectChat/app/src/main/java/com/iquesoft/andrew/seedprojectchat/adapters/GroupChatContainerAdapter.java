package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.util.StringToMapUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by andru on 9/22/2016.
 */

public class GroupChatContainerAdapter extends RecyclerView.Adapter<GroupChatContainerAdapter.ViewHolder> {

    private List<GroupChat> groupChatList;
    private Context context;
    private FragmentManager fragmentManager;

    public GroupChatContainerAdapter(@NonNull List<GroupChat> groupChatList, Context context, FragmentManager fragmentManager){
        this.context = context;
        this.groupChatList = groupChatList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public GroupChatContainerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_group_chat_container_raw, parent, false);
        return new GroupChatContainerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupChatContainerAdapter.ViewHolder holder, int position) {
        GroupChat curentGroupChat = groupChatList.get(position);
        holder.tvChatName.setText(curentGroupChat.getChatName());
        if (curentGroupChat.getMessages() != null ){
            if (curentGroupChat.getMessages().size() != 0){
                String lastMessageOwnerID = curentGroupChat.getMessages().get(0).getOwnerId();
                BackendlessUser user = null;
                for (int i = 0; i < groupChatList.get(position).getUsers().size(); i++) {
                    BackendlessUser listUser = groupChatList.get(position).getUsers().get(i);
                    if (listUser.getUserId().equals(lastMessageOwnerID)){
                        user = listUser;
                        break;
                    } else if (i == groupChatList.get(position).getUsers().size() - 1 & user == null){
                        if (curentGroupChat.getOwner().getUserId().equals(lastMessageOwnerID)){
                            user = groupChatList.get(position).getOwner();
                            break;
                        }
                    }
                }
                if (user != null){
                    if (user.getProperty(ChatUser.PHOTO) != null){
                        String photo = (String) user.getProperty(ChatUser.PHOTO);
                        Uri uri = Uri.parse(photo);
                        Picasso.with(context).load(uri).placeholder(R.drawable.seed_logo).error(R.drawable.error).into(holder.cimUserImage);
                    }
                    Boolean online = (Boolean) user.getProperty(ChatUser.ONLINE);
                    if (online){
                        holder.cimOnline.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                    } else {
                        holder.cimOnline.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
                    }
                }
                Observable.just(curentGroupChat.getMessages()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(Observable::from)
                        .toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                        .subscribe(response -> {
                            Map<String, String> message = StringToMapUtils.splitToMap(StringEscapeUtils.unescapeJava(response.get(0).getData()),", ", "=");
                            if (message.containsKey("message")){
                                holder.tvLastMessage.setText(message.get("message"));
                                message.remove("message");
                                if (message.size() != 0){
                                    setMessageImage(holder, message);
                                }
                            } else {
                                setMessageImage(holder, message);
                            }
                            holder.tvLastMessageDate.setText(response.get(0).getTimestamp().toString());
                        });
            }else {
                setMessageGone(holder);
            }
        } else {
            setMessageGone(holder);
        }
    }

    public void setMessageImage(GroupChatContainerAdapter.ViewHolder holder, Map<String, String> messageImageMap){
        MessageDataAdapter adapter = new MessageDataAdapter(messageImageMap, context, fragmentManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    private void setMessageGone(ViewHolder viewHolder){
        viewHolder.tvLastMessage.setVisibility(View.GONE);
        viewHolder.tvLastMessageDate.setVisibility(View.GONE);
        viewHolder.cimUserImage.setVisibility(View.GONE);
        viewHolder.cimOnline.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        CircularImageView cimOnline;
        TextView tvChatName;
        EmojiconTextView tvLastMessage;
        TextView tvLastMessageDate;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.relLayout);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            cimOnline = (CircularImageView) itemView.findViewById(R.id.cim_online);
            tvChatName = (TextView) itemView.findViewById(R.id.tv_group_chat_name);
            tvLastMessage = (EmojiconTextView) itemView.findViewById(R.id.tv_last_message);
            tvLastMessageDate = (TextView) itemView.findViewById(R.id.tv_last_message_date);
        }
    }

    // Insert a new item to the RecyclerView
    public void insert(GroupChat groupChat, int position) {
        groupChatList.add(position, groupChat);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        groupChatList.remove(index);
        notifyItemRemoved(index);
    }
}
