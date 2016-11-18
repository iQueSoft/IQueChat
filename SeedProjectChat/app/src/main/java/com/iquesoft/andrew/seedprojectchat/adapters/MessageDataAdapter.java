package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iquesoft.andrew.seedprojectchat.R;
import com.iquesoft.andrew.seedprojectchat.util.SelectedUri;
import com.squareup.picasso.Picasso;
import com.turhanoz.android.reactivedirectorychooser.ui.DirectoryChooserFragment;

import java.io.File;
import java.util.Map;

/**
 * Created by andru on 10/26/2016.
 */

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.ViewHolder> {

    private File currentRootDirectory = Environment.getExternalStorageDirectory();
    private Map<String, String> uriMap;
    private Context context;
    private FragmentManager fragmentManager;

    public MessageDataAdapter(Map<String, String> uriMap, Context context, FragmentManager fragmentManager) {
        this.uriMap = uriMap;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MessageDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_message_image_raw, parent, false);
        return new MessageDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageDataAdapter.ViewHolder holder, int position) {
        if (uriMap.containsKey("image0")){
            Uri uri = Uri.parse(uriMap.get("image" + position));
            Picasso.with(context).load(uri).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.image);
            holder.image.setOnClickListener(v -> {
                addDirectoryChooserAsFloatingFragment(fragmentManager);
                SelectedUri.getInstance().setUri(uri.toString());
            });
        } else if (uriMap.containsKey("document0")){
            holder.image.setImageDrawable(context.getResources().getDrawable((R.drawable.document)));
            holder.image.setOnClickListener(v -> {
                SelectedUri.getInstance().setUri(uriMap.get("document" + position));
                addDirectoryChooserAsFloatingFragment(fragmentManager);
            });
        }

    }

    private void addDirectoryChooserAsFloatingFragment(FragmentManager fragmentManager) {
        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        directoryChooserFragment.show(transaction, "RDC");
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

