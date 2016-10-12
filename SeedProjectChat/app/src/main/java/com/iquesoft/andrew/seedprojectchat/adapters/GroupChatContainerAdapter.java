package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by andru on 9/22/2016.
 */

public class GroupChatContainerAdapter extends RecyclerView.Adapter<GroupChatContainerAdapter.ViewHolder> {

    private List<GroupChat> groupChatList;
    private Context context;

    public GroupChatContainerAdapter(@NonNull List<GroupChat> groupChatList, Context context){
        this.context = context;
        this.groupChatList = groupChatList;
    }
    @Override
    public GroupChatContainerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_group_chat_container_raw, parent, false);
        return new GroupChatContainerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GroupChatContainerAdapter.ViewHolder holder, int position) {
        GroupChat curentGroupChat = groupChatList.get(position);
        holder.tvChatName.setText(curentGroupChat.getChanel());
        if (curentGroupChat.getMessages().size() != 0){
            String lastMessageOwnerID = curentGroupChat.getMessages().get(0).getOwnerId();
            BackendlessUser user = null;
            for (BackendlessUser listUser : groupChatList.get(position).getUsers() ) {
                if (listUser.getUserId().equals(lastMessageOwnerID)){
                    user = listUser;
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
                Observable.just(curentGroupChat.getMessages()).flatMap(Observable::from).observeOn(Schedulers.io()).toSortedList((messages, messages2) -> Long.valueOf(messages2.getTimestamp().getTime()).compareTo(messages.getTimestamp().getTime()))
                        .subscribe(response -> {
                            holder.tvLastMessage.setText(response.get(0).getData());
                            holder.tvLastMessageDate.setText(response.get(0).getTimestamp().toString());
                        });
            } else {
            }
        }
    }

    @Override
    public int getItemCount() {
        return groupChatList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        CircularImageView cimOnline;
        TextView tvChatName;
        TextView tvLastMessage;
        TextView tvLastMessageDate;

        public ViewHolder(View itemView) {
            super(itemView);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            cimOnline = (CircularImageView) itemView.findViewById(R.id.cim_online);
            tvChatName = (TextView) itemView.findViewById(R.id.tv_group_chat_name);
            tvLastMessage = (TextView) itemView.findViewById(R.id.tv_last_message);
            tvLastMessageDate = (TextView) itemView.findViewById(R.id.tv_last_message_date);
        }
    }
}
