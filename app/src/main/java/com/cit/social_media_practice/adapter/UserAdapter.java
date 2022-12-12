package com.cit.social_media_practice.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.activities.ChatActivity;
import com.cit.social_media_practice.activities.OtherProfileActivity;
import com.cit.social_media_practice.activities.ProfileActivity;
import com.cit.social_media_practice.model.User;
import com.cit.social_media_practice.viewHolder.UserViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    Context context;
    List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);
        holder.name.setText(user.getUser_name());
        holder.phone.setText(user.getUser_email());
        Glide.with(context).load(user.getUser_profile()).into(holder.profile);
        holder.more.setOnClickListener(view -> {
            showDialog(context, user.getUser_id());
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context,OtherProfileActivity.class);
            intent.putExtra("other_id", user.getUser_id());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void showDialog(Context context, String id) {

        CircleImageView profile;
        TextView name,message,date;
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        profile = dialog.findViewById(R.id.profile_image);
        name = dialog.findViewById(R.id.name);
        message = dialog.findViewById(R.id.message_txt);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    name.setText(user.getUser_name());
                    if (!user.getUser_profile().equals("")){
                        Glide.with(context).load(user.getUser_profile()).into(profile);
                    }else {
                        profile.setImageResource(R.drawable.ic_logo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        message.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("other_id", id);
            context.startActivity(intent);
        });

        dialog.create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
