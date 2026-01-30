package com.example.android_mech_app.Activities;

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
    private ConstraintLayout HomeScreen, ProfileScreen, JobRequestScreen, EarningsScreen, SettingsScreen;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView loggedUserName;

    // Profile views
    private TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    private Button btnEditProfile, btnLogout;

    // Earnings
    private RecyclerView recyclerEarnings;
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

        screenToShow.setVisibility(ConstraintLayout.VISIBLE);
    }

    // ------------------- Job Requests -------------------
    private void loadJobRequests() {
        String username = Utils.getLoggedInUsername(this);

        apiHandler.getMechanicRequestsByUsername(username, new ApiHandler.ApiCallback<List<MechanicRequest>>() {
            @Override
            public void onSuccess(List<MechanicRequest> requests) {
                RecyclerView jobRequestsRecycler = findViewById(R.id.recyclerJobRequests);
                jobRequestsRecycler.setLayoutManager(new LinearLayoutManager(MechanicActivity.this));
                JobRequestsAdapter adapter = new JobRequestsAdapter(requests, Role.MECHANIC);
                jobRequestsRecycler.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, "Failed to load requests: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------- Earnings -------------------
    private void setupEarnings() {
        Long mechanicId = Utils.getLoggedInUserId(this);

        apiHandler.getPaymentsByMechanic(mechanicId, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                earningsAdapter = new EarningsAdapter(payments);
                recyclerEarnings.setAdapter(earningsAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, "Failed to load earnings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------- Profile -------------------
    private void loadProfile() {
        if (loadProfileFromCache()) return; // Load from cache if exists

        String token = Utils.getAuthToken(this);

        apiHandler.getProfile(token, new ApiHandler.ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                // Full name
                String fullName = String.format("%s %s",
                        profile.getLastName() != null ? profile.getLastName() : "",
                        profile.getFirstName() != null ? profile.getFirstName() : "");
                txtFullName.setText(fullName);

                // Username, email, phone
                txtUsername.setText(profile.getUsername() != null ? profile.getUsername() : "");
                txtEmail.setText(profile.getEmail() != null ? profile.getEmail() : "");
                txtPhone.setText(profile.getPhoneNumber() != null ? profile.getPhoneNumber() : "");

                // Single role
                txtRoles.setText(profile.getRole() != null ? profile.getRole() : "No role");

                // Dates (createdAt and updatedAt)
                txtCreatedAt.setText(profile.getCreatedAt() != null ? profile.getCreatedAt().toString() : "");
                txtUpdatedAt.setText(profile.getUpdatedAt() != null ? profile.getUpdatedAt().toString() : "");

                // Status (if you add a getStatus method in UserProfile)
                // txtStatus.setText(profile.getStatus() != null ? profile.getStatus() : "");
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MechanicActivity.this, "Failed to load profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // ------------------- Local caching -------------------
    private void saveProfileLocally(UserProfile profile) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("PROFILE_FIRSTNAME", profile.getFirstName());
        editor.putString("PROFILE_LASTNAME", profile.getLastName());
        editor.putString("PROFILE_USERNAME", profile.getUsername());
        editor.putString("PROFILE_EMAIL", profile.getEmail());
        editor.putString("PROFILE_PHONE", profile.getPhoneNumber());

        // Save single role
        editor.putString("PROFILE_ROLE", profile.getRole() != null ? profile.getRole() : "No role");

        // Convert LocalDateTime to string
        editor.putString("PROFILE_CREATED_AT", profile.getCreatedAt() != null ? profile.getCreatedAt().toString() : "");
        editor.putString("PROFILE_UPDATED_AT", profile.getUpdatedAt() != null ? profile.getUpdatedAt().toString() : "");

        // Status if needed
        // editor.putString("PROFILE_STATUS", profile.getStatus());

        editor.apply();
    }



    private boolean loadProfileFromCache() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("PROFILE_USERNAME", null);

        if (username != null) {
            txtFullName.setText(prefs.getString("PROFILE_FULLNAME", ""));
            txtUsername.setText(username);
            txtEmail.setText(prefs.getString("PROFILE_EMAIL", ""));
            txtPhone.setText(prefs.getString("PROFILE_PHONE", ""));
            txtRoles.setText(prefs.getString("PROFILE_ROLES", ""));
            txtStatus.setText(prefs.getString("PROFILE_STATUS", ""));
            txtCreatedAt.setText(prefs.getString("PROFILE_CREATED_AT", ""));
            txtUpdatedAt.setText(prefs.getString("PROFILE_UPDATED_AT", ""));
            return true;
        }
        return false;
    }
}
