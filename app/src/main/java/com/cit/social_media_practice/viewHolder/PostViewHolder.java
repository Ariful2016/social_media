package com.cit.social_media_practice.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cit.social_media_practice.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView profile_img,post_img;
    public TextView name,post_txt,date;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_img = itemView.findViewById(R.id.profile_image);
        name = itemView.findViewById(R.id.name);
        date = itemView.findViewById(R.id.date);
        post_txt = itemView.findViewById(R.id.post_txt);
        post_img = itemView.findViewById(R.id.post_img);

    }
}
