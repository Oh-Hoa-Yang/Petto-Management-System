package com.example.pettomanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SelectTreatmentsActivity extends AppCompatActivity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_treatments);

        back = findViewById(R.id.buttonBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectTreatmentsActivity.this, HealthMonitorActivity.class));
            }
        });

        CardView checkup = findViewById(R.id.cardCheckup);
        checkup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Checkup");
                startActivity(intent);
            }
        });

        CardView consultation = findViewById(R.id.cardConsultation);
        consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Consultation");
                startActivity(intent);
            }
        });

        CardView dental = findViewById(R.id.cardDental);
        dental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Dental");
                startActivity(intent);
            }
        });

        CardView grooming = findViewById(R.id.cardGrooming );
        grooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Grooming");
                startActivity(intent);
            }
        });

        CardView surgery = findViewById(R.id.cardSurgery);
        surgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Surgery");
                startActivity(intent);
            }
        });

        CardView vaccination = findViewById(R.id.cardVaccination);
        vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectTreatmentsActivity.this, SelectClinicActivity.class);
                intent.putExtra("treatment", "Vaccination");
                startActivity(intent);
            }
        });
    }
}