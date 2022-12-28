package com.cit.social_media_practice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.model.User;
import com.cit.social_media_practice.viewHolder.AddStoryViewHolder;

import java.util.List;

public class AddStoryAdapter extends RecyclerView.Adapter<AddStoryViewHolder> {
    Context context;
    List<User> userList;

    public AddStoryAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AddStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_story,parent,false);
        return new AddStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddStoryViewHolder holder, int position) {

        User user = userList.get(position);
        holder.name.setText(user.getUser_name());
        if (user.getUser_cover() != null){
            Glide.with(context).load(user.getUser_cover()).into(holder.coverPic);
        }else {
            holder.coverPic.setImageResource(R.drawable.ic_logo);
        }
        if (user.getUser_profile() != null){
            Glide.with(context).load(user.getUser_profile()).into(holder.profilePic);
        }else {
            holder.profilePic.setImageResource(R.drawable.ic_logo);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
