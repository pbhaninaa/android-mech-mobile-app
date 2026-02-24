package com.example.android_mech_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.example.android_mech_app.Models.User;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.api.ApiClient;
import com.example.android_mech_app.api.ApiConstants;
import com.example.android_mech_app.api.ApiHandler;
import com.example.android_mech_app.api.ApiService;
import com.google.gson.Gson;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // Layouts
    private ScrollView layoutLogin, layoutCreateUser, layoutCreateUserProfile;

    // Login views
    private EditText etLoginEmail, etLoginPassword;
    private Button btnLogin;
    private TextView tvCreateUserLink;

    // Create User views
    private EditText etCreateUserEmail, etCreateUserPassword, etConfirmPassword;
    private Button btnCreateUser;
    private TextView tvLoginLink;

    // Create Profile views
    private EditText etProfileUsername, etProfileEmail;
    private EditText etProfileFirstName, etProfileLastName, etProfilePhone, etProfileAddress;
    private Spinner spinnerRoles;
    private Button btnCreateProfile;

    private ApiHandler apiHandler;

    private SharedPreferences prefs;
    private String accessToken;
    private boolean CreateUser = true;

    private static final String KEY_LOGGED_USERNAME = "LOGGED_USERNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseViews();

        prefs = getSharedPreferences(ApiConstants.PREFS_NAME, Context.MODE_PRIVATE);
        accessToken = prefs.getString(ApiConstants.KEY_TOKEN, null);
        System.out.println(" In Main Activity");

        boolean isEditMode = getIntent().getBooleanExtra("editProfile", false);
        UserProfile savedProfile = getSavedUserProfile();
        if(isEditMode){
            CreateUser =false;

            switchToCreateUserProfile();
        }else{
            if (savedProfile != null && savedProfile.getRole() != null) {
                navigateToRole(savedProfile.getRole());
                return;
            }

            if (accessToken != null) {
                fetchUserProfile();
            } else {
                switchToLogin();
            }
        }


    }

    private void initialiseViews() {

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);

        layoutLogin = findViewById(R.id.layout_login);
        layoutCreateUser = findViewById(R.id.layout_create_user);
        layoutCreateUserProfile = findViewById(R.id.layout_create_user_profile);

        // ---------------- LOGIN ----------------
        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvCreateUserLink = findViewById(R.id.tv_create_user_link);

        btnLogin.setOnClickListener(v -> handleLogin());
        tvCreateUserLink.setOnClickListener(v -> switchToCreateUser());

        // ---------------- CREATE USER ----------------
        etCreateUserEmail = findViewById(R.id.et_create_user_email);
        etCreateUserPassword = findViewById(R.id.et_create_user_password);
        etConfirmPassword = findViewById(R.id.et_pass_confirm);
        btnCreateUser = findViewById(R.id.btn_create_user);
        tvLoginLink = findViewById(R.id.tv_login_link);

        btnCreateUser.setOnClickListener(this::handleCreateUser);
        tvLoginLink.setOnClickListener(v -> switchToLogin());

        // ---------------- CREATE PROFILE ----------------
        etProfileUsername = findViewById(R.id.et_profile_username);
        etProfileEmail = findViewById(R.id.et_profile_email);
        etProfileFirstName = findViewById(R.id.et_profile_first_name);
        etProfileLastName = findViewById(R.id.et_profile_last_name);
        etProfilePhone = findViewById(R.id.et_profile_phone);
        etProfileAddress = findViewById(R.id.et_profile_address);
        spinnerRoles = findViewById(R.id.spinner_roles);
        btnCreateProfile = findViewById(R.id.btn_create_profile);

        btnCreateProfile.setOnClickListener(this::handleCreateProfile);

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"CLIENT", "MECHANIC", "CARWASH","ADMIN"}
        );

        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoles.setAdapter(roleAdapter);

        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // ---------------- Layout Switching ----------------
    private void switchToLogin() {
        layoutLogin.setVisibility(View.VISIBLE);
        layoutCreateUser.setVisibility(View.GONE);
        layoutCreateUserProfile.setVisibility(View.GONE);
    }

    private void switchToCreateUser() {
        layoutLogin.setVisibility(View.GONE);
        layoutCreateUser.setVisibility(View.VISIBLE);
        layoutCreateUserProfile.setVisibility(View.GONE);
    }

    private void switchToCreateUserProfile() {
        layoutLogin.setVisibility(View.GONE);
        layoutCreateUser.setVisibility(View.GONE);
        layoutCreateUserProfile.setVisibility(View.VISIBLE);
        System.out.println(" We have Profile in Session  ");
        boolean isEditMode = getIntent().getBooleanExtra("editProfile", false);
        UserProfile savedProfile = getSavedUserProfile();

        if (savedProfile != null) {
            // Populate fields
            etProfileUsername.setText(savedProfile.getUsername());
            etProfileEmail.setText(savedProfile.getEmail());
            etProfileFirstName.setText(savedProfile.getFirstName());
            etProfileLastName.setText(savedProfile.getLastName());
            etProfilePhone.setText(savedProfile.getPhoneNumber().replace("+27", ""));
            etProfileAddress.setText(savedProfile.getAddress());

            if (isEditMode) {
                // Disable username always
                etProfileUsername.setKeyListener(null);

                // Disable role spinner only if user is NOT admin
                if (!"ADMIN".equalsIgnoreCase(savedProfile.getRole())) {
                    spinnerRoles.setEnabled(false);
                }

                // Optionally, disable email editing
                etProfileEmail.setKeyListener(null);
            } else {
                // Normal flow: only disable username if saved in prefs
                String loggedUsername = prefs.getString(KEY_LOGGED_USERNAME, null);
                if (loggedUsername != null) {
                    etProfileUsername.setText(loggedUsername);
                    etProfileUsername.setKeyListener(null); // Disable editing
                }
            }
        }
    }

    // ---------------- LOGIN ----------------
    private void handleLogin() {

        String username = etLoginEmail.getText().toString().trim();
        String password = etLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);

        apiHandler.login(this, request, new ApiHandler.ApiCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse result) {

                accessToken = result.getAccessToken();

                prefs.edit()
                        .putString(ApiConstants.KEY_TOKEN, accessToken)
                        .putString(KEY_LOGGED_USERNAME, request.getUsername())
                        .apply();

                if (result.isHasProfile()) {
                    fetchUserProfile();
                } else {
                    switchToCreateUserProfile();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfile() {
        apiHandler.getProfile(this, new ApiHandler.ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                Utils.saveProfile(MainActivity.this, profile);
                navigateToRole(profile.getRole());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                switchToLogin();
            }
        });
    }

    private UserProfile getSavedUserProfile() {
        String json = prefs.getString(ApiConstants.KEY_PROFILE_JSON, null);
        if (json == null) return null;
        return new Gson().fromJson(json, UserProfile.class);
    }

    // ---------------- CREATE USER ----------------
    private void handleCreateUser(View v) {

        String email = etCreateUserEmail.getText().toString().trim();
        String password = etCreateUserPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        User request = new User();
        request.setUsername(email);
        request.setPassword(password);

        apiHandler.createUser(this, request, new ApiHandler.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Toast.makeText(MainActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                etLoginEmail.setText(request.getUsername());
                etLoginPassword.setText(request.getPassword());
                switchToLogin();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ---------------- CREATE PROFILE ----------------
    private void handleCreateProfile(View v) {

        String username = etProfileUsername.getText().toString().trim();
        String email = etProfileEmail.getText().toString().trim();
        String firstName = etProfileFirstName.getText().toString().trim();
        String lastName = etProfileLastName.getText().toString().trim();
        String phone ="+27"+ etProfilePhone.getText().toString().trim();
        String address = etProfileAddress.getText().toString().trim();
        String selectedRole = spinnerRoles.getSelectedItem().toString();

        if (TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) ||
                TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(address)) {

            Toast.makeText(this, "Fill all profile fields", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfile request = new UserProfile();
        request.setUsername(username);
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPhoneNumber(phone);
        request.setAddress(address);
        request.setRoles(Collections.singletonList(selectedRole));
if(!CreateUser){
    apiHandler.updateProfile(this, request, new ApiHandler.ApiCallback<UserProfile>() {
        @Override
        public void onSuccess(UserProfile profile) {
            System.out.println(profile.toString());
            Utils.saveProfile(MainActivity.this, profile);
            Toast.makeText(MainActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
            navigateToRole(profile.getRole());
        }

        @Override
        public void onFailure(String errorMessage) {
            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    });
}else{apiHandler.createProfile(this, request, new ApiHandler.ApiCallback<UserProfile>() {
    @Override
    public void onSuccess(UserProfile profile) {
        System.out.println(profile.toString());
        Utils.saveProfile(MainActivity.this, profile);
        Toast.makeText(MainActivity.this, "Profile created successfully", Toast.LENGTH_SHORT).show();
        navigateToRole(profile.getRole());
    }

    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
});}

    }

    // ---------------- NAVIGATION ----------------
    private void navigateToRole(String role) {

        Intent intent;

        switch (role.toUpperCase()) {
            case "CLIENT": intent = new Intent(this, ClientActivity.class); break;
            case "MECHANIC": intent = new Intent(this, MechanicActivity.class); break;
            case "CARWASH": intent = new Intent(this, CarWashActivity.class); break;
            case "ADMIN": intent = new Intent(this, AdminActivity.class); break;
            default: intent = new Intent(this, ClientActivity.class); break;
        }

        startActivity(intent);
        finish();
    }
}