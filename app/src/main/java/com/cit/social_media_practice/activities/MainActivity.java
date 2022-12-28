package com.cit.social_media_practice.activities;

import static com.cit.social_media_practice.utils.CustomDialog.ValidateInputField;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.cit.social_media_practice.R;
import com.cit.social_media_practice.adapter.FragmentAdapter;
import com.cit.social_media_practice.databinding.ActivityMainBinding;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseUser firebaseUser;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    GoogleSignInAccount googleSignInAccount;

    FragmentAdapter fragmentAdapter;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);


        FirebaseMessaging.getInstance().subscribeToTopic("JobAlert").addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(MainActivity.this, "Subscribe to JA", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager,101);

        binding.viewPager.setAdapter(fragmentAdapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        binding.tab.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        binding.tab.getTabAt(1).setIcon(R.drawable.ic_baseline_people_24);
        binding.tab.getTabAt(2).setIcon(R.drawable.ic_baseline_message_24);
        binding.tab.getTabAt(3).setIcon(R.drawable.ic_baseline_notifications_24);



        googleSignInOptions  = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this,googleSignInOptions);

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (googleSignInAccount != null){
            Log.i("TAG", "onCreate: "+googleSignInAccount.getEmail());
            Log.i("TAG", "onCreate: "+googleSignInAccount.getDisplayName());
        }

       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            if(!firebaseUser.isEmailVerified()){
                //ValidateInputField(MainActivity.this,"Please verify your email!");
            }
        }

        UserPermission();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    private void UserPermission() {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
//                        Manifest.permission.POST_NOTIFICATIONS
                )

                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }
}