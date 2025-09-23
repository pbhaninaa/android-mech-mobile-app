package com.example.android_mech_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_mech_app.Activities.AdminActivity;
import com.example.android_mech_app.Activities.CarWashActivity;
import com.example.android_mech_app.Activities.ClientActivity;
import com.example.android_mech_app.Activities.MechanicActivity;
import com.example.android_mech_app.Api.ApiClient;
import com.example.android_mech_app.Activities.UserProfileAPI;
import com.example.android_mech_app.Endpints.AuthApi;
import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.CallStructures.LoginRequest;
import com.example.android_mech_app.Models.CallStructures.LoginResponse;
import com.example.android_mech_app.Models.UserProfile;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    // --- Layouts ---
    private ConstraintLayout layoutLogin, layoutCreateUser, layoutCreateUserProfile;

    // --- Login Views ---
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvCreateUserLink;

    // --- Create User Views ---
    private EditText etUserName, etCreatePassword, etCreatePasswordConfirmation;
    private Button btnCreateUser;
    private TextView tvLoginLink;

    // --- Create UserProfile Views ---
    private EditText etProfileFirstName, etProfileLastName, etProfilePhone, etProfileAddress;
    private Button btnCreateProfile;

    // --- APIs ---
    private AuthApi authApi;
    private UserProfileAPI userProfileApi;

    // --- Token ---
    private String accessToken;
    private SharedPreferences prefs;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();
        startActivity(new Intent(MainActivity.this, ClientActivity.class));
//        prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
//        accessToken = prefs.getString("ACCESS_TOKEN", null);
//
//        if (accessToken != null) {
//            // Token already stored, validate & fetch profile
//            fetchUserProfile(accessToken);
//        } else {
//            // Show login by default
//            switchToLogin();
//        }
    }

    private void initialiseViews() {
        authApi = ApiClient.authApi();
        userProfileApi = ApiClient.userProfileApi();

        // --- Layouts ---
        layoutLogin = findViewById(R.id.layout_login);
        layoutCreateUser = findViewById(R.id.layout_create_user);
        layoutCreateUserProfile = findViewById(R.id.layout_create_user_profile);

        // --- Login Views ---
        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvCreateUserLink = findViewById(R.id.tv_create_user_link);

        // --- Create User Views ---
        etCreatePasswordConfirmation = findViewById(R.id.et_pass_confirm);
        etUserName = findViewById(R.id.et_create_user_email);
        etCreatePassword = findViewById(R.id.et_create_user_password);
        btnCreateUser = findViewById(R.id.btn_create_user);
        tvLoginLink = findViewById(R.id.tv_login_link);

        // --- Create UserProfile Views ---
        etProfileFirstName = findViewById(R.id.et_profile_first_name);
        etProfileLastName = findViewById(R.id.et_profile_last_name);
        etProfilePhone = findViewById(R.id.et_profile_phone);
        etProfileAddress = findViewById(R.id.et_profile_address);
        btnCreateProfile = findViewById(R.id.btn_create_profile);


        // --- Listeners ---
        btnLogin.setOnClickListener(v -> handleLogin());
        tvCreateUserLink.setOnClickListener(v -> switchToCreateUser());
        tvLoginLink.setOnClickListener(v -> switchToLogin());
        btnCreateUser.setOnClickListener(v -> handleCreateUser());
        btnCreateProfile.setOnClickListener(v -> handleCreateUserProfile());

        // --- Window Insets ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // --- Layout Switchers ---
    private void switchToCreateUser() {
        layoutLogin.setVisibility(View.GONE);
        layoutCreateUser.setVisibility(View.VISIBLE);
        layoutCreateUserProfile.setVisibility(View.GONE);
    }

    private void switchToLogin() {
        layoutCreateUser.setVisibility(View.GONE);
        layoutLogin.setVisibility(View.VISIBLE);
        layoutCreateUserProfile.setVisibility(View.GONE);
    }

    private void switchToCreateUserProfile() {
        layoutLogin.setVisibility(View.GONE);
        layoutCreateUser.setVisibility(View.GONE);
        layoutCreateUserProfile.setVisibility(View.VISIBLE);
    }

    // --- Login ---
    private void handleLogin() {
        String username = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        authApi.login(request).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    accessToken = response.body().getData().getAccessToken();
                    // Save token
                    prefs.edit().putString("ACCESS_TOKEN", accessToken).apply();
                    prefs.edit().putString("USERNAME", username).apply();


                    Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Timber.d("AccessToken: %s", accessToken);

                    fetchUserProfile(accessToken);

                } else {
                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Login error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Timber.e(t, "Login failed");
            }
        });
    }

    // --- Fetch UserProfile ---
    private void fetchUserProfile(String token) {
        String authHeader = "Bearer " + token;
        userProfileApi.getProfile(authHeader).enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body().getData();
                    if (profile != null && profile.getRoles() != null && !profile.getRoles().isEmpty()) {
                        Role role = profile.getRoles().iterator().next();

                        switch (role) {
                            case CLIENT:
                                startActivity(new Intent(MainActivity.this, ClientActivity.class));
                                break;
                            case MECHANIC:
                                startActivity(new Intent(MainActivity.this, MechanicActivity.class));
                                break;
                            case ADMIN:
                                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                                break;
                            case CARWASH:
                                startActivity(new Intent(MainActivity.this, CarWashActivity.class));
                                break;
                        }
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Profile not found. Please create one.", Toast.LENGTH_SHORT).show();
                        switchToCreateUserProfile();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch profile", Toast.LENGTH_SHORT).show();
                    switchToLogin();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching profile: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Timber.e(t, "Fetch profile failed");
                switchToLogin();
            }
        });
    }

    // --- Create User ---
    private void handleCreateUser() {
        String username = etUserName.getText().toString().trim();
        String pass = etCreatePasswordConfirmation.getText().toString().trim();
        String password = etCreatePassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(password)) {
            Toast.makeText(this, "Password Mismatch!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        authApi.createUser(new com.example.android_mech_app.Models.User(username, password))
                .enqueue(new Callback<ApiResponse<com.example.android_mech_app.Models.User>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<com.example.android_mech_app.Models.User>> call, Response<ApiResponse<com.example.android_mech_app.Models.User>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(MainActivity.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                            switchToLogin();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<com.example.android_mech_app.Models.User>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Timber.e(t, "Create user failed");
                    }
                });
    }

    // --- Create UserProfile ---
    private void handleCreateUserProfile() {
        String firstName = etProfileFirstName.getText().toString().trim();
        String lastName = etProfileLastName.getText().toString().trim();
        String phone = etProfilePhone.getText().toString().trim();
        String address = etProfileAddress.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile profile = new UserProfile();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setPhoneNumber(phone);
        profile.setAddress(address);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.CLIENT);
        profile.setRoles(roles);

        String authHeader = "Bearer " + accessToken;
        userProfileApi.createProfile(authHeader, profile).enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Profile created successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ClientActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to create profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Timber.e(t, "Create profile failed");
            }
        });
    }
}
