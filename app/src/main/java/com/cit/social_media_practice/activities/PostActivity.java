package com.cit.social_media_practice.activities;

import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.adapter.AddStoryAdapter;
import com.cit.social_media_practice.databinding.ActivityPostBinding;
import com.cit.social_media_practice.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    ActivityPostBinding binding;
    FirebaseUser firebaseUser;
    String currentUserId;
    DatabaseReference databaseReference, postDatabaseRef;
    StorageReference storageReference;


    Uri postUri;
    String postUrl,post_id;
    String post_txt;
    Long date = System.currentTimeMillis();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUserId = firebaseUser.getUid();
        }

        progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("Please wait!");

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        postDatabaseRef = FirebaseDatabase.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("Post_Img");
        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    binding.name.setText(user.getUser_name());
                    if (!user.getUser_profile().equals("")){
                        Glide.with(PostActivity.this).load(user.getUser_profile()).into(binding.profileImage);
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

        binding.galleryIcon.setOnClickListener(view -> {
            ImagePicker.with(PostActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(101);
        });

        binding.postBtn.setOnClickListener(view -> {

            progressDialog.show();
            post_txt = binding.postSheetEdit.getText().toString();
            if (post_txt.isEmpty()){
                post_txt = "";
            }
            post_id = databaseReference.push().getKey();

            if (postUri != null){
                StorageReference storageRefer = storageReference.child(currentUserId+System.currentTimeMillis());
                storageRefer.putFile(postUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            storageRefer.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    postUrl = String.valueOf(uri);

                                    HashMap<String,Object> postMap = new HashMap<>();
                                    postMap.put("post_id", post_id);
                                    postMap.put("creator_id", currentUserId);
                                    postMap.put("post_txt", post_txt);
                                    postMap.put("post_img", postUrl);
                                    postMap.put("date", date);
                                    postDatabaseRef.child(post_id).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }else {
                HashMap<String,Object> postMap = new HashMap<>();
                postMap.put("post_id", post_id);
                postMap.put("creator_id", currentUserId);
                postMap.put("post_txt", post_txt);
                postMap.put("post_img", "");
                postMap.put("date", date);
                postDatabaseRef.child(post_id).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(PostActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null){
            postUri = data.getData();
            Log.i("TAG", "onActivityResult: "+postUri);
            binding.previewImg.setImageURI(postUri);
        }


    }



}