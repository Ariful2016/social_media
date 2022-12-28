package com.cit.social_media_practice.fragments;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.activities.ChatActivity;
import com.cit.social_media_practice.activities.MainActivity;
import com.cit.social_media_practice.activities.PostActivity;
import com.cit.social_media_practice.activities.ProfileEditActivity;
import com.cit.social_media_practice.adapter.AddStoryAdapter;
import com.cit.social_media_practice.adapter.PostAdapter;
import com.cit.social_media_practice.databinding.FragmentHomeBinding;
import com.cit.social_media_practice.model.Post;
import com.cit.social_media_practice.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseUser firebaseUser;
    String currentUserId;
    DatabaseReference databaseReference, postDatabaseRef;

    List<User> addStoryList;
    AddStoryAdapter addStoryAdapter;


    List<Post> postList;
    PostAdapter postAdapter;


    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        View view = binding.getRoot();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUserId = firebaseUser.getUid();
        }
        addStoryList = new ArrayList<>();
        postList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        postDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    if (user.getUser_profile() != null){
                        Glide.with(getContext()).load(user.getUser_profile()).into(binding.myProfileImage);
                    }else {
                        binding.myProfileImage.setImageResource(R.drawable.ic_logo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);

                    postList.add(post);
                }
                postAdapter = new PostAdapter(getContext(),postList);
                binding.postRecycler.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.postEdit.setOnClickListener(view1 -> {
            startActivity(new Intent(getContext(), PostActivity.class));
        });


        return view;
    }


    @Override
    public void onResume() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                addStoryList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    addStoryList.add(user);
                }
                addStoryAdapter = new AddStoryAdapter(getContext(),addStoryList);
                binding.addStoryRecycler.setAdapter(addStoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onResume();
    }




}