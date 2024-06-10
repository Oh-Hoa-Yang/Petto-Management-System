package com.example.pettomanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectClinicActivity extends AppCompatActivity {

    private final String[][] clinic_details1 = {
            {"Clinic A", "Jalan Batu Balik, 26600 Pekan", "1234567890", "0800-2000"},
            {"Clinic B", "Jalan Tanah Putih, 25100 Kuantan", "0987654321", "1000-2000"},
            {"Clinic C", "Jalan Hospital, 47000 Sungai Buloh", "1357924680", "0900-1800"},
            {"Clinic D", "Jalan Langat, 41200 Klang", "2468135790", "0900-2200"},
            {"Clinic E", "Jalan Semenyih, 43000 Kajang", "2468135790", "0800-1900"},
    };
    private final String[][] clinic_details2 = {
            {"Clinic F", "Jalan Hospital, 47000 Sungai Buloh","1234567890", "1000-2000"},
            {"Clinic G", "Jalan Langat, 41200 Klang","0987654321", "0900-1800"},
            {"Clinic H", "Jalan Batu Balik, 26600 Pekan","1357924680", "0800-2000"},
            {"Clinic I", "Jalan Semenyih, 43000 Kajang", "2468135790", "0800-1900"},
            {"Clinic J", "Jalan Tanah Putih, 25100 Kuantan","2468135790", "0900-2200"},
    };
    private final String[][] clinic_details3 = {
            {"Clinic K", "Jalan Tanah Putih, 25100 Kuantan","1234567890", "0900-1800"},
            {"Clinic L", "Jalan Batu Balik, 26600 Pekan","0987654321", "0800-2000"},
            {"Clinic M", "Jalan Hospital, 47000 Sungai Buloh","1357924680", "1000-2000"},
            {"Clinic N", "Jalan Langat, 41200 Klang","2468135790", "0800-1900"},
            {"Clinic O", "Jalan Semenyih, 43000 Kajang", "2468135790", "0900-2200"},
    };
    private final String[][] clinic_details4 = {
            {"Clinic P", "Jalan Langat, 41200 Klang","1234567890", "0800-1900"},
            {"Clinic Q", "Jalan Hospital, 47000 Sungai Buloh","0987654321", "0900-1800"},
            {"Clinic R", "Jalan Tanah Putih, 25100 Kuantan","1357924680", "0900-2200"},
            {"Clinic S", "Jalan Batu Balik, 26600 Pekan","2468135790", "0800-2000"},
            {"Clinic T", "Jalan Semenyih, 43000 Kajang", "2468135790", "1000-2000"},
    };
    private final String[][] clinic_details5 = {
            {"Clinic U", "Jalan Batu Balik, 26600 Pekan","1234567890", "0900-2200"},
            {"Clinic V", "Jalan Hospital, 47000 Sungai Buloh","0987654321", "0800-1900"},
            {"Clinic W", "Jalan Langat, 41200 Klang","1357924680", "0900-1800"},
            {"Clinic X", "Jalan Tanah Putih, 25100 Kuantan","2468135790", "1000-2000"},
            {"Clinic Y", "Jalan Semenyih, 43000 Kajang", "2468135790", "0800-2000"},
    };
    private final String[][] clinic_details6 = {
            {"Clinic Z", "Jalan Hospital, 47000 Sungai Buloh","1234567890", "1000-2000"},
            {"Clinic A", "Jalan Langat, 41200 Klang","0987654321", "0800-2000"},
            {"Clinic C", "Jalan Semenyih, 43000 Kajang", "1357924680", "0900-1800"},
            {"Clinic D", "Jalan Tanah Putih, 25100 Kuantan","2468135790", "0800-1900"},
            {"Clinic E", "Jalan Batu Balik, 26600 Pekan","2468135790", "0900-2200"},
    };



    TextView treatmentTitle;
    Button back;
    String[][] clinic_details = {};
    ArrayList<HashMap<String,String>> list = new ArrayList<>();

    SimpleAdapter adapter;
    HashMap<String,String> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_clinic);

        treatmentTitle = findViewById(R.id.textViewSelectTreatmentTitle);
        Intent intent = getIntent();
        String treatment_type = intent.getStringExtra("treatment");
        treatmentTitle.setText(treatment_type);

        back = findViewById(R.id.buttonBack);

        if(treatment_type != null) {
            if(treatment_type.compareTo("Checkup") == 0){
                clinic_details = clinic_details1;
            } else if(treatment_type.compareTo("Consultation") == 0){
                clinic_details = clinic_details2;
            } else if(treatment_type.compareTo("Dental") == 0){
                clinic_details = clinic_details3;
            } else if(treatment_type.compareTo("Grooming") == 0){
                clinic_details = clinic_details4;
            } else if(treatment_type.compareTo("Surgery") == 0){
                clinic_details = clinic_details5;
            } else if(treatment_type.compareTo("Vaccination") == 0){
                clinic_details = clinic_details6;
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectClinicActivity.this, SelectTreatmentsActivity.class));
            }
        });

        for(int i = 0; i < clinic_details.length; i++){
            item = new HashMap<>();
            item.put("line1", clinic_details[i][0]);
            item.put("line2", clinic_details[i][1]);
            item.put("line3", clinic_details[i][2]);
            item.put("line4", clinic_details[i][3]);
            list.add(item);
        }
        adapter = new SimpleAdapter(this,
                list,
                R.layout.multi_lines,
                new String[]{"line1","line2","line3","line4"},
                new int[]{R.id.textViewLineA, R.id.textViewLineB, R.id.textViewLineC, R.id.textViewLineD});

        ListView listView = findViewById(R.id.listViewClinicList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SelectClinicActivity.this, BookTreatmentActivity.class);
                intent.putExtra("treatment", treatment_type);
                intent.putExtra("clinic", clinic_details[i][0]);
                intent.putExtra("address", clinic_details[i][1]);
                intent.putExtra("contact", clinic_details[i][2]);
                intent.putExtra("working_hours", clinic_details[i][3]);
                startActivity(intent);
            }
        });
    }
}