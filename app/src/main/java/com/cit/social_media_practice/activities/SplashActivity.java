package com.cit.social_media_practice.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cit.social_media_practice.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    GoogleSignInAccount googleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextPage();
            }
        },2500);
    }

    private void nextPage() {
        if(firebaseUser != null){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }else if (googleSignInAccount!= null){
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }

    }
}