package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PetProfileActivity extends AppCompatActivity {

    Button editProfileButton, registerNewPet;
    TextView petnameView, pettypeView, breedView;
    ImageView petProfileImageView;
    DatabaseReference petReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

        registerNewPet = findViewById(R.id.register_new_pet_btn);
        editProfileButton = findViewById(R.id.profile_edit_btn);
        petnameView = findViewById(R.id.petname);
        pettypeView = findViewById(R.id.pettype);
        breedView = findViewById(R.id.breed);
        petProfileImageView = findViewById(R.id.petProfileImageView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onNavigationIconClick());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            petReference = FirebaseDatabase.getInstance().getReference("petinfo").child(userId);
            petReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String petName = dataSnapshot.child("petname").getValue(String.class);
                        String petType = dataSnapshot.child("type").getValue(String.class);
                        String breed = dataSnapshot.child("breed").getValue(String.class);
                        String petProfilePictureUri = dataSnapshot.child("profile_picture").getValue(String.class);

                        petnameView.setText(petName);
                        pettypeView.setText(petType);
                        breedView.setText(breed);
                        if (petProfilePictureUri != null && !petProfilePictureUri.isEmpty()) {
                            Glide.with(PetProfileActivity.this).load(petProfilePictureUri).into(petProfileImageView);
                        } else {
                            petProfileImageView.setImageResource(R.drawable.pettype);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PetProfileActivity.this, "Failed to load pet profile data.", Toast.LENGTH_SHORT).show();
                }
            });

            registerNewPet.setOnClickListener(v -> {
                Intent intent = new Intent(PetProfileActivity.this, PetRegisterActivity.class);
                startActivity(intent);
            });
//            editProfileButton.setOnClickListener(v -> {
//                Intent intent = new Intent(PetProfileActivity.this, EditPetProfileActivity.class);
//                startActivity(intent);
//            });
        }
    }

    private void onNavigationIconClick() {
        Intent intent = new Intent(PetProfileActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
}