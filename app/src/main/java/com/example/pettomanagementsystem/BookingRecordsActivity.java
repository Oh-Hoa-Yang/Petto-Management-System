package com.example.pettomanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingRecordsActivity extends AppCompatActivity {

    Button back;
    ListView listView;
    ArrayList<HashMap<String, String>> list = new ArrayList<>();
    SimpleAdapter adapter;
    HashMap<String, String> item;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_records);

        back = findViewById(R.id.buttonBack);
        listView = findViewById(R.id.listViewBookingRecords);

        // Back button functionality
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingRecordsActivity.this, HealthMonitorActivity.class));
            }
        });

        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("appointments");

        // Retrieve data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String treatment = snapshot.child("treatment").getValue(String.class);
                    String clinic = snapshot.child("clinic").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);
                    String workingHours = snapshot.child("workingHours").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);
                    Float temperature = snapshot.child("temperature").getValue(Float.class);

                    item = new HashMap<>();
                    item.put("id", id);
                    item.put("treatment", "Treatment: " + treatment);
                    item.put("clinic", "Clinic: " + clinic);
                    item.put("address", "Address: " + address);
                    item.put("contact", "Contact: " + contact);
                    item.put("workingHours", "Working Hours: " + workingHours);
                    item.put("date", "Date: " + date);
                    item.put("time", "Time: " + time);
                    item.put("temperature", "Temperature: " + temperature + "°C");
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BookingRecordsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup adapter
        adapter = new SimpleAdapter(this, list, R.layout.multi_lines_booking_records,
                new String[]{"treatment", "clinic", "address", "contact", "workingHours", "date", "time", "temperature"},
                new int[]{R.id.textViewLineA, R.id.textViewLineB, R.id.textViewLineC, R.id.textViewLineD, R.id.textViewLineE, R.id.textViewLineF, R.id.textViewLineG, R.id.textViewLineH});

        listView.setAdapter(adapter);

        // Add on item click listener for deleting an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> selectedItem = (HashMap<String, String>) parent.getItemAtPosition(position);
                String bookingId = selectedItem.get("id");

                databaseReference.child(bookingId).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(BookingRecordsActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(BookingRecordsActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
