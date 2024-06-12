package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactListActivity extends AppCompatActivity implements ContactListInterface {
    ArrayList<ContactModel> contactList = new ArrayList<>();
    static int PERMISSION_REQUEST_CODE = 1;
    int clickedPosition;
    ContactListRecyclerViewAdapter contactListRecyclerViewAdapter;
    FloatingActionButton addButton, editButton, deleteButton, callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        addButton = findViewById(R.id.floatingActionButtonAdd);
        editButton = findViewById(R.id.floatingActionButtonEdit);
        deleteButton = findViewById(R.id.floatingActionButtonDelete);
        callButton = findViewById(R.id.floatingActionButtonCall);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewContactList);

        buttonBehaviour(addButton, editButton, deleteButton, callButton);

        setUpContact();
        contactListRecyclerViewAdapter = new ContactListRecyclerViewAdapter(this, contactList, this);
        recyclerView.setAdapter(contactListRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpContact() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference contactRef = database.getReference("contact");

        contactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list to avoid duplications
                contactList.clear();

                // Loop through all the children in the "contact" node
                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                    // Get the contact data and convert it into a ContactModel object
                    ContactModel contact = contactSnapshot.getValue(ContactModel.class);

                    // Add the contact to the list
                    contactList.add(contact);
                }

                // Notify the adapter that the data has changed
                contactListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log a message if there was an error reading the data
                Log.e("ContactList", "Failed to read contacts", databaseError.toException());
            }
        });
    }

    @Override
    public void onContactClick(int position) {
        if (deleteButton.getVisibility() == View.GONE && addButton.getVisibility() == View.GONE && editButton.getVisibility() == View.GONE) {
            clickedPosition = position;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
            } else {
                initiateCall();
            }
        }

        if (deleteButton.getVisibility() == View.GONE && addButton.getVisibility() == View.GONE && callButton.getVisibility() == View.GONE) {
            Intent intent = new Intent(ContactListActivity.this, EditContactActivity.class);
            intent.putExtra("name", contactList.get(position).getName());
            intent.putExtra("phoneNo", contactList.get(position).getPhoneNo());
            intent.putExtra("key", contactList.get(position).getKey());
            startActivity(intent);
        }
    }

    @Override
    public void onLongContactClick(int position) {
        if (callButton.getVisibility() == View.GONE && addButton.getVisibility() == View.GONE && editButton.getVisibility() == View.GONE) {
            String key = contactList.get(position).getKey();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference contactRef = database.getReference("contact");
            contactRef.child(key).removeValue();
            contactList.remove(position);
            contactListRecyclerViewAdapter.notifyItemRemoved(position);
        }
    }

    @Override
    public int getCallButtonVisibility() {
        return callButton.getVisibility();
    }

    @Override
    public int getEditButtonVisibility() {
        return editButton.getVisibility();
    }

    @Override
    public int getAddButtonVisibility() {
        return addButton.getVisibility();
    }

    @Override
    public int getDeleteButtonVisibility() {
        return deleteButton.getVisibility();
    }

    private void initiateCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contactList.get(clickedPosition).getPhoneNo()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted. If necessary, perform a check to identify the clicked contact.
                initiateCall();
            }
        }
    }

    public void buttonBehaviour(FloatingActionButton addButton, FloatingActionButton editButton, FloatingActionButton deleteButton, FloatingActionButton callButton) {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addButton.getVisibility() == View.VISIBLE && editButton.getVisibility() == View.VISIBLE && deleteButton.getVisibility() == View.VISIBLE) {
                    addButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    contactListRecyclerViewAdapter.updateImageVisibility();
                    Toast.makeText(ContactListActivity.this, "Call Mode", Toast.LENGTH_SHORT).show();
                } else {
                    addButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callButton.getVisibility() == View.VISIBLE && editButton.getVisibility() == View.VISIBLE && deleteButton.getVisibility() == View.VISIBLE) {
                    callButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    contactListRecyclerViewAdapter.updateImageVisibility();
                    Toast.makeText(ContactListActivity.this, "Add Mode", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ContactListActivity.this, AddContactActivity.class);
                    startActivity(intent);
                } else {
                    callButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callButton.getVisibility() == View.VISIBLE && addButton.getVisibility() == View.VISIBLE && deleteButton.getVisibility() == View.VISIBLE) {
                    callButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.GONE);
                    contactListRecyclerViewAdapter.updateImageVisibility();
                    Toast.makeText(ContactListActivity.this, "Edit Mode", Toast.LENGTH_SHORT).show();
                } else {
                    callButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callButton.getVisibility() == View.VISIBLE && addButton.getVisibility() == View.VISIBLE && editButton.getVisibility() == View.VISIBLE) {
                    callButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.GONE);
                    contactListRecyclerViewAdapter.updateImageVisibility();
                    Toast.makeText(ContactListActivity.this, "Delete Mode", Toast.LENGTH_SHORT).show();
                } else {
                    callButton.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}