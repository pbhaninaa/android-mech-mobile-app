package com.example.android_mech_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.android_mech_app.Activities.AdminActivity;
import com.example.android_mech_app.Activities.CarWashActivity;
import com.example.android_mech_app.Activities.ClientActivity;
import com.example.android_mech_app.Activities.MechanicActivity;
import com.example.android_mech_app.Models.CallStructures.LoginRequest;
import com.example.android_mech_app.Models.CallStructures.LoginResponse;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.api.ApiClient;
import com.example.android_mech_app.api.ApiHandler;
import com.example.android_mech_app.api.ApiService;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // Layouts
    private ScrollView layoutLogin, layoutCreateUser, layoutCreateUserProfile;

    // Login views
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvCreateUserLink;

    // API
    private ApiHandler apiHandler;
    private SharedPreferences prefs;
    private String accessToken;

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String KEY_USER_PROFILE = "LOGGED_USER_PROFILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialiseViews();

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);

        // Check if profile is already saved
        UserProfile savedProfile = getSavedUserProfile();

        if (savedProfile != null) {
            // Assuming navigateToRole accepts a Set<Role>
//            navigateToRole(savedProfile.getRoles());

            // OR, if you want to navigate based on the first role name:
             if (!savedProfile.getRole().isEmpty()) {
                 String roleName = savedProfile.getRole();
                 navigateToRole(roleName);
             }
        } else if (accessToken != null) {
            fetchUserProfile();
        } else {
            switchToLogin();
        }

    }

    private void initialiseViews() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);

        layoutLogin = findViewById(R.id.layout_login);
        layoutCreateUser = findViewById(R.id.layout_create_user);
        layoutCreateUserProfile = findViewById(R.id.layout_create_user_profile);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvCreateUserLink = findViewById(R.id.tv_create_user_link);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvCreateUserLink.setOnClickListener(v -> switchToCreateUser());

        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

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

    // -------------------- LOGIN --------------------
    private void handleLogin() {
        String username = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        Log.d("LOGIN_FLOW", "handleLogin called with username: " + username + ", password: " + password);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            Log.d("LOGIN_FLOW", "Username or password empty");
            return;
        }

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        Log.d("LOGIN_FLOW", "Sending login request: " + request.getUsername() + " / " + request.getPassword());

        apiHandler.login(request, new ApiHandler.ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {
                Log.d("LOGIN_FLOW", "Login success: " + result);
                runOnUiThread(() -> {
                    accessToken = result.getAccessToken();
                    Log.d("LOGIN_FLOW", "Access token received: " + accessToken);
                    prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();

                    if (result.isHasProfile()) {
                        Log.d("LOGIN_FLOW", "User has profile, fetching profile...");
                        fetchUserProfile();
                    } else {
                        Log.d("LOGIN_FLOW", "User has no profile, switching to create profile");
                        switchToCreateUserProfile();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("LOGIN_FLOW", "Login failed: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    // -------------------- FETCH USER PROFILE --------------------
    private void fetchUserProfile() {
        Log.d("LOGIN_FLOW", "fetchUserProfile called with token: " + accessToken);
        apiHandler.getProfile(accessToken, new ApiHandler.ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                Log.d("LOGIN_FLOW", "Profile fetched: " + profile);
                runOnUiThread(() -> {
                    saveUserProfileLocally(profile);
                    if (profile.getRole() != null && !profile.getRole().isEmpty()) {
                        Log.d("LOGIN_FLOW", "Navigating to role: " + profile.getRole());
                        navigateToRole(profile.getRole());
                    } else {
                        Log.d("LOGIN_FLOW", "Profile has no role, switching to create profile");
                        Toast.makeText(MainActivity.this, "No profile found. Please create one.", Toast.LENGTH_SHORT).show();
                        switchToCreateUserProfile();
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("LOGIN_FLOW", "Error fetching profile: " + errorMessage);
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Error fetching profile: " + errorMessage, Toast.LENGTH_SHORT).show();
                    switchToLogin();
                });
            }
        });
    }
    private void saveUserProfileLocally(UserProfile profile) {
        Gson gson = new Gson();
        String profileString = gson.toJson(profile);
        prefs.edit().putString(KEY_USER_PROFILE, profileString).apply();
    }

    private UserProfile getSavedUserProfile() {
        Gson gson = new Gson();
        String profileString = prefs.getString(KEY_USER_PROFILE, null);
        if (profileString != null) {
            return gson.fromJson(profileString, UserProfile.class);
        }
        return null;
    }

    // -------------------- NAVIGATE BY ROLE --------------------
    private void navigateToRole(String role) {
        Log.d("LOGIN_FLOW", "navigateToRole called with: " + role);
        Intent intent;
        switch (role.toUpperCase()) {
            case "CLIENT":
                intent = new Intent(this, ClientActivity.class);
                break;
            case "MECHANIC":
                intent = new Intent(this, MechanicActivity.class);
                break;
            case "CARWASH":
                intent = new Intent(this, CarWashActivity.class);
                break;
            case "ADMIN":
                intent = new Intent(this, AdminActivity.class);
                break;
            default:
                intent = new Intent(this, CarWashActivity.class);
                Log.d("LOGIN_FLOW", "Unknown role, defaulting to CarWashActivity");
        }
        startActivity(intent);
        Log.d("LOGIN_FLOW", "Activity started for role: " + role);
        finish();
    }
}
