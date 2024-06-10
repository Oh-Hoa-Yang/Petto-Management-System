package com.example.pettomanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DisplayTemperatureActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView textViewTemperature;
    private Button buttonMeasure, buttonBookAppointment, buttonCancel;
    private float currentTemperature;
    private String treatment, clinic, address, contact, workingHours, selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_temperature);

        textViewTemperature = findViewById(R.id.textViewTemperature);
        buttonMeasure = findViewById(R.id.buttonMeasure);
        buttonBookAppointment = findViewById(R.id.buttonBookAppointment);
        buttonCancel = findViewById(R.id.buttonCancel);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (temperatureSensor == null) {
            Toast.makeText(this, "Temperature sensor not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Get data from intent
        Intent intent = getIntent();
        treatment = intent.getStringExtra("treatment");
        clinic = intent.getStringExtra("clinic");
        address = intent.getStringExtra("address");
        contact = intent.getStringExtra("contact");
        workingHours = intent.getStringExtra("working_hours");
        selectedDate = intent.getStringExtra("date");
        selectedTime = intent.getStringExtra("time");

        buttonMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measureTemperature();
            }
        });

        buttonBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            currentTemperature = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void measureTemperature() {
        textViewTemperature.setText("Pet Temperature: " + currentTemperature + "°C");
    }

    private void bookAppointment() {
        // Create a reference to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("appointments");

        // Create a unique key for the appointment
        String appointmentId = myRef.push().getKey();

        // Create a new appointment object
        Appointment appointment = new Appointment(treatment, clinic, address, contact, workingHours, currentTemperature, selectedDate, selectedTime);

        // Save the appointment in the database
        if (appointmentId != null) {
            myRef.child(appointmentId).setValue(appointment)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(DisplayTemperatureActivity.this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DisplayTemperatureActivity.this, HealthMonitorActivity.class));
                        } else {
                            Toast.makeText(DisplayTemperatureActivity.this, "Failed to book appointment", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Appointment class to hold the data
    public static class Appointment {
        public String treatment;
        public String clinic;
        public String address;
        public String contact;
        public String workingHours;
        public float temperature;
        public String date;
        public String time;

        public Appointment() {
            // Default constructor required for calls to DataSnapshot.getValue(Appointment.class)
        }

        public Appointment(String treatment, String clinic, String address, String contact, String workingHours, float temperature, String date, String time) {
            this.treatment = treatment;
            this.clinic = clinic;
            this.address = address;
            this.contact = contact;
            this.workingHours = workingHours;
            this.temperature = temperature;
            this.date = date;
            this.time = time;
        }
    }
}
