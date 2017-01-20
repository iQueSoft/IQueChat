package net.iquesoft.android.seedprojectchat.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.iquesoft.android.seedprojectchat.util.DownloadTask;
import net.iquesoft.android.seedprojectchat.util.SelectedUri;

import java.util.Map;

public class MessageDataAdapter extends RecyclerView.Adapter<MessageDataAdapter.ViewHolder> {

    private ProgressDialog mProgressDialog;
    private Map<String, String> uriMap;
    private Context context;
    private FragmentManager fragmentManager;

    public MessageDataAdapter(Map<String, String> uriMap, Context context, FragmentManager fragmentManager) {
        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Load...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        this.uriMap = uriMap;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MessageDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.last_message_image_raw, parent, false);
        return new MessageDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageDataAdapter.ViewHolder holder, int position) {
        if (uriMap.containsKey("image0")){
            Uri uri = Uri.parse(uriMap.get("image" + position));
            Picasso.with(context).load(uri).placeholder(net.iquesoft.android.seedprojectchat.R.drawable.placeholder).error(net.iquesoft.android.seedprojectchat.R.drawable.error).into(holder.image);
            holder.image.setOnClickListener(v -> {
                SelectedUri.getInstance().setUri(uriMap.get("image" + position));
                String fileName = SelectedUri.getInstance().getUri().substring(SelectedUri.getInstance().getUri().lastIndexOf('/') + 1, SelectedUri.getInstance().getUri().length());
                final DownloadTask downloadTask = new DownloadTask(context, "/storage/emulated/0/Download/" + fileName, mProgressDialog);
                downloadTask.execute(SelectedUri.getInstance().getUri());
            });
        } else if (uriMap.containsKey("document0")){
            holder.image.setImageDrawable(context.getResources().getDrawable((net.iquesoft.android.seedprojectchat.R.drawable.document)));
            holder.image.setOnClickListener(v -> {
                SelectedUri.getInstance().setUri(uriMap.get("document" + position));
                String fileName = SelectedUri.getInstance().getUri().substring(SelectedUri.getInstance().getUri().lastIndexOf('/') + 1, SelectedUri.getInstance().getUri().length());
                final DownloadTask downloadTask = new DownloadTask(context, "/storage/emulated/0/Download/" + fileName, mProgressDialog);
                downloadTask.execute(SelectedUri.getInstance().getUri());
            });
        }

    }
//    private void addDirectoryChooserAsFloatingFragment(FragmentManager fragmentManager) {
//        DialogFragment directoryChooserFragment = DirectoryChooserFragment.newInstance(currentRootDirectory);
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        directoryChooserFragment.show(transaction, "RDC");
//    }


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
            image = (ImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.image);
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

