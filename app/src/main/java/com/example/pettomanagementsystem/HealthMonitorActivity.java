package com.example.pettomanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HealthMonitorActivity extends AppCompatActivity {

    Button buttonSelectClinic, buttonBookingRecords, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_monitor);


        buttonSelectClinic = findViewById(R.id.buttonBooking);
        buttonSelectClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthMonitorActivity.this, SelectTreatmentsActivity.class));
            }
        });

        buttonBookingRecords = findViewById(R.id.buttonRecords);
        buttonBookingRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthMonitorActivity.this, BookingRecordsActivity.class));
            }
        });

        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HealthMonitorActivity.this, HomeActivity.class));
            }
        });
    }
}