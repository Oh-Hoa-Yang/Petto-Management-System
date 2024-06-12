package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditContactActivity extends AppCompatActivity {
    EditText editTextName, editTextPhoneNo;
    Button buttonUpdateContact;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        buttonUpdateContact = findViewById(R.id.buttonAddContact);

        editTextName.setText(getIntent().getStringExtra("name"));
        editTextPhoneNo.setText(getIntent().getStringExtra("phoneNo"));
        key = getIntent().getStringExtra("key");

        buttonUpdateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContactInDatabase(editTextName.getText().toString(), editTextPhoneNo.getText().toString());
            }
        });
    }

    private void updateContactInDatabase(String name, String phoneNo) {
        // Get a reference to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("contact");

        // Update the contact
        HashMap<String, Object> contact = new HashMap<>();
        contact.put("name", name);
        contact.put("phoneNo", phoneNo);
        contactRef.child(key).updateChildren(contact);
        finish();
    }
}