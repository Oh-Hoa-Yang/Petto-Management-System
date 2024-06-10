package com.example.pettomanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PetRegisterActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 100;

    EditText petName, petType, petAge, petBreed;
    Button addPetPhotoButton, registerPetButton;
    ImageView petImageView;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersReference;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onNavigationIconClick());

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database Reference
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("userinfo");

        // Initialize EditText fields
        petName = findViewById(R.id.pet_name);
        petType = findViewById(R.id.pet_type);
        petAge = findViewById(R.id.pet_age);
        petBreed = findViewById(R.id.pet_breed);

        // Initialize Buttons and ImageView
        addPetPhotoButton = findViewById(R.id.add_pet_photo_button);
        registerPetButton = findViewById(R.id.register_pet_button);
        petImageView = findViewById(R.id.pet_image_view);

        // Handle Add Photo Button Click
        addPetPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check for camera permissions
                if (ContextCompat.checkSelfPermission(PetRegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(PetRegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PetRegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                } else {
                    // Launch the camera
                    dispatchTakePictureIntent();
                }
            }
        });

        // Handle Register Pet Button Click
        registerPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from EditText fields
                String name = petName.getText().toString().trim();
                String type = petType.getText().toString().trim();
                String age = petAge.getText().toString().trim();
                String breed = petBreed.getText().toString().trim();

                // Validate if all fields are filled
                if (name.isEmpty() || type.isEmpty() || age.isEmpty() || breed.isEmpty() || currentPhotoPath == null) {
                    Toast.makeText(PetRegisterActivity.this, "Please fill in all fields and add a photo", Toast.LENGTH_SHORT).show();
                } else {
                    // Register the pet by saving data to Firebase Database
                    savePetDataToDatabase(name, type, age, breed);
                }
            }
        });
    }

    private void onNavigationIconClick() {
        Intent intent = new Intent(PetRegisterActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
                return; // Add return to avoid executing further code
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pettomanagementsystem.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Set the image in ImageView
            File file = new File(currentPhotoPath);
            petImageView.setImageURI(Uri.fromFile(file));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void savePetDataToDatabase(String name, String type, String age, String breed) {
        // Get the current user's UID
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Create a new child node for the user using their UID
        DatabaseReference userReference = usersReference.child(userId);

        // Generate a unique key for the pet
        String petId = userReference.child("pets").push().getKey();

        // Create a new child node for the pet using its unique ID under the user's node
        DatabaseReference petReference = userReference.child("pets").child(petId);

        // Set values for the pet attributes
        petReference.child("name").setValue(name);
        petReference.child("type").setValue(type);
        petReference.child("age").setValue(age);
        petReference.child("breed").setValue(breed);
        petReference.child("photo").setValue(currentPhotoPath);

        // Inform the user about successful registration
        Toast.makeText(this, "Pet Registration Successful", Toast.LENGTH_SHORT).show();

        // Clear EditText fields after registration
        petName.setText("");
        petType.setText("");
        petAge.setText("");
        petBreed.setText("");
        petImageView.setImageResource(0);
        currentPhotoPath = null;

        // Navigate to PetProfileActivity
        Intent intent = new Intent(PetRegisterActivity.this, PetProfileActivity.class);
        intent.putExtra("petId", petId);
        startActivity(intent);

        // Finish the current activity
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
