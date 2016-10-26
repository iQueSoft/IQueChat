package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iquesoft.andrew.seedprojectchat.R;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by andru on 10/26/2016.
 */

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.ViewHolder> {

    private Map<String, String> uriMap;
    private Context context;

    public MessageDataAdapter(Map<String, String> uriMap, Context context) {
        this.uriMap = uriMap;
        this.context = context;
    }

    @Override
    public MessageDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_row_in_mesage, parent, false);
        return new MessageDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageDataAdapter.ViewHolder holder, int position) {
        if (uriMap.containsKey("image0")){
            Uri uri = Uri.parse(uriMap.get("image" + position));
            Picasso.with(context).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.image);
        } else if (uriMap.containsKey("document0")){
            holder.image.setImageDrawable(context.getResources().getDrawable((R.drawable.document)));
        }

    }

    @Override
    public int getItemCount() {
        if (uriMap != null) {
            return uriMap.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

//    // Insert a new item to the RecyclerView
//    public void insert(Messages message) {
//        messageList.add(0, message);
//        notifyItemInserted(0);
//    }
//
//    // Remove a RecyclerView item containing the Data object
//    public void remove(int index) {
//        messageList.remove(index);
//        notifyItemRemoved(index);
//    }
}

