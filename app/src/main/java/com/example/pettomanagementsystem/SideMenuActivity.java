package com.example.pettomanagementsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SideMenuActivity extends AppCompatActivity {

    String sideMenuItem[] = {"Home", "User Profile", "Pet Profile", "Health Monitor", "Emergency", "Logout"};
    int sideMenuIcon[] = {R.drawable.baseline_home, R.drawable.baseline_account, R.drawable.peticon, R.drawable.healthmonitoricon, R.drawable.emergencyicon, R.drawable.baseline_logout};

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNavigationIconClick();
            }
        });

        listView = findViewById(R.id.sideMenu_list);

        BaseAdapter customBaseAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return sideMenuItem.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(SideMenuActivity.this);
                    convertView = inflater.inflate(R.layout.list_item, parent, false);
                }

                TextView menuItem = convertView.findViewById(R.id.menuItem);
                ImageView menuIcon = convertView.findViewById(R.id.menuIcon);

                menuItem.setText(sideMenuItem[position]);
                menuIcon.setImageResource(sideMenuIcon[position]);

                return convertView;
            }
        };

        listView.setAdapter(customBaseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(SideMenuActivity.this, HomeActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SideMenuActivity.this, ProfileActivity.class));
                        break;
//                    case 2:
//                        startActivity(new Intent(SideMenuActivity.this, PetProfileActivity.class));
//                        break;
                    case 3:
                        startActivity(new Intent(SideMenuActivity.this, HealthMonitorActivity.class));
                        break;
//                    case 4:
//                        startActivity(new Intent(SideMenuActivity.this, EmergencyActivity.class));
//                        break;
                    case 5:
                        logout();
                        break;
                }
            }
        });
    }

    private void onNavigationIconClick() {
        startActivity(new Intent(SideMenuActivity.this, HomeActivity.class));
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SideMenuActivity.this);
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SideMenuActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
