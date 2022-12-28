package com.cit.social_media_practice.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.cit.social_media_practice.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddStoryViewHolder extends RecyclerView.ViewHolder {
    public AppCompatImageView coverPic;
    public CircleImageView profilePic;
    public TextView name;
    public AddStoryViewHolder(@NonNull View itemView) {
        super(itemView);

        coverPic = itemView.findViewById(R.id.coverPic);
        profilePic = itemView.findViewById(R.id.profile_image);
        name = itemView.findViewById(R.id.name);
    }
}
