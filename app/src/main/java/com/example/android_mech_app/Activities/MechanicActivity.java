package com.example.android_mech_app.Activities;

import android.os.Build;
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
import com.example.android_mech_app.Adapters.JobRequestsAdapter;
import com.example.android_mech_app.Adapters.ManageWashesAdapter;
import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MechanicActivity extends AppCompatActivity {



        // Dashboard views
        DrawerLayout drawerLayout;
        ConstraintLayout HomeScreen, ProfileScreen, JobRequestScreen, EarningsScreen, SettingsScreen;
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
            setContentView(R.layout.activity_mechanic);

            // Initialize views
            initialiseViews();

            // Toolbar and Drawer
            setSupportActionBar(toolbar);
            toolbar.setTitle("Mechanic Dash");

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
                    } else if (id == R.id.nav_profile) {
                        showScreen(ProfileScreen);
                    } else if (id == R.id.nav_job_request) {
                        showScreen(JobRequestScreen);
                        loadJobRequests();
                    } else if (id == R.id.nav_earnings) {
                        showScreen(EarningsScreen);
                        setupEarnings();
                    } else if (id == R.id.nav_settings) {
                        showScreen(SettingsScreen);
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
            JobRequestScreen = findViewById(R.id.job_request);
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
            EarningsScreen.setVisibility(View.GONE);
            SettingsScreen.setVisibility(View.GONE);
            JobRequestScreen.setVisibility(View.GONE);

            // Show selected screen
            screenToShow.setVisibility(View.VISIBLE);
        }

    private void loadJobRequests() {
        List<MechanicRequest> requests = new ArrayList<>();

        requests.add(new MechanicRequest() {{
            setId(1L);
            setUsername("TBhani");
            setDescription("Replace brake pads");
            setLocation("Buccleuch, Johannesburg Ward 32, Sandton, City of Johannesburg, Gauteng, 2054, South Africa");
            setLatitude(-26.0679378);
            setLongitude(28.1027465);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setDate(LocalDate.of(2025, 9, 18));
            }
            setStatus("pending");
        }});

        requests.add(new MechanicRequest() {{
            setId(2L);
            setUsername("JSmith");
            setDescription("Oil change");
            setLocation("Rosebank, Johannesburg, Gauteng, 2196, South Africa");
            setLatitude(-26.123456);
            setLongitude(28.123456);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setDate(LocalDate.of(2025, 9, 20));
            }
            setStatus("pending");
        }});

        requests.add(new MechanicRequest() {{
            setId(3L);
            setUsername("AMkhize");
            setDescription("Battery replacement");
            setLocation("Sandton, Johannesburg, Gauteng, 2196, South Africa");
            setLatitude(-26.065432);
            setLongitude(28.102345);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setDate(LocalDate.of(2025, 9, 21));
            }
            setStatus("pending");
        }});
        Role role = Role.MECHANIC;
        RecyclerView jobRequestsRecycler = findViewById(R.id.recyclerJobRequests);
        jobRequestsRecycler.setLayoutManager(new LinearLayoutManager(this));
        JobRequestsAdapter adapter = new JobRequestsAdapter(requests,role);
        jobRequestsRecycler.setAdapter(adapter);
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
