package com.cit.social_media_practice.activities;

import static com.cit.social_media_practice.utils.CustomDialog.ValidateInputField;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.cit.social_media_practice.R;
import com.cit.social_media_practice.databinding.ActivityRegisterBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");


        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Please wait!");




        binding.registerBtn.setOnClickListener(view -> {
            createAccountWithEmailAndPass();
        });

        binding.google.setOnClickListener(view -> {
            //createAccountWithGoogle();
        });



    }


    private void createAccountWithEmailAndPass() {

        String name = binding.name.getText().toString();
        String email = binding.email.getText().toString();
        String pass = binding.pass.getText().toString();
        String con_pass = binding.conPass.getText().toString();

        if (name.equals("")){
            ValidateInputField(RegisterActivity.this,"Name can't be empty!");
        }else if (email.equals("")){
            ValidateInputField(RegisterActivity.this,"Email can't be empty!");
        }else if (pass.equals("")){
            ValidateInputField(RegisterActivity.this,"Password can't be empty!");
        }else if (con_pass.length()<6 ){
            ValidateInputField(RegisterActivity.this,"Password should be more than 6 character!");
        }else if (con_pass.equals("")){
            ValidateInputField(RegisterActivity.this,"Conform password can't be empty!");
        } else if (!pass.equals(con_pass)){
            ValidateInputField(RegisterActivity.this,"Password doesn't match!");
        }else {
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null){
                            String user_id = firebaseUser.getUid();

                            HashMap<String,String> map = new HashMap<>();
                            map.put("user_name",name);
                            map.put("user_email",email);
                            map.put("user_password",pass);
                            map.put("user_cover","");
                            map.put("user_profile","");
                            map.put("user_id",user_id);

                            databaseReference.child(user_id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressDialog.dismiss();
                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Failed! try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Log.i("TAG", "onFailure: "+e.getMessage());
                                }
                            });
                        }

                        /*firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Send email verification link to "+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, "Failed to send email verification link to "+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            }
                        });*/

                    }else {
                        progressDialog.dismiss();
                        ValidateInputField(RegisterActivity.this,"Failed! try again");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    ValidateInputField(RegisterActivity.this,e.toString());
                }
            });
        }
    }


}