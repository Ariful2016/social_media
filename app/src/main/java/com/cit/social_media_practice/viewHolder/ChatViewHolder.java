package com.cit.social_media_practice.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cit.social_media_practice.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profile_image;
    public TextView message;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

    profile_image = itemView.findViewById(R.id.profile_image);
    message = itemView.findViewById(R.id.chat);
    }
}
