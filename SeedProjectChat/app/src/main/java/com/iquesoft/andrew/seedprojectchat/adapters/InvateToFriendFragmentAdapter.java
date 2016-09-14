package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andru on 9/1/2016.
 */

public class InvateToFriendFragmentAdapter extends RecyclerView.Adapter<InvateToFriendFragmentAdapter.ViewHolder> {

    private List<Friends> users;
    private Context context;

    public InvateToFriendFragmentAdapter(List<Friends> users, Context context){
        this.context = context;
        this.users = users;
    }
    @Override
    public InvateToFriendFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_invite_to_friends_fragment_row, parent, false);
        return new InvateToFriendFragmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InvateToFriendFragmentAdapter.ViewHolder holder, int position) {
        Friends curentFriend = users.get(position);
        BackendlessUser user = users.get(position).getUser_one();
        if (user.getProperty(ChatUser.PHOTO) != null){
            Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
            Picasso.with(context).load(uri).fit().placeholder(R.drawable.seed_logo).into(holder.cimUserImage);
        }
        if (user.getProperty(ChatUser.NAME) != null){
            holder.tvUserName.setText(user.getProperty(ChatUser.NAME).toString());
        }
        holder.tvUserEMail.setText(user.getEmail());

        holder.accept.setOnClickListener(view -> {
            curentFriend.setStatus(2);
            curentFriend.setSubtopic(curentFriend.getUser_one().getProperty(ChatUser.NAME) + "_with_" + curentFriend.getUser_two().getProperty(ChatUser.NAME));
            curentFriend.saveAsync(new DefaultBackendlessCallback<Friends>(context){
                @Override
                public void handleResponse(Friends response) {
                    super.handleResponse(response);
                    remove(position);
                }
            });
        });
        holder.denial.setOnClickListener(view -> removeFromBackendless(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        Button accept;
        Button denial;
        TextView tvUserName;
        TextView tvUserEMail;

        public ViewHolder(View itemView) {
            super(itemView);
            accept = (Button) itemView.findViewById(R.id.btn_accept_invite);
            denial = (Button) itemView.findViewById(R.id.btn_accept_denial);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            tvUserEMail = (TextView) itemView.findViewById(R.id.tv_user_email);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }

    // Insert a new item to the RecyclerView
    public void insert(Friends friends, int position) {
        users.add(position, friends);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        users.remove(index);
        notifyItemRemoved(index);
    }

    public void removeFromBackendless(int index){
        Friends friends = users.get(index);
        friends.removeAsync(new DefaultBackendlessCallback<Long>(context){
            @Override
            public void handleResponse(Long response) {
                super.handleResponse(response);
                users.remove(index);
                notifyItemRemoved(index);
            }
        });
    }
}
