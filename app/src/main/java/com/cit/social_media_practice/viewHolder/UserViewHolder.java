package com.cit.social_media_practice.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cit.social_media_practice.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView profile;
    public ImageView more;
    public TextView name,phone;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        profile = itemView.findViewById(R.id.profile_image);
        more = itemView.findViewById(R.id.more);
        name = itemView.findViewById(R.id.name);
        phone = itemView.findViewById(R.id.phone);
    }
}
