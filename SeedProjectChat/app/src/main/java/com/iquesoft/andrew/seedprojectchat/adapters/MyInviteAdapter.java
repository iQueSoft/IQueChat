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
 * Created by andru on 9/5/2016.
 */

public class MyInviteAdapter extends RecyclerView.Adapter<MyInviteAdapter.ViewHolder> {

    private List<Friends> users;
    private Context context;

    public MyInviteAdapter(List<Friends> users, Context context){
        this.context = context;
        this.users = users;
    }
    @Override
    public MyInviteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_invate_raw, parent, false);
        return new MyInviteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyInviteAdapter.ViewHolder holder, int position) {
        Friends curentFriend = users.get(position);
        BackendlessUser user = users.get(position).getUser_two();
        if (user != null){
            if (user.getProperty(ChatUser.PHOTO) != null){
                Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
                Picasso.with(context).load(uri).fit().placeholder(R.drawable.seed_logo).into(holder.cimUserImage);
            }
            if (user.getProperty(ChatUser.NAME) != null){
                holder.tvUserName.setText(user.getProperty(ChatUser.NAME).toString());
            }
            holder.tvUserEMail.setText(user.getEmail());

            holder.denial.setOnClickListener(view -> curentFriend.removeAsync(new DefaultBackendlessCallback<Long>(){
                @Override
                public void handleResponse(Long response) {
                    users.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    super.handleResponse(response);
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        Button denial;
        TextView tvUserName;
        TextView tvUserEMail;

        public ViewHolder(View itemView) {
            super(itemView);
            denial = (Button) itemView.findViewById(R.id.btn_revocation);
            cimUserImage = (CircularImageView) itemView.findViewById(R.id.cim_user_image);
            tvUserEMail = (TextView) itemView.findViewById(R.id.tv_last_message);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_group_chat_name);
        }
    }
}
