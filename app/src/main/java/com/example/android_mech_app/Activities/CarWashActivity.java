package com.example.android_mech_app.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mech_app.Adapters.EarningsAdapter;
import com.example.android_mech_app.Adapters.ManageWashesAdapter;
import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarWashActivity extends AppCompatActivity {
    // Dashboard views
    DrawerLayout drawerLayout;
    ConstraintLayout HomeScreen, ProfileScreen, BookingsScreen, ManageWashesScreen, EarningsScreen, SettingsScreen;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView loggedUserName;

    // Profile views
    TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    Button btnEditProfile;

    // Earnings
    RecyclerView recyclerEarnings;
    EarningsAdapter earningsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);

        // Initialize views
        initialiseViews();

        // Toolbar and Drawer
        setSupportActionBar(toolbar);
        toolbar.setTitle("Nice and Clean Car Wash");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation item click handling
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    showScreen(HomeScreen);
                    Toast.makeText(CarWashActivity.this, "Home clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    showScreen(ProfileScreen);
                    Toast.makeText(CarWashActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.bookings) {
                    showScreen(BookingsScreen);
                    loadManageBookings();
                    Toast.makeText(CarWashActivity.this, "Bookings clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.manage_washes) {
                    showScreen(ManageWashesScreen);
                    loadManageWashes();
                    Toast.makeText(CarWashActivity.this, "Manage Washes clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_earnings) {
                    showScreen(EarningsScreen);
                    setupEarnings(); // 🔥 Load demo data
                } else if (id == R.id.nav_settings) {
                    showScreen(SettingsScreen);
                    Toast.makeText(CarWashActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Show Home screen by default
        showScreen(HomeScreen);
    }

    private void initialiseViews() {
        // Dashboard
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        loggedUserName = findViewById(R.id.loggedUsername);
        HomeScreen = findViewById(R.id.dashboard);
        ProfileScreen = findViewById(R.id.profile);
        BookingsScreen = findViewById(R.id.bookings);
        ManageWashesScreen = findViewById(R.id.manage_washes);
        EarningsScreen = findViewById(R.id.earnings);
        SettingsScreen = findViewById(R.id.settings);

        // Profile
        txtFullName = findViewById(R.id.txtFullName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtRoles = findViewById(R.id.txtRoles);
        txtStatus = findViewById(R.id.txtStatus);
        txtCreatedAt = findViewById(R.id.txtCreatedAt);
        txtUpdatedAt = findViewById(R.id.txtUpdatedAt);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        // Earnings
        recyclerEarnings = findViewById(R.id.recyclerEarnings);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showScreen(ConstraintLayout screenToShow) {
        // Hide all screens
        HomeScreen.setVisibility(View.GONE);
        ProfileScreen.setVisibility(View.GONE);
        BookingsScreen.setVisibility(View.GONE);
        ManageWashesScreen.setVisibility(View.GONE);
        EarningsScreen.setVisibility(View.GONE);
        SettingsScreen.setVisibility(View.GONE);

        // Show selected screen
        screenToShow.setVisibility(View.VISIBLE);
    }
    private void loadManageWashes() {
        List<CarWashBooking> bookings = new ArrayList<>();

        bookings.add(new CarWashBooking("TBhani", "dg", "SUV", "gdf",
                List.of("Exterior Wash"), 50.0, "2025-09-19", "e"));
        bookings.get(0).setStatus("Completed");

        bookings.add(new CarWashBooking("TBhani", "yu", "SUV", "ugv",
                Arrays.asList("Exterior Wash", "Full Wash"), 170.0, "2025-09-19", "ty"));
        bookings.get(1).setStatus("Completed");

        bookings.add(new CarWashBooking("TBhani", "u4tu6", "Sedan", "rtujrtu",
                Arrays.asList("Full Wash", "Exterior Wash"), 170.0, "2025-09-18", "wgwrgergh"));
        bookings.get(2).setStatus("Completed");

        bookings.add(new CarWashBooking("TBhani", "jfyjfyjfh", "SUV", "ryui",
                Arrays.asList("Interior Cleaning","Exterior Wash","Undercarriage Wash","Engine Wash","Valet Service"), 540.0, "2025-09-21", "fjkyfhkhkghk"));
        bookings.get(3).setStatus("In Progress");

        RecyclerView manageWashes = findViewById(R.id.recyclerManageWashes);
        manageWashes.setLayoutManager(new LinearLayoutManager(this));
        ManageWashesAdapter adapter = new ManageWashesAdapter(bookings);
        manageWashes.setAdapter(adapter);


    }
    private void loadManageBookings() {
        List<CarWashBooking> bookings = new ArrayList<>();

        bookings.add(new CarWashBooking("TBhani", "dg", "SUV", "gdf",
                List.of("Exterior Wash"), 50.0, "2025-09-19", "e"));
        bookings.get(0).setStatus("Pending");

        bookings.add(new CarWashBooking("TBhani", "yu", "SUV", "ugv",
                Arrays.asList("Exterior Wash", "Full Wash"), 170.0, "2025-09-19", "ty"));
        bookings.get(1).setStatus("Pending");

        bookings.add(new CarWashBooking("TBhani", "u4tu6", "Sedan", "rtujrtu",
                Arrays.asList("Full Wash", "Exterior Wash"), 170.0, "2025-09-18", "wgwrgergh"));
        bookings.get(2).setStatus("Pending");

        bookings.add(new CarWashBooking("TBhani", "jfyjfyjfh", "SUV", "ryui",
                Arrays.asList("Interior Cleaning","Exterior Wash","Undercarriage Wash","Engine Wash","Valet Service"), 540.0, "2025-09-21", "fjkyfhkhkghk"));
        bookings.get(3).setStatus("Pending");


        RecyclerView manageBookings = findViewById(R.id.recyclerBookings);
        manageBookings.setLayoutManager(new LinearLayoutManager(this));
        ManageWashesAdapter adapter = new ManageWashesAdapter(bookings);
        manageBookings.setAdapter(adapter);


    }

    private void setupEarnings() {
        RecyclerView recyclerView = findViewById(R.id.recyclerEarnings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Earning> demoEarnings = new ArrayList<>();
        demoEarnings.add(new Earning(
                8,                // id
                45.0,             // amount
                "TBhani",         // clientUsername
                1,                // jobId
                null,             // mechanicId
                5,                // carWashId
                5.0,              // platformFee
                "2025-09-19T23:01:53", // paidAt
                "Car Wash service"      // jobDescription
        ));

        demoEarnings.add(new Earning(
                9,
                153.0,
                "TBhani",
                2,
                null,
                5,
                17.0,
                "2025-09-19T23:04:46",
                "Car Wash service"
        ));

        EarningsAdapter adapter = new EarningsAdapter(demoEarnings);
        recyclerView.setAdapter(adapter);
    }


}
