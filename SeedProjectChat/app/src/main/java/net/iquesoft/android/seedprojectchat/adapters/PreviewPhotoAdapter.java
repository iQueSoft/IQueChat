package net.iquesoft.android.seedprojectchat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;

public class PreviewPhotoAdapter extends RecyclerView.Adapter<PreviewPhotoAdapter.ViewHolder> {

    private List<String> uriFileList;
    private Context context;
    public Boolean photoFlag;

    public PreviewPhotoAdapter(List<String> uriFileList, Context context, Boolean photoFlag){
        this.photoFlag = photoFlag;
        this.uriFileList = uriFileList;
        this.context = context;
    }

    @Override
    public PreviewPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(net.iquesoft.android.seedprojectchat.R.layout.preview_photo_item, parent, false);
        return new PreviewPhotoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviewPhotoAdapter.ViewHolder holder, int position) {
        if (photoFlag){
            File image = new File(uriFileList.get(position));
            Bitmap compressedImageBitmap = Compressor.getDefault(context).compressToBitmap(image);
            holder.userPhoto.setImageBitmap(compressedImageBitmap);
            holder.userPhoto.setOnClickListener(v -> remove(position));
        } else {
            holder.userPhoto.setImageDrawable(context.getResources().getDrawable(net.iquesoft.android.seedprojectchat.R.drawable.document));
            holder.userPhoto.setOnClickListener(v -> remove(position));
        }
    }

    @Override
    public int getItemCount() {
        return uriFileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userPhoto;
        ImageButton buttonDelete;

        ViewHolder(View itemView) {
            super(itemView);
            userPhoto = (ImageView) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.image);
            buttonDelete = (ImageButton) itemView.findViewById(net.iquesoft.android.seedprojectchat.R.id.button_delete);
        }
    }

    public void clear(){
        uriFileList.clear();
        notifyDataSetChanged();
    }

    // Insert a new item to the RecyclerView
    public void insert(String uriFile) {
        uriFileList.add(0, uriFile);
        notifyItemInserted(0);
    }

    // Remove a RecyclerView item containing the Data object
    public void remove(int index) {
        uriFileList.remove(index);
        notifyItemRemoved(index);
    }
}
