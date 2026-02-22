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
import com.example.android_mech_app.api.ApiConstants;
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

    // Session
    private SharedPreferences prefs;
    private String accessToken;

    private static final String TAG = "LOGIN_FLOW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();

        prefs = getSharedPreferences(ApiConstants.PREFS_NAME, Context.MODE_PRIVATE);
        accessToken = prefs.getString(ApiConstants.KEY_TOKEN, null);

        UserProfile savedProfile = getSavedUserProfile();

        // Already logged in
        if (savedProfile != null && savedProfile.getRole() != null) {
            navigateToRole(savedProfile.getRole());
            return;
        }

        // Token exists but no profile cached
        if (accessToken != null) {
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
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
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

    // =====================================================
    // LOGIN
    // =====================================================
    private void handleLogin() {

        String username = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this,
                    "Enter username and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        apiHandler.login(this, request, new ApiHandler.ApiCallback<LoginResponse>() {

            @Override
            public void onSuccess(LoginResponse result) {

                accessToken = result.getAccessToken();

                // Save token using ApiConstants key
                prefs.edit()
                        .putString(ApiConstants.KEY_TOKEN, accessToken)
                        .apply();

                if (result.isHasProfile()) {
                    fetchUserProfile();
                } else {
                    switchToCreateUserProfile();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this,
                        errorMessage,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // =====================================================
    // PROFILE
    // =====================================================
    private void fetchUserProfile() {

        apiHandler.getProfile(this,
                new ApiHandler.ApiCallback<UserProfile>() {

                    @Override
                    public void onSuccess(UserProfile profile) {

                        Utils.saveProfile(MainActivity.this,profile);

                        navigateToRole(profile.getRole());
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                        Toast.makeText(MainActivity.this,
                                errorMessage,
                                Toast.LENGTH_SHORT).show();

                        switchToLogin();
                    }
                });
    }


    private UserProfile getSavedUserProfile() {

        String json = prefs.getString(ApiConstants.KEY_PROFILE_JSON, null);

        if (json == null) return null;

        return new Gson().fromJson(json, UserProfile.class);
    }

    // =====================================================
    // NAVIGATION
    // =====================================================
    private void navigateToRole(String role) {

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
                intent = new Intent(this, ClientActivity.class);
        }

        startActivity(intent);
        finish();
    }
}