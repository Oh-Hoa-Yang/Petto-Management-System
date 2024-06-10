package com.example.pettomanagementsystem;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class BookTreatmentActivity extends AppCompatActivity {

    EditText editTextTreatment, editTextClinic, editTextAddress, editTextContact, editTextWorkingHours;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button buttonDate, buttonTime, buttonNext, buttonCancel;
    private String selectedDate, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_treatment);

        editTextTreatment = findViewById(R.id.editTextTreatment);
        editTextClinic = findViewById(R.id.editTextClinic);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextContact = findViewById(R.id.editTextContact);
        editTextWorkingHours = findViewById(R.id.editTextWorkingHours);
        buttonDate = findViewById(R.id.buttonAppDate);
        buttonTime = findViewById(R.id.buttonAppTime);

        editTextClinic.setKeyListener(null);
        editTextAddress.setKeyListener(null);
        editTextContact.setKeyListener(null);
        editTextWorkingHours.setKeyListener(null);

        Intent intent = getIntent();
        String treatment = intent.getStringExtra("treatment");
        String clinic = intent.getStringExtra("clinic");
        String address = intent.getStringExtra("address");
        String contact = intent.getStringExtra("contact");
        String workingHours = intent.getStringExtra("working_hours");

        editTextTreatment.setText(treatment);
        editTextClinic.setText(clinic);
        editTextAddress.setText(address);
        editTextContact.setText(contact);
        editTextWorkingHours.setText(workingHours);

        //date picker
        initDatePicker();
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //time picker
        initTimePicker();
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookTreatmentActivity.this, SelectTreatmentsActivity.class));
            }
        });

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookTreatmentActivity.this, DisplayTemperatureActivity.class);
                intent.putExtra("treatment", treatment);
                intent.putExtra("clinic", clinic);
                intent.putExtra("address", address);
                intent.putExtra("contact", contact);
                intent.putExtra("working_hours", workingHours);
                intent.putExtra("date", selectedDate);
                intent.putExtra("time", selectedTime);
                startActivity(intent);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                selectedDate = dayOfMonth + "/" + month + "/" + year;
                buttonDate.setText(selectedDate);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                selectedTime = hour + ":" + minute;
                buttonTime.setText(selectedTime);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
    }
}
