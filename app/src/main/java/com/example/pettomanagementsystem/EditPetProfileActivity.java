package com.example.pettomanagementsystem;

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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditPetProfileActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_CAMERA_PERMISSION = 100;

    EditText editPetName, editPetType, editPetAge, editPetBreed;
    Button editPetPhotoButton, savePetProfileButton;
    ImageView editPetImageView;
    FirebaseAuth firebaseAuth;
    DatabaseReference petReference;
    String currentPhotoPath;
    String userId, petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_profile);

        editPetName = findViewById(R.id.edit_pet_name);
        editPetType = findViewById(R.id.edit_pet_type);
        editPetAge = findViewById(R.id.edit_pet_age);
        editPetBreed = findViewById(R.id.edit_pet_breed);
        editPetPhotoButton = findViewById(R.id.edit_pet_photo_button);
        savePetProfileButton = findViewById(R.id.save_pet_profile_button);
        editPetImageView = findViewById(R.id.edit_pet_image_view);
        petId = getIntent().getStringExtra("petId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onNavigationIconClick());

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userId = currentUser.getUid();

            petReference = FirebaseDatabase.getInstance().getReference("userinfo").child(userId).child("pets").child(petId);
            petReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String petName = dataSnapshot.child("name").getValue(String.class);
                        String petAge = dataSnapshot.child("age").getValue(String.class);
                        String petType = dataSnapshot.child("type").getValue(String.class);
                        String breed = dataSnapshot.child("breed").getValue(String.class);
                        String petPhotoUrl = dataSnapshot.child("photo").getValue(String.class);

                        editPetName.setText(petName);
                        editPetType.setText(petType);
                        editPetAge.setText(petAge);
                        editPetBreed.setText(breed);

                        if (petPhotoUrl != null && !petPhotoUrl.isEmpty()) {
                            Glide.with(EditPetProfileActivity.this).load(petPhotoUrl).into(editPetImageView);
                        } else {
                            editPetImageView.setImageResource(R.drawable.default_pet_image);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EditPetProfileActivity.this, "Failed to load pet profile data.", Toast.LENGTH_SHORT).show();
                }
            });

            editPetPhotoButton.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(EditPetProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(EditPetProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditPetProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                } else {
                    dispatchTakePictureIntent();
                }
            });

            savePetProfileButton.setOnClickListener(v -> {
                String updatedName = editPetName.getText().toString().trim();
                String updatedType = editPetType.getText().toString().trim();
                String updatedAge = editPetAge.getText().toString().trim();
                String updatedBreed = editPetBreed.getText().toString().trim();

                if (updatedName.isEmpty() || updatedType.isEmpty() || updatedAge.isEmpty() || updatedBreed.isEmpty()) {
                    Toast.makeText(EditPetProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    updatePetData(updatedName, updatedType, updatedAge, updatedBreed);
                }
            });
        }
    }

    private void updatePetData(String name, String type, String age, String breed) {
        DatabaseReference specificPetReference = petReference;

        specificPetReference.child("name").setValue(name);
        specificPetReference.child("type").setValue(type);
        specificPetReference.child("age").setValue(age);
        specificPetReference.child("breed").setValue(breed);

        if (currentPhotoPath != null) {
            specificPetReference.child("photo").setValue(currentPhotoPath);
        }

        Toast.makeText(EditPetProfileActivity.this, "Pet Profile Updated Successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(EditPetProfileActivity.this, PetProfileActivity.class);
        intent.putExtra("petId", petId);
        startActivity(intent);
        finish();
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pettomanagementsystem.fileprovider",
                        photoFile);
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
