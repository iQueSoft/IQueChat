package com.iquesoft.andrew.seedprojectchat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.iquesoft.andrew.seedprojectchat.R;

import java.util.List;

/**
 * Created by andru on 10/24/2016.
 */

public class PreviewPhotoAdapter extends RecyclerView.Adapter<PreviewPhotoAdapter.ViewHolder> {

    List<String> uriFileList;
    Context context;

    public PreviewPhotoAdapter(List<String> uriFileList, Context context){
        this.uriFileList = uriFileList;
        this.context = context;
    }

    @Override
    public PreviewPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_photo_item, parent, false);
        return new PreviewPhotoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviewPhotoAdapter.ViewHolder holder, int position) {
        Bitmap bm = BitmapFactory.decodeFile(uriFileList.get(position));
        holder.userPhoto.setImageBitmap(bm);
        //Uri uri = Uri.parse(uriFileList.get(position));
        //Picasso.with(context).load(uri).resize(100, 100).placeholder(R.drawable.placeholder).error(R.drawable.error).into(holder.userPhoto);
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
            userPhoto = (ImageView) itemView.findViewById(R.id.image);
            buttonDelete = (ImageButton) itemView.findViewById(R.id.button_delete);
        }
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
