package com.example.pettomanagementsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VetListActivity extends AppCompatActivity implements VetListInterface {

    ArrayList<VetModel> vetList = new ArrayList<>();
    VetListRecyclerViewAdapter vetListRecyclerViewAdapter;

    // Declare these outside the methods for accessibility
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);
        RecyclerView vetRecyclerView = findViewById(R.id.recyclerViewVetList);

        setUpVetModels();
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        vetListRecyclerViewAdapter = new VetListRecyclerViewAdapter(this, vetList, this);
        vetRecyclerView.setAdapter(vetListRecyclerViewAdapter);
        vetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpVetModels() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference vetRef = database.getReference("vet");

        vetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vetList.clear();
                for (DataSnapshot vetSnapshot : dataSnapshot.getChildren()) {
                    VetModel vet = vetSnapshot.getValue(VetModel.class);
                    vetList.add(vet);
                }

                // Create Location object for user's location
                Location userLocation = new Location();
                userLocation.setLatitude(latitude);
                userLocation.setLongitude(longitude);

                // Sort the vetList based on distance from user's location
                Collections.sort(vetList, new Comparator<VetModel>() {
                    @Override
                    public int compare(VetModel vet1, VetModel vet2) {
                        Location vetLocation1 = new Location();
                        vetLocation1.setLatitude(vet1.getLatitude());
                        vetLocation1.setLongitude(vet1.getLongitude());

                        Location vetLocation2 = new Location();
                        vetLocation2.setLatitude(vet2.getLatitude());
                        vetLocation2.setLongitude(vet2.getLongitude());

                        float distanceToVet1 = userLocation.distanceTo(vetLocation1);
                        float distanceToVet2 = userLocation.distanceTo(vetLocation2);

                        return Float.compare(distanceToVet1, distanceToVet2);
                    }
                });

                // Notify the adapter that the data has changed
                vetListRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onItemClicked(int position) {
        VetModel vet = vetList.get(position);
        String uri = "geo:" + vet.getLatitude() + "," + vet.getLongitude() + "?q=" + vet.getLatitude() + "," + vet.getLongitude() + "(" + vet.getName() + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
} // Closing brace for VetListActivity class