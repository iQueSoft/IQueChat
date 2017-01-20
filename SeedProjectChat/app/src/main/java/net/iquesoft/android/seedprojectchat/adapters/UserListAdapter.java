package net.iquesoft.android.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import net.iquesoft.android.seedprojectchat.common.DefaultBackendlessCallback;
import net.iquesoft.android.seedprojectchat.model.ChatUser;
import net.iquesoft.android.seedprojectchat.model.Friends;

import java.util.List;

import io.huannguyen.swipetodeleterv.STDAdapter;

// TODO: Fix Delete friend
public class UserListAdapter extends STDAdapter<Friends> {

    private List<Friends> friendses;
    private Context context;



    public UserListAdapter(List<Friends> users, Context context){
        super(users);
        this.context = context;
        this.friendses = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_friends_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BackendlessUser user;
        if (Backendless.UserService.CurrentUser().getObjectId().equals(friendses.get(position).getUser_one().getObjectId())){
            user = friendses.get(position).getUser_two();
        } else {
            user = friendses.get(position).getUser_one();
        }
        if (user.getProperty(ChatUser.PHOTO) != null){
            Uri uri = Uri.parse(user.getProperty(ChatUser.PHOTO).toString());
            Picasso.with(context).load(uri).fit().placeholder(net.iquesoft.android.seedprojectchat.R.drawable.seed_logo).into(((UserListAdapter.ViewHolder) holder).cimUserImage);
        }
        if (user.getProperty(ChatUser.NAME) != null){
            ((UserListAdapter.ViewHolder) holder).tvUserName.setText(user.getProperty(ChatUser.NAME).toString());
        }
        Boolean online = (Boolean) user.getProperty(ChatUser.ONLINE);
        if (online){
            ((UserListAdapter.ViewHolder) holder).isOnline.setImageDrawable(context.getResources().getDrawable(net.iquesoft.android.seedprojectchat.R.drawable.online));
        } else {
            ((UserListAdapter.ViewHolder) holder).isOnline.setImageDrawable(context.getResources().getDrawable(net.iquesoft.android.seedprojectchat.R.drawable.offline));
        }
        ((UserListAdapter.ViewHolder) holder).tvUserEMail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return friendses.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircularImageView cimUserImage;
        CircularImageView isOnline;
        TextView tvUserName;
        TextView tvUserEMail;

        public ViewHolder(View itemView) {
            super(itemView);
            isOnline = (CircularImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.cim_online);
            cimUserImage = (CircularImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.cim_user_image);
            tvUserEMail = (TextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.tv_last_message);
            tvUserName = (TextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.tv_group_chat_name);
        }
    }

    // Insert a new item to the RecyclerView
    public void insert(Friends friends, int position) {
        friends.saveAsync(new DefaultBackendlessCallback<Friends>(){
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
        friendses.get(index).removeAsync(new DefaultBackendlessCallback<Long>(){
            @Override
            public void handleResponse(Long response) {
                super.handleResponse(response);
                friendses.remove(index);
                notifyItemRemoved(index);
            }
        });
    }

    public Friends getCurentFriend(int position){
        return friendses.get(position);
    }
}
