package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.common.DefaultBackendlessCallback;
import com.iquesoft.andrew.seedprojectchat.model.ChatUser;
import com.iquesoft.andrew.seedprojectchat.model.Friends;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by andru on 8/30/2016.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<Friends> friendses;
    private Context context;

    public UserListAdapter(List<Friends> users, Context context){
        this.context = context;
        this.friendses = users;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_friends_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BackendlessUser user;
        if (Backendless.UserService.CurrentUser().getObjectId().equals(friendses.get(position).getUser_one().getObjectId())){
            user = friendses.get(position).getUser_two();
        } else {
            user = friendses.get(position).getUser_one();
        }
        if (user.getProperty(ChatUser.PHOTO) != null){
            Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
            Picasso.with(context).load(uri).fit().placeholder(R.drawable.seed_logo).into(holder.cimUserImage);
        }
        if (user.getProperty(ChatUser.NAME) != null){
            holder.tvUserName.setText(user.getProperty(ChatUser.NAME).toString());
        }
        holder.tvUserEMail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return friendses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        TextView tvUserName;
        TextView tvUserEMail;

        public ViewHolder(View itemView) {
            super(itemView);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            tvUserEMail = (TextView) itemView.findViewById(R.id.tv_user_email);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }

    // Insert a new item to the RecyclerView
    public void insert(Friends friends, int position) {
        friends.saveAsync(new DefaultBackendlessCallback<Friends>(context){
            @Override
            public void handleResponse(Friends response) {
                super.handleResponse(response);
                friendses.add(position, friends);
                notifyItemInserted(position);
            }
        });
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        friendses.get(index).removeAsync(new DefaultBackendlessCallback<Long>(context){
            @Override
            public void handleResponse(Long response) {
                super.handleResponse(response);
                friendses.remove(index);
                notifyItemRemoved(index);
            }
        });
    }
}
