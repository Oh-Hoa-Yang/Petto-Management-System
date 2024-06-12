package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    Button editProfileButton, registerNewPet, deletePetButton;
    TextView petageView, petnameView, pettypeView, breedView;
    ImageView petProfileImageView;
    DatabaseReference petReference;
    private String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_profile);

//        petidView = findViewById(R.id.pet_id);
        petnameView = findViewById(R.id.petname);
        petageView = findViewById(R.id.pet_age);
        pettypeView = findViewById(R.id.pettype);
        breedView = findViewById(R.id.breed);
        registerNewPet = findViewById(R.id.register_new_pet_btn);
        editProfileButton = findViewById(R.id.profile_edit_btn);
        petProfileImageView = findViewById(R.id.petProfileImageView);
        deletePetButton = findViewById(R.id.delete_pet_btn);

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
                        petId = petSnapshot.getKey(); // Get the petId from the database

                        // Fetch other details under each pet node
                        String petName = petSnapshot.child("name").getValue(String.class);
                        String petAge = petSnapshot.child("age").getValue(String.class);
                        String petType = petSnapshot.child("type").getValue(String.class);
                        String breed = petSnapshot.child("breed").getValue(String.class);
                        String petPhotoUrl = petSnapshot.child("photo").getValue(String.class);

                        // Display the pet information in TextViews
//                        petidView.setText(petId); // Display petId
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

                        // Since you are looping through pets, consider breaking after finding the pet you want to edit
                        break;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(PetProfileActivity.this, "Failed to load pet profile data.", Toast.LENGTH_SHORT).show();
                }
            });

            editProfileButton.setOnClickListener(v -> {
                Intent intent = new Intent(PetProfileActivity.this, EditPetProfileActivity.class);
                intent.putExtra("petId", petId); // Pass the petId to EditPetProfileActivity
                startActivity(intent);
            });

            registerNewPet.setOnClickListener(v -> {
                Intent intent = new Intent(PetProfileActivity.this, PetRegisterActivity.class);
                startActivity(intent);
            });

            deletePetButton.setOnClickListener(v -> {
                if (petId != null) {
                    petReference.child(petId).removeValue((databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            Toast.makeText(PetProfileActivity.this, "Pet profile deleted successfully.", Toast.LENGTH_SHORT).show();
                            // Optionally, you can refresh the activity or navigate to another activity
                            finish(); // Close the current activity
                        } else {
                            Toast.makeText(PetProfileActivity.this, "Failed to delete pet profile.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void onNavigationIconClick() {
        Intent intent = new Intent(PetProfileActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
}
