package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.iquesoft.andrew.seedprojectchat.model.GroupChat;
import com.iquesoft.andrew.seedprojectchat.model.Messages;
import com.iquesoft.andrew.seedprojectchat.util.StringToMapUtils;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by andru on 11/11/2016.
 */

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.ViewHolder> {

    List<Object> objectList;
    private Context context;

    public MainFragmentAdapter(List<Object> objectList, Context context){
        this.objectList = objectList;
        this.context = context;
    }
    @Override
    public MainFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_group_chat_container_raw, parent, false);
        return new MainFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainFragmentAdapter.ViewHolder holder, int position) {
        Friends friends = null;
        GroupChat groupChat;
        try {
           friends = (Friends) objectList.get(position);
        } catch (Throwable e){
            groupChat = (GroupChat) objectList.get(position);
        }
        if (friends != null){
            holder.tvChatName.setVisibility(View.GONE);
            holder.tvLastMessageDate.setVisibility(View.GONE);
            Messages messages = null;
            BackendlessUser user = null;
            Map<String, String> messageData = null;
            if (friends.getMessages().size() != 0){
                messages = friends.getMessages().get(0);
                if (messages.getPublisher_id().equals(friends.getUser_one().getUserId())){
                    user = friends.getUser_one();
                } else {
                    user = friends.getUser_two();
                }
                messageData = StringToMapUtils.splitToMap(messages.getData(),", ", "=");
                if (messageData.containsKey("message")){
                    holder.tvLastMessage.setText(messageData.get("message"));
                }
            }
            try {
                Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
                Picasso.with(context).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.cimUserImage);
            } catch (Throwable e){
                e.printStackTrace();
            }

            try {
                if (user.getProperty(ChatUser.ONLINE).equals("true")){
                    holder.cimOnline.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
                } else {
                    holder.cimOnline.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
                }
            } catch (Throwable e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
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
