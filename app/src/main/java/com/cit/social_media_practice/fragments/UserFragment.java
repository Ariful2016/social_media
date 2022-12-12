package com.cit.social_media_practice.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cit.social_media_practice.R;
import com.cit.social_media_practice.adapter.UserAdapter;
import com.cit.social_media_practice.databinding.FragmentUserBinding;
import com.cit.social_media_practice.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {


    FragmentUserBinding binding;
    List<User> userList;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String currentUser;
    UserAdapter userAdapter;

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater);

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && !user.getUser_id().equals(currentUser)){
                        userList.add(user);
                    }

                    userAdapter = new UserAdapter(getActivity(),userList);
                }
                binding.userRecycler.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}