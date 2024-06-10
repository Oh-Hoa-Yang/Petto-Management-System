package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class EditPetProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 100;

    EditText editPetName, editPetType, editPetAge, editPetBreed;
    Button editPetPhotoButton, savePetProfileButton;
    ImageView editPetImageView;
    FirebaseAuth firebaseAuth;
    DatabaseReference petReference;
    String currentPhotoPath;
    String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_profile);

        petId = getIntent().getStringExtra("petId");

        editPetName = findViewById(R.id.edit_pet_name);
        editPetType = findViewById(R.id.edit_pet_type);
        editPetAge = findViewById(R.id.edit_pet_age);
        editPetBreed = findViewById(R.id.edit_pet_breed);
        editPetPhotoButton = findViewById(R.id.edit_pet_photo_button);
        savePetProfileButton = findViewById(R.id.save_pet_profile_button);
        editPetImageView = findViewById(R.id.edit_pet_image_view);

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
                        editPetName.setText(petName);
                        editPetType.setText(petAge);
                        editPetAge.setText(petType);
                        editPetBreed.setText(breed);

                        // Load and display the pet photo using Glide
                        if (petPhotoUrl != null && !petPhotoUrl.isEmpty()) {
                            Glide.with(EditPetProfileActivity.this).load(petPhotoUrl).into(editPetImageView);
                        } else {
                            // If there's no photo available, you can set a default image
                            editPetImageView.setImageResource(R.drawable.default_pet_image);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EditPetProfileActivity.this, "Failed to load pet profile data.", Toast.LENGTH_SHORT).show();
                }

            });

            // Inside onCreate after setting petReference ValueEventListener

            savePetProfileButton.setOnClickListener(v -> {
                // Get the values from EditText fields
                String name = editPetName.getText().toString().trim();
                String type = editPetType.getText().toString().trim();
                String age = editPetAge.getText().toString().trim();
                String breed = editPetBreed.getText().toString().trim();

                // Validate if all fields are filled
                if (name.isEmpty() || type.isEmpty() || age.isEmpty() || breed.isEmpty()) {
                    Toast.makeText(EditPetProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Update the pet data in Firebase Database
                    updatePetData(name, type, age, breed);
                }
            });

        }
    }

    private void onNavigationIconClick() {
        Intent intent = new Intent(EditPetProfileActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File file = new File(currentPhotoPath);
            editPetImageView.setImageURI(Uri.fromFile(file));
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void updatePetData(String name, String type, String age, String breed) {
        // Get the reference to the specific pet using petId
        petId = getIntent().getStringExtra("petId");

        DatabaseReference specificPetReference = petReference.child(petId);

        // Update the pet data
        specificPetReference.child("name").setValue(name);
        specificPetReference.child("type").setValue(type);
        specificPetReference.child("age").setValue(age);
        specificPetReference.child("breed").setValue(breed);

        // If there's a new photo, update the photo URL
        if (currentPhotoPath != null) {
            specificPetReference.child("photo").setValue(currentPhotoPath);
        }

        // Show a success message
        Toast.makeText(EditPetProfileActivity.this, "Pet Profile Updated Successfully", Toast.LENGTH_SHORT).show();

        // Navigate back to the PetProfileActivity
        Intent intent = new Intent(EditPetProfileActivity.this, PetProfileActivity.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
