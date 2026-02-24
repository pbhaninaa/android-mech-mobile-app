package com.example.android_mech_app.Activities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;
import com.example.android_mech_app.Utils;
import com.example.android_mech_app.api.ApiHandler;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MechanicActivity extends BaseActivity {

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
        setupLogout(btnLogout);

        initApi();
    }

    private void setupToolbarAndDrawer() {
        setupToolbarDrawer(toolbar, drawerLayout, "Admin Dashboard");
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
        showOnly(
                HomeScreen,
                ProfileScreen,
                EarningsScreen,
                SettingsScreen,
                JobRequestScreen,
                ManageJobsScreen
        );

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
        apiHandler.getAllMerchantRequest(this,  new ApiHandler.ApiCallback<List<MechanicRequest>>() {
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
        loadProfile(txtFullName,txtUsername,txtEmail,txtPhone,txtRoles,txtCreatedAt,txtUpdatedAt);
    }


}
