package com.cit.social_media_practice.activities;

import static com.cit.social_media_practice.utils.CustomDialog.ValidateInputField;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.adapter.ChatAdapter;


import com.cit.social_media_practice.databinding.ActivityChatBinding;
import com.cit.social_media_practice.model.Chat;
import com.cit.social_media_practice.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    Intent intent;
    String other_user;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUser;

    DatabaseReference databaseReference, chatReference;

    List<Chat> chatList;
    ChatAdapter chatAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        other_user = intent.getStringExtra("other_id");

        chatList = new ArrayList<>();

        //recyclerView = findViewById(R.id.messageRecycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        binding.messageRecycler.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        if (firebaseUser != null){
            currentUser = firebaseUser.getUid();
        }

        databaseReference.child(other_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    binding.name.setText(user.getUser_name());
                    if (!user.getUser_profile().equals("")){
                        Glide.with(ChatActivity.this).load(user.getUser_profile()).into(binding.profileImage);
                    }else {
                        binding.profileImage.setImageResource(R.drawable.ic_logo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.send.setOnClickListener(view -> {
            String message = binding.editMessage.getText().toString();

            //String chatId = UUID.randomUUID().toString()+System.currentTimeMillis();
            String chatId = chatReference.push().getKey();

            HashMap<String,Object> chatMap = new HashMap<>();
            chatMap.put("message", message);
            chatMap.put("sender", currentUser);
            chatMap.put("receiver", other_user);
            chatMap.put("chat_id", chatId);

            chatReference.child(chatId).setValue(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        binding.editMessage.setText("");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ValidateInputField(ChatActivity.this,e.getMessage().toString());
                }
            });


        });

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (chat != null){
                        if (chat.getSender().equals(currentUser) && chat.getReceiver().equals(other_user)
                        || chat.getSender().equals(other_user) && chat.getReceiver().equals(currentUser))
                        {
                            chatList.add(chat);
                        }
                    }
                    chatAdapter = new ChatAdapter(ChatActivity.this,chatList);
                    binding.messageRecycler.setAdapter(chatAdapter);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}