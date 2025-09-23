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

import com.example.android_mech_app.R;
import com.example.android_mech_app.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ConstraintLayout HomeScreen, ProfileScreen, UserManagementScreen, SettingsScreen;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView loggedUserName;

    // Profile views
    TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    Button btnEditProfile,Logout;

    // User Management views
    RecyclerView usersRecyclerView;
    UserAdapter userAdapter;
    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        initialiseViews();

        // Toolbar and Drawer
        setSupportActionBar(toolbar);
        toolbar.setTitle("Admin side");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation item click handling
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showScreen(HomeScreen);
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_profile) {
                showScreen(ProfileScreen);
                loadProfileData();
            } else if (id == R.id.user_management) {
                showScreen(UserManagementScreen);
                loadUsers();
            } else if (id == R.id.nav_settings) {
                showScreen(SettingsScreen);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Default screen
        showScreen(HomeScreen);

        // Edit profile button
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "Go to Edit Profile screen", Toast.LENGTH_SHORT).show()
        );
    }

    private void initialiseViews() {
        // Dashboard
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.admin_navigationView);
        toolbar = findViewById(R.id.admin_toolbar);
        loggedUserName = findViewById(R.id.loggedUsername);
        HomeScreen = findViewById(R.id.admin_dashboard);
        ProfileScreen = findViewById(R.id.admin_profile);
        SettingsScreen = findViewById(R.id.admin_settings);
        UserManagementScreen = findViewById(R.id.admin_user_management);

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

        // User Management
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Logout=findViewById(R.id.btnLogout);



        Logout.setOnClickListener(v->{
            Utils.logout(this);
        });
    }

    private void showScreen(ConstraintLayout screenToShow) {
        HomeScreen.setVisibility(View.GONE);
        ProfileScreen.setVisibility(View.GONE);
        UserManagementScreen.setVisibility(View.GONE);
        SettingsScreen.setVisibility(View.GONE);

        screenToShow.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void loadProfileData() {
        // Mock profile data
        txtFullName.setText("John Doe");
        txtUsername.setText("johndoe");
        txtEmail.setText("john@example.com");
        txtPhone.setText("123-456-7890");
        txtRoles.setText("ADMIN, USER");
        txtStatus.setText("Enabled");
        txtCreatedAt.setText("2025-01-01 10:00");
        txtUpdatedAt.setText("2025-09-20 15:30");
    }

    private void loadUsers() {
        userList.clear();
        userList.add(new User("Alice Smith", "alice", "alice@mail.com", "123456789", "ADMIN"));
        userList.add(new User("Bob Johnson", "bobby", "bob@mail.com", "987654321", "USER"));
        userList.add(new User("Carol White", "carolw", "carol@mail.com", "555555555", "MODERATOR"));

        userAdapter = new UserAdapter(userList);
        usersRecyclerView.setAdapter(userAdapter);
    }

    // ------------------------------
    // Inner User model class
    // ------------------------------
    static class User {
        String fullName, username, email, phone, roles;

        User(String fullName, String username, String email, String phone, String roles) {
            this.fullName = fullName;
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.roles = roles;
        }
    }

    // ------------------------------
    // Inner RecyclerView Adapter
    // ------------------------------
    class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        List<User> users;

        UserAdapter(List<User> users) {
            this.users = users;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = users.get(position);
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
        public int getItemCount() {
            return users.size();
        }

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
