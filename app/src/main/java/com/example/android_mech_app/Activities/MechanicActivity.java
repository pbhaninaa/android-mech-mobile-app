package com.example.android_mech_app.Activities;

import static com.example.android_mech_app.api.ApiConstants.PREFS_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.Models.Payment;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;
import com.example.android_mech_app.Utils;
import com.example.android_mech_app.api.ApiClient;
import com.example.android_mech_app.api.ApiHandler;
import com.example.android_mech_app.api.ApiService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MechanicActivity extends AppCompatActivity {

    // Dashboard views
    private DrawerLayout drawerLayout;
    private ConstraintLayout HomeScreen, ProfileScreen, JobRequestScreen, EarningsScreen, SettingsScreen,ManageJobsScreen;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView loggedUserName;

    // Profile views
    private TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    private Button btnEditProfile, btnLogout;

    // Earnings
    private RecyclerView recyclerEarnings,ManageRequestRecyclerView;
    private EarningsAdapter earningsAdapter;

    private ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);

        initialiseViews();
        setupToolbarAndDrawer();
        setupNavigation();
        loadProfile();

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
        ManageJobsScreen = findViewById(R.id.manage_job_request);

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

        ManageRequestRecyclerView = findViewById(R.id.recyclerManageJobRequests);
        ManageRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Logout button
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> Utils.logout(this));

        // API handler
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Mechanic Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showScreen(HomeScreen);
            } else if (id == R.id.nav_profile) {
                showScreen(ProfileScreen);
                loadProfile();
            } else if (id == R.id.nav_job_request) {
                showScreen(JobRequestScreen);
               loadJobRequests();
            } else if (id == R.id.nav_earnings) {
                showScreen(EarningsScreen);
                setupEarnings();
            } else if (id == R.id.nav_manage_requests) {
                showScreen(ManageJobsScreen);
                loadJobRequestsById();
            } else if (id == R.id.nav_settings) {
                showScreen(SettingsScreen);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }



    private void showScreen(ConstraintLayout screenToShow) {
        HomeScreen.setVisibility(ConstraintLayout.GONE);
        ProfileScreen.setVisibility(ConstraintLayout.GONE);
        JobRequestScreen.setVisibility(ConstraintLayout.GONE);
        EarningsScreen.setVisibility(ConstraintLayout.GONE);
        SettingsScreen.setVisibility(ConstraintLayout.GONE);
        ManageJobsScreen.setVisibility(ConstraintLayout.GONE);

        screenToShow.setVisibility(ConstraintLayout.VISIBLE);
    }

    // ------------------- Job Requests -------------------
    private void loadJobRequestsById() {
        String username = Utils.getLoggedInUsername(this);

        apiHandler.getMechanicRequestsByUsername(this,username, new ApiHandler.ApiCallback<List<MechanicRequest>>() {
            @Override
            public void onSuccess(List<MechanicRequest> requests) {
                ManageRequestRecyclerView.setLayoutManager(new LinearLayoutManager(MechanicActivity.this));
                JobRequestsAdapter adapter = new JobRequestsAdapter(requests, Role.MECHANIC);
                ManageRequestRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, "Failed to load requests: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadJobRequests() {
        apiHandler.getAllMechanicRequests(this,  new ApiHandler.ApiCallback<List<MechanicRequest>>() {
            @Override
            public void onSuccess(List<MechanicRequest> requests) {
                JobRequestsAdapter adapter = new JobRequestsAdapter(requests, Role.MECHANIC);
//                recyclerMechanicRequests.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, "Failed to load requests: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // ------------------- Earnings -------------------
    private void setupEarnings() {
        String username = Utils.getLoggedInUsername(this);

        apiHandler.getPaymentsByClient(this,username, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                earningsAdapter = new EarningsAdapter(payments);
                recyclerEarnings.setAdapter(earningsAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // ------------------- Profile -------------------


    private void loadProfile() {

        apiHandler.getProfile(this, new ApiHandler.ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                txtFullName.setText(profile.getFirstName() + " " + profile.getLastName());
                txtUsername.setText(profile.getUsername());
                txtEmail.setText(profile.getEmail());
                txtPhone.setText(profile.getPhoneNumber());
                txtCreatedAt.setText(profile.getCreatedAt());
                txtUpdatedAt.setText(profile.getUpdatedAt());
                txtRoles.setText(profile.getRole());
                // Save profile to session
                Utils.saveProfile(MechanicActivity.this, profile);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                // Fallback to cached profile
                UserProfile cached = Utils.getProfile(MechanicActivity.this);
                if (cached != null) {
                    txtFullName.setText(cached.getFirstName() + " " + cached.getLastName());
                    txtUsername.setText(cached.getUsername());
                    txtEmail.setText(cached.getEmail());
                    txtPhone.setText(cached.getPhoneNumber());
                    txtCreatedAt.setText(cached.getCreatedAt());
                    txtUpdatedAt.setText(cached.getUpdatedAt());
                    Toast.makeText(MechanicActivity.this, "Loaded cached profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
