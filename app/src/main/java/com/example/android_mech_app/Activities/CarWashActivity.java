package com.example.android_mech_app.Activities;

import android.content.Intent;
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
import com.example.android_mech_app.MainActivity;
import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.Models.Payment;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Utils;
import com.example.android_mech_app.api.ApiClient;
import com.example.android_mech_app.api.ApiHandler;
import com.example.android_mech_app.api.ApiService;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CarWashActivity extends BaseActivity {

    // Dashboard views
    private DrawerLayout drawerLayout;
    private ConstraintLayout HomeScreen, ProfileScreen, BookingsScreen, ManageWashesScreen, EarningsScreen, SettingsScreen;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView loggedUserName;

    // Profile views
    private TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    private Button btnEditProfile, btnLogout;

    // Earnings
    private RecyclerView recyclerEarnings;
    private EarningsAdapter earningsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);
        initialiseViews();
        setupToolbarAndDrawer();
        setupNavigation();

        showScreen(HomeScreen);
    }

    private void initialiseViews() {
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

        txtFullName = findViewById(R.id.txtFullName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtRoles = findViewById(R.id.txtRoles);
        txtStatus = findViewById(R.id.txtStatus);
        txtCreatedAt = findViewById(R.id.txtCreatedAt);
        txtUpdatedAt = findViewById(R.id.txtUpdatedAt);
        btnEditProfile = findViewById(R.id.btnEditProfile);

        recyclerEarnings = findViewById(R.id.recyclerEarnings);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));

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
                loadUserProfile();
            } else if (id == R.id.nav_profile) {
                showScreen(ProfileScreen);
                loadUserProfile();

            } else if (id == R.id.bookings) {
                showScreen(BookingsScreen);
                loadManageBookings();
            } else if (id == R.id.manage_washes) {
                showScreen(ManageWashesScreen);
                loadManageWashes();
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

        showOnly(
                HomeScreen,
                ProfileScreen,
                EarningsScreen,
                SettingsScreen,BookingsScreen,ManageWashesScreen
        );

        screenToShow.setVisibility(View.VISIBLE);
    }

    private void loadManageWashes() {
        apiHandler.getBookingsByClient(this, Utils.getLoggedInUsername(this), new ApiHandler.ApiCallback<List<CarWashBooking>>() {
            @Override
            public void onSuccess(List<CarWashBooking> bookings) {
                if (bookings == null || bookings.isEmpty()) {
                    Toast.makeText(CarWashActivity.this, "No bookings found.", Toast.LENGTH_SHORT).show();
                    return;
                }

                RecyclerView manageBookings = findViewById(R.id.recyclerBookings);
                manageBookings.setLayoutManager(new LinearLayoutManager(CarWashActivity.this));
                manageBookings.setAdapter(new ManageWashesAdapter(bookings)); // Pass the list directly
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CarWashActivity.this, "Failed to load bookings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadManageBookings() {
        apiHandler.getAllCarWashBookings(this, new ApiHandler.ApiCallback<List<CarWashBooking>>() {
            @Override
            public void onSuccess(List<CarWashBooking> bookings) {
//                carWashAdapter = new ManageWashesAdapter(bookings);
//                recyclerCarWashBookings.setAdapter(carWashAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CarWashActivity.this, "Failed to load bookings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfile() {
        loadProfile(txtFullName,txtUsername,txtEmail,txtPhone,txtRoles,txtCreatedAt,txtUpdatedAt);

    }
    private void setupEarnings() {
        Long userId = Utils.getLoggedInUserId(this);

        String username = Utils.getLoggedInUsername(this);

        apiHandler.getPaymentsByClient(this,username, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                recyclerEarnings.setAdapter(new EarningsAdapter(payments));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(CarWashActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
