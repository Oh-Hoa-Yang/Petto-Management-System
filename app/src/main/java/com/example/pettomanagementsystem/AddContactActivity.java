package com.example.pettomanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddContactActivity extends AppCompatActivity {
    EditText editTextName, editTextPhoneNo;
    Button buttonAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        buttonAddContact = findViewById(R.id.buttonAddContact);

        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phoneNo = editTextPhoneNo.getText().toString();

                if (name.isEmpty()) {
                    editTextName.setError("Name cannot be empty");
                    editTextName.requestFocus();
                    return;
                } else if (phoneNo.isEmpty()) {
                    editTextPhoneNo.setError("Phone number cannot be empty");
                    editTextPhoneNo.requestFocus();
                    return;
                }

                addContactToDatabase(name, phoneNo);
            }
        });
    }

    private void addContactToDatabase(String name, String phoneNo) {
        HashMap<String, String> contact = new HashMap<>();
        contact.put("name", name);
        contact.put("phoneNo", phoneNo);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("contact");

        String key = contactRef.push().getKey();
        contact.put("key", key);
        contactRef.child(key).setValue(contact).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddContactActivity.this, "Contact added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddContactActivity.this, "Failed to add contact", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}