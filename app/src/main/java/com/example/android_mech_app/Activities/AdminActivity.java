package com.example.android_mech_app.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.Models.Payment;
import com.example.android_mech_app.Models.User;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;
import com.example.android_mech_app.Utils;
import com.example.android_mech_app.api.ApiClient;
import com.example.android_mech_app.api.ApiHandler;
import com.example.android_mech_app.api.ApiService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    // Layout & Navigation
    private DrawerLayout drawerLayout;
    private ConstraintLayout screenDashboard, screenProfile, screenUserManagement,
            screenSettings, screenEarnings, screenCarWashManagement, screenMechanicRequests;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView loggedUserName;

    // Profile views
    private TextView txtFullName, txtUsername, txtEmail, txtPhone,
            txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    private Button btnEditProfile, btnLogout;

    // RecyclerViews
    private RecyclerView recyclerUsers, recyclerEarnings, recyclerMechanicRequests,
            recyclerCarWashBookings;
    private UserAdapter userAdapter;
    private EarningsAdapter earningsAdapter;
    private ManageWashesAdapter carWashAdapter;

    private List<LocalUser> userList = new ArrayList<>();
    private ApiHandler apiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initialiseViews();
        setupToolbarDrawer();
        setupNavigation();

        // Default screen
        showDashboardScreen();

        // Edit profile button
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "Go to Edit Profile screen", Toast.LENGTH_SHORT).show()
        );

        // Logout
        btnLogout.setOnClickListener(v -> Utils.logout(this));
    }

    // ----------------- Initialise Views -----------------
    private void initialiseViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.admin_navigationView);
        toolbar = findViewById(R.id.admin_toolbar);
        loggedUserName = findViewById(R.id.loggedUsername);

        screenDashboard = findViewById(R.id.admin_dashboard);
        screenProfile = findViewById(R.id.admin_profile);
        screenSettings = findViewById(R.id.admin_settings);
        screenUserManagement = findViewById(R.id.admin_user_management);
        screenEarnings = findViewById(R.id.earnings);
        screenCarWashManagement = findViewById(R.id.manage_washes);
        screenMechanicRequests = findViewById(R.id.bookings);

        // Profile TextViews
        txtFullName = findViewById(R.id.txtFullName);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtRoles = findViewById(R.id.txtRoles);
        txtStatus = findViewById(R.id.txtStatus);
        txtCreatedAt = findViewById(R.id.txtCreatedAt);
        txtUpdatedAt = findViewById(R.id.txtUpdatedAt);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // RecyclerViews
        recyclerUsers = findViewById(R.id.usersRecyclerView);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));

        recyclerEarnings = findViewById(R.id.recyclerEarnings);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));

        recyclerMechanicRequests = findViewById(R.id.recyclerJobRequests);
        recyclerMechanicRequests.setLayoutManager(new LinearLayoutManager(this));

        recyclerCarWashBookings = findViewById(R.id.recyclerManageWashes);
        recyclerCarWashBookings.setLayoutManager(new LinearLayoutManager(this));

        // Setup API handler
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);
    }

    // ----------------- Toolbar & Drawer -----------------
    private void setupToolbarDrawer() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // ----------------- Navigation -----------------
    private void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showDashboardScreen();
            } else if (id == R.id.nav_profile) {
                showProfileScreen();
            } else if (id == R.id.user_management) {
                showUserManagementScreen();
            } else if (id == R.id.nav_earnings) {
                showEarningsScreen();
            } else if (id == R.id.nav_carwah_managements) {
                showCarWashManagementScreen();
            } else if (id == R.id.na_mechanic_management) {
                showMechanicRequestsScreen();
            } else if (id == R.id.nav_settings) {
                showSettingsScreen();
            } else {
                showDashboardScreen();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Default selected item
        navigationView.setCheckedItem(R.id.nav_home);
    }

    // ----------------- Screen Display Methods -----------------
    private void showDashboardScreen() {
        hideAllScreens();
        screenDashboard.setVisibility(View.VISIBLE);
        loadUserProfile();
    }

    private void showProfileScreen() {
        hideAllScreens();
        screenProfile.setVisibility(View.VISIBLE);
        loadUserProfile();
    }

    private void showUserManagementScreen() {
        hideAllScreens();
        screenUserManagement.setVisibility(View.VISIBLE);
        loadUsers();
    }

    private void showEarningsScreen() {
        hideAllScreens();
        screenEarnings.setVisibility(View.VISIBLE);
        loadEarnings();
    }

    private void showCarWashManagementScreen() {
        hideAllScreens();
        screenCarWashManagement.setVisibility(View.VISIBLE);
        loadCarWashBookings();
    }

    private void showMechanicRequestsScreen() {
        hideAllScreens();
        screenMechanicRequests.setVisibility(View.VISIBLE);
        loadMechanicRequests();
    }

    private void showSettingsScreen() {
        hideAllScreens();
        screenSettings.setVisibility(View.VISIBLE);
    }

    private void hideAllScreens() {
        screenDashboard.setVisibility(View.GONE);
        screenProfile.setVisibility(View.GONE);
        screenUserManagement.setVisibility(View.GONE);
        screenSettings.setVisibility(View.GONE);
        screenEarnings.setVisibility(View.GONE);
        screenCarWashManagement.setVisibility(View.GONE);
        screenMechanicRequests.setVisibility(View.GONE);
    }

    // ----------------- Load Data Methods -----------------
    private void loadUserProfile() {
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
                Utils.saveProfile(AdminActivity.this, profile);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AdminActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUsers() {
        apiHandler.getAllProfiles(this, new ApiHandler.ApiCallback<List<UserProfile>>() {
            @Override
            public void onSuccess(List<UserProfile> apiUsers) {
                userList.clear();

                // Map API Users to LocalUser
                for (UserProfile apiUser : apiUsers) {
                    userList.add(new LocalUser(
                            apiUser.getFirstName() + " " + apiUser.getLastName(),
                            apiUser.getUsername(),
                            apiUser.getEmail(),
                            apiUser.getPhoneNumber(),
                            apiUser.getRoles() != null ? apiUser.getRoles().toString() : ""
                    ));
                }

                if (userAdapter == null) {
                    userAdapter = new UserAdapter(userList);
                    recyclerUsers.setAdapter(userAdapter);
                } else {
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AdminActivity.this,
                        "Failed to load users: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMechanicRequests() {
        apiHandler.getAllMechanicRequests(this,  new ApiHandler.ApiCallback<List<MechanicRequest>>() {
            @Override
            public void onSuccess(List<MechanicRequest> requests) {
                JobRequestsAdapter adapter = new JobRequestsAdapter(requests, Role.MECHANIC);
                recyclerMechanicRequests.setAdapter(adapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AdminActivity.this, "Failed to load requests: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCarWashBookings() {
        apiHandler.getAllCarWashBookings(this, new ApiHandler.ApiCallback<List<CarWashBooking>>() {
            @Override
            public void onSuccess(List<CarWashBooking> bookings) {
                carWashAdapter = new ManageWashesAdapter(bookings);
                recyclerCarWashBookings.setAdapter(carWashAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AdminActivity.this, "Failed to load bookings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEarnings() {
        apiHandler.getAllPayments(this, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                earningsAdapter = new EarningsAdapter(payments);
                recyclerEarnings.setAdapter(earningsAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(AdminActivity.this, "Failed to load earnings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----------------- Local User Model & Adapter -----------------
    static class LocalUser {
        String fullName, username, email, phone, roles;
        LocalUser(String fullName, String username, String email, String phone, String roles) {
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.roles = roles;
        }
    }

    class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        List<LocalUser> users;
        UserAdapter(List<LocalUser> users) { this.users = users; }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            LocalUser user = users.get(position);
            holder.tvFullName.setText("Full Name: " + user.fullName);
            holder.tvUsername.setText("Username: " + user.username);
            holder.tvEmail.setText("Email: " + user.email);
            holder.tvPhone.setText("Phone: " + user.phone);
            holder.tvRoles.setText("Roles: " + user.roles);

            holder.btnAction.setOnClickListener(v ->
                    Toast.makeText(AdminActivity.this,
                            "Managing " + user.username, Toast.LENGTH_SHORT).show()
            );
        }

        @Override
        public int getItemCount() { return users.size(); }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvFullName, tvUsername, tvEmail, tvPhone, tvRoles;
            Button btnAction;
            UserViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFullName = itemView.findViewById(R.id.tvFullName);
                tvUsername = itemView.findViewById(R.id.tvUsername);
                tvEmail = itemView.findViewById(R.id.tvEmail);
                tvPhone = itemView.findViewById(R.id.tvPhone);
                tvRoles = itemView.findViewById(R.id.tvRoles);
                btnAction = itemView.findViewById(R.id.btnAction);
            }
        }
    }
}