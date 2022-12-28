package com.cit.social_media_practice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.activities.ProfileActivity;
import com.cit.social_media_practice.model.Post;
import com.cit.social_media_practice.model.User;
import com.cit.social_media_practice.viewHolder.PostViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context;
    List<Post> postList;

    DatabaseReference databaseReference;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = postList.get(position);
        if (post.getPost_img() != null){
            holder.post_img.setVisibility(View.VISIBLE);
            Glide.with(context).load(post.getPost_img()).into(holder.post_img);
        }else {
            holder.post_img.setVisibility(View.GONE);
        }
        if (post.getPost_txt().equals("")){
            holder.post_txt.setText("");
        }else {
            holder.post_txt.setText(post.getPost_txt());
        }
        Date date = new Date(post.getDate());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        holder.date.setText(dateFormat.format(date));
        databaseReference.child(post.getCreator_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    holder.name.setText(user.getUser_name());

                    if (!user.getUser_profile().equals("")){
                        Glide.with(context).load(user.getUser_profile()).into(holder.profile_img);
                    }else {
                        holder.post_img.setImageResource(R.drawable.ic_logo);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
