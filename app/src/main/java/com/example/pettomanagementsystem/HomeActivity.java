package com.example.pettomanagementsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    CardView userProfileCard, petCard, healthCard, emergencyCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigationIconClick();
            }
        });

        userProfileCard = findViewById(R.id.userprofile_card);
        petCard = findViewById(R.id.pet_card);
        healthCard = findViewById(R.id.health_card);
        emergencyCard = findViewById(R.id.emergency_card);

        userProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

//        petCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, PetProfileActivity.class);
//                startActivity(intent);
//            }
//        });
//
        healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, HealthMonitorActivity.class);
                startActivity(intent);
            }
        });
//
//        emergencyCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, EmergencyActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void onNavigationIconClick() {
        Intent intent = new Intent(HomeActivity.this, SideMenuActivity.class);
        startActivity(intent);
    }
}
