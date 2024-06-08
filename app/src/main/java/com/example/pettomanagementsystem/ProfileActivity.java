package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    Button edit_profile_btn;
    TextView change_pwd_btn;
    ImageView profilePic;
    DatabaseReference usersReference;
    ProgressBar progressBar;
    TextView usernameView, emailView, contactView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        edit_profile_btn = findViewById(R.id.profile_edit_btn);
        change_pwd_btn = findViewById(R.id.change_pwd_btn);
        profilePic = findViewById(R.id.profileImageView);
        progressBar = findViewById(R.id.profile_progress_bar);
        usernameView = findViewById(R.id.username);
        emailView = findViewById(R.id.email);
        contactView = findViewById(R.id.contact);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onNavigationIconClick());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference = FirebaseDatabase.getInstance().getReference("userinfo").child(userId);
            usersReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userEmail = dataSnapshot.child("email").getValue(String.class);
                        String usernameText = dataSnapshot.child("username").getValue(String.class);
                        String contactText = dataSnapshot.child("contact").getValue(String.class);
                        String profilePictureUri = dataSnapshot.child("profile_picture").getValue(String.class);

                        emailView.setText(userEmail);
                        usernameView.setText(usernameText);
                        contactView.setText(contactText);
                        if (profilePictureUri != null && !profilePictureUri.isEmpty()) {
                            Glide.with(ProfileActivity.this).load(profilePictureUri).into(profilePic);
                        } else {
                            profilePic.setImageResource(R.drawable.baseline_account);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

            edit_profile_btn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, EditUserProfileActivity.class);
                startActivity(intent);
            });

            change_pwd_btn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, FingerprintActivity.class);
                startActivity(intent);
            });
        }
    }

    private void onNavigationIconClick() {
        Intent intent = new Intent(ProfileActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
}
