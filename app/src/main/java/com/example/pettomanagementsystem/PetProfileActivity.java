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



public class PetProfileActivity extends AppCompatActivity {

    Button editProfileButton, registerNewPet;
    TextView petageView, petnameView, pettypeView, breedView;
    ImageView petProfileImageView;
    DatabaseReference petReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

//        editProfileButton = findViewById(R.id.profile_edit_btn);

        petnameView = findViewById(R.id.petname);
        petageView = findViewById(R.id.pet_age);
        pettypeView = findViewById(R.id.pettype);
        breedView = findViewById(R.id.breed);
        registerNewPet = findViewById(R.id.register_new_pet_btn);
        editProfileButton = findViewById(R.id.profile_edit_btn);
        petProfileImageView = findViewById(R.id.petProfileImageView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onNavigationIconClick());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            petReference = FirebaseDatabase.getInstance().getReference("userinfo").child(userId).child("pets");
            petReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                        String petName = petSnapshot.child("name").getValue(String.class);
                        String petAge = petSnapshot.child("age").getValue(String.class);
                        String petType = petSnapshot.child("type").getValue(String.class);
                        String breed = petSnapshot.child("breed").getValue(String.class);
                        String petPhotoUrl = petSnapshot.child("photo").getValue(String.class);

                        // Display the pet information in TextViews
                        petnameView.setText(petName);
                        petageView.setText(petAge);
                        pettypeView.setText(petType);
                        breedView.setText(breed);

                        // Load and display the pet photo using Glide
                        if (petPhotoUrl != null && !petPhotoUrl.isEmpty()) {
                            Glide.with(PetProfileActivity.this).load(petPhotoUrl).into(petProfileImageView);
                        } else {
                            // If there's no photo available, you can set a default image
                            petProfileImageView.setImageResource(R.drawable.default_pet_image);
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
        }
    }


    private void onNavigationIconClick() {
        Intent intent = new Intent(PetProfileActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
}