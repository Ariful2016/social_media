package com.cit.social_media_practice.activities;

import static com.cit.social_media_practice.utils.CustomDialog.ValidateInputField;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cit.social_media_practice.R;
import com.cit.social_media_practice.databinding.ActivityProfileEditBinding;
import com.cit.social_media_practice.model.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {

    ActivityProfileEditBinding binding;
    String userId;
    Intent intent;
    DatabaseReference databaseReference;
    StorageReference storageReference,storageCover,storageProfile;

    Uri coverUri,profileUri;
    String coverUrl = "";
    String profileUrl = "";

    ProgressDialog progressDialog;

    String name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        userId = intent.getStringExtra("userId");

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference("social_media");
        storageCover = storageReference.child(userId).child("cover");
        storageProfile = storageReference.child(userId).child("profile");

        progressDialog = new ProgressDialog(ProfileEditActivity.this);
        progressDialog.setMessage("Please wait!");

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null){
                    binding.name.setText(user.getUser_name());
                    binding.email.setText(user.getUser_email());

                    if (!user.getUser_cover().equals("")){
                        Glide.with(ProfileEditActivity.this).load(user.getUser_cover()).into(binding.coverPic);
                    }else {
                        binding.coverPic.setImageResource(R.drawable.ic_logo);
                    }

                    if (!user.getUser_profile().equals("")){
                        Glide.with(ProfileEditActivity.this).load(user.getUser_profile()).into(binding.profileImage);
                    }else {
                        binding.profileImage.setImageResource(R.drawable.ic_logo);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.coverPicEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileEditActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(101);
            }
        });

        binding.profilePicEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ProfileEditActivity.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(102);
            }
        });

        binding.updateBtn.setOnClickListener(view -> {
             name = binding.name.getText().toString();
             email = binding.email.getText().toString();

            if (name.equals("")){
                ValidateInputField(getApplication(),"Name field can't be empty!");
            }
            if (email.equals("")){
                ValidateInputField(getApplication(),"Email field can't be empty!");
            }
            setUpCover();
            progressDialog.show();
           /* progressDialog.show();
            if (coverUri != null){
                setUpCover();
            }

            if (profileUri !=null){
                setUpProfile();
            }

            if (coverUrl != null && profileUrl != null){
                sendToDatabase();
            }*/


        });

    }

    private void setUpCover() {
            storageCover.putFile(coverUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        storageCover.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                coverUrl = String.valueOf(uri);
                                setUpProfile();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ValidateInputField(getApplication(),e.getMessage().toString());
                    progressDialog.dismiss();
                }
            });

    }

    private void setUpProfile() {
        storageProfile.putFile(profileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    storageProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileUrl = String.valueOf(uri);
                            sendToDatabase();

                        }
                    });
                }
            }
        });


    }

    private void sendToDatabase() {
        HashMap<String,Object> updateMap = new HashMap<>();
        updateMap.put("user_name", name);
        updateMap.put("user_email", email);
        updateMap.put("user_cover",coverUrl);
        updateMap.put("user_profile", profileUrl);

        databaseReference.child(userId).updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(ProfileEditActivity.this,ProfileActivity.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ValidateInputField(getApplication(),e.getMessage().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null){
            coverUri = data.getData();
            binding.coverPic.setImageURI(coverUri);
        }
        if (requestCode == 102 && resultCode == RESULT_OK && data != null){
            profileUri = data.getData();
            binding.profileImage.setImageURI(profileUri);
        }
    }
}