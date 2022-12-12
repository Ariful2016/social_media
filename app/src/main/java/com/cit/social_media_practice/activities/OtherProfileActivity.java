package com.cit.social_media_practice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.databinding.ActivityOtherProfileBinding;
import com.cit.social_media_practice.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OtherProfileActivity extends AppCompatActivity {
    ActivityOtherProfileBinding binding;
    Intent intent;
    String other_user_id;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();

        other_user_id = intent.getStringExtra("other_id");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            userId = firebaseUser.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    if (!user.getUser_profile().equals("")){
                        Glide.with(OtherProfileActivity.this).load(user.getUser_profile()).into(binding.myProfileImage);
                    }else {
                        binding.profileImage.setImageResource(R.drawable.ic_logo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child(other_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    binding.name.setText(user.getUser_name());
                    binding.email.setText(user.getUser_email());

                    if (!user.getUser_cover().equals("")){
                        Glide.with(OtherProfileActivity.this).load(user.getUser_cover()).into(binding.coverPic);
                    }else {
                        binding.coverPic.setImageResource(R.drawable.ic_logo);
                    }

                    if (!user.getUser_profile().equals("")){
                        Glide.with(OtherProfileActivity.this).load(user.getUser_profile()).into(binding.profileImage);
                    }else {
                        binding.profileImage.setImageResource(R.drawable.ic_logo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backArrow.setOnClickListener(view -> {
            finish();
        });

        binding.editMessage.setOnClickListener(view -> {
            Intent intent = new Intent(OtherProfileActivity.this, ChatActivity.class);
            intent.putExtra("other_id", other_user_id);
            startActivity(intent);
        });
    }
}