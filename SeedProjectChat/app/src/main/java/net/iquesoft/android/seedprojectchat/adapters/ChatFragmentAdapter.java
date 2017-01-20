package net.iquesoft.android.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import net.iquesoft.android.seedprojectchat.model.ChatUser;
import net.iquesoft.android.seedprojectchat.model.Messages;
import net.iquesoft.android.seedprojectchat.util.StringToMapUtils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class ChatFragmentAdapter extends RecyclerView.Adapter<ChatFragmentAdapter.ViewHolder> {


    private static final int USER = 1;
    private static final int OPPONENT = 2;

    private List<Messages> messageList;
    private Context context;
    private FragmentManager fragmentManager;

    public List<Messages> getMessageList() {
        return messageList;
    }

    public ChatFragmentAdapter(List<Messages> messageList, Context context, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.messageList = null;
        this.messageList = new ArrayList<>(messageList);
        this.context = context;
    }

    @Override
    public ChatFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == USER) {
            v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_chat_raw, parent, false);
            return new ChatFragmentAdapter.ViewHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.fragment_chat_raw_left, parent, false);
            return new ChatFragmentAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(ChatFragmentAdapter.ViewHolder holder, int position) {
        Messages messages = messageList.get(position);
        Map<String, String> messageMap = new HashMap<>(StringToMapUtils.splitToMap(StringEscapeUtils.unescapeJava(messages.getData()), ", ", "="));
        if (messageMap.containsKey("message")) {
            holder.txtMessage.setText(messageMap.get("message"));
            messageMap.remove("message");
            if (messageMap.size() != 0) {
                setMessageImage(holder, messageMap);
            }
        } else {
            setMessageImage(holder, messageMap);
        }

        try {
            if (!messages.getRead() & getItemViewType(position) == OPPONENT) {
                holder.content.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), net.iquesoft.android.seedprojectchat.R.color.grey, null));
            } else {
                holder.content.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), net.iquesoft.android.seedprojectchat.R.color.material_light, null));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        if (messages.getTimestamp() != null) {
            holder.txtInfo.setText(messages.getTimestamp().toString());

            Backendless.UserService.findById(messages.getPublisher_id(), new BackendlessCallback<BackendlessUser>() {
                @Override
                public void handleResponse(BackendlessUser backendlessUser) {
                    Uri uri = null;
                    try {
                        uri = Uri.parse(backendlessUser.getProperty(ChatUser.PHOTO).toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    Picasso.with(context).load(uri).placeholder(net.iquesoft.android.seedprojectchat.R.drawable.placeholder).error(net.iquesoft.android.seedprojectchat.R.drawable.error).into(holder.cimUserImage);
                }
            });
        }
    }

    public void setMessageImage(ChatFragmentAdapter.ViewHolder holder, Map<String, String> messageImageMap) {
        MessageDataAdapter adapter = new MessageDataAdapter(messageImageMap, context, fragmentManager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        if (messageList != null) {
            return messageList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messageList.get(position);
        String myObjectId = Backendless.UserService.CurrentUser().getObjectId();
        if (messages != null) {
            if (myObjectId.equals(messages.getPublisher_id())) {
                return USER;
            } else {
                return OPPONENT;
            }
        }
        return USER;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        EmojiconTextView txtMessage;
        TextView txtInfo;
        LinearLayout content;
        LinearLayout contentWithBG;
        CircularImageView cimUserImage;
        RecyclerView recyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.recycler_added_data);
            txtMessage = (EmojiconTextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.txtMessage);
            content = (LinearLayout) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.content);
            contentWithBG = (LinearLayout) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.contentWithBackground);
            txtInfo = (TextView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.txtInfo);
            cimUserImage = (CircularImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.cim_user_image);
        }
    }

    // Insert a new item to the RecyclerView
    public void insert(Messages message) {
        messageList.add(0, message);
        notifyItemInserted(0);
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        messageList.remove(index);
        notifyItemRemoved(index);
    }
}
