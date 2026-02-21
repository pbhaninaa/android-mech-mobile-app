package com.example.android_mech_app.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mech_app.Adapters.EarningsAdapter;
import com.example.android_mech_app.Adapters.FaqAdapter;
import com.example.android_mech_app.Adapters.JobRequestsAdapter;
import com.example.android_mech_app.Adapters.ManageWashesAdapter;
import com.example.android_mech_app.Models.CarWashBooking;
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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    // ----------------- Views -----------------
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView loggedUserName;

    // Screens
    private ConstraintLayout HomeScreen, ProfileScreen, CarWashBookingsScreen,
            CarWashHistoryScreen,HelpScreen, ServiceHistoryScreen, PaymentsScreen,
            SettingsScreen, ServiceRequestScreen;

    // Profile
    private TextView txtFullName, txtUsername, txtEmail, txtPhone,
            txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    private Button btnEditProfile;

    // Payments
    private RecyclerView recyclerEarnings;

    // Mechanic Requests
    private RadioGroup radioForSelf;
    private RadioButton radioSelf, radioOther;
    private Spinner spinnerDescription;
    private EditText editCustomDescription, editLocation, editDate;
    private Button btnSubmitRequest, btnLogout;

    private ApiHandler apiHandler;

    private String[] jobOptions = {
            "Fix car engine", "Replace brake pads", "Change oil", "Battery replacement",
            "Tire replacement", "AC repair", "Suspension repair", "Other"
    };
    private RecyclerView rvFaqs;
    private LinearLayout llContactDetails;
    private Button btnEmail, btnWhatsApp;
    // ----------------- Lifecycle -----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initialiseViews();
        setupToolbarDrawer();
        setupNavigation();

        loadUserProfile(); // load profile
        showScreen(HomeScreen); // default screen
        setupRequestMechanic();
        loadJobRequests();
    }

    // ----------------- Initialize Views -----------------
    private void initialiseViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        loggedUserName = findViewById(R.id.loggedUsername);

        HomeScreen = findViewById(R.id.dashboard);
        ProfileScreen = findViewById(R.id.profile);
        CarWashBookingsScreen = findViewById(R.id.carWashBookings);
        CarWashHistoryScreen = findViewById(R.id.bookings);
        HelpScreen = findViewById(R.id.helpPage);
        ServiceHistoryScreen = findViewById(R.id.service_history);
        PaymentsScreen = findViewById(R.id.earnings);
        SettingsScreen = findViewById(R.id.settings);
        ServiceRequestScreen = findViewById(R.id.request_service);

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

        radioForSelf = findViewById(R.id.radio_for_self);
        radioSelf = findViewById(R.id.radio_self);
        radioOther = findViewById(R.id.radio_other);
        spinnerDescription = findViewById(R.id.spinner_description);
        editCustomDescription = findViewById(R.id.edit_custom_description);
        editLocation = findViewById(R.id.edit_location);
        editDate = findViewById(R.id.edit_date);
        btnSubmitRequest = findViewById(R.id.btn_submit_request);
        btnLogout = findViewById(R.id.btnLogout);
//        helpPage = findViewById(R.id.helpPage);
        rvFaqs = findViewById(R.id.rvFaqs);
        llContactDetails = findViewById(R.id.llContactDetails);
        btnEmail = findViewById(R.id.btnEmail);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);
        btnLogout.setOnClickListener(v -> Utils.logout(this));



        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);
    }

    // ----------------- Toolbar & Drawer -----------------
    private void setupToolbarDrawer() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Client Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // ----------------- Navigation -----------------
    private void setupNavigation() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showScreen(HomeScreen);
            } else if (id == R.id.nav_profile) {
                showScreen(ProfileScreen);
            } else if (id == R.id.nav_service_request) {
                showScreen(ServiceRequestScreen);
            } else if (id == R.id.nav_book_car_wash) {
                showScreen(CarWashBookingsScreen);
            } else if (id == R.id.nav_service_history) {
                loadJobRequests();
                showScreen(ServiceHistoryScreen);

            } else if (id == R.id.nav_payments) {
                setupPayments();
                showScreen(PaymentsScreen);

            } else if (id == R.id.my_washes) {
                loadManageBookings();
                showScreen(CarWashHistoryScreen);

            } else if (id == R.id.nav_settings) {
                showScreen(SettingsScreen);
            } else if (id==R.id.nav_help) {
                showScreen(HelpScreen);
                loadHelpData();
            } else {
                showScreen(HomeScreen);
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Highlight default item
        navigationView.setCheckedItem(R.id.nav_home);
    }

    // ----------------- Screen Switch -----------------
    private void showScreen(ConstraintLayout screen) {
        HomeScreen.setVisibility(View.GONE);
        ProfileScreen.setVisibility(View.GONE);
        CarWashBookingsScreen.setVisibility(View.GONE);
        CarWashHistoryScreen.setVisibility(View.GONE);
        ServiceHistoryScreen.setVisibility(View.GONE);
        PaymentsScreen.setVisibility(View.GONE);
        SettingsScreen.setVisibility(View.GONE);
        ServiceRequestScreen.setVisibility(View.GONE);
        HelpScreen.setVisibility(View.GONE);

        screen.setVisibility(View.VISIBLE);
    }
//---------------------Load Help Page---------------------
private void loadHelpData() {
    // Sample FAQ data
    List<String[]> faqs = List.of(
            new String[]{"How do I book a service?", "You can book a service by selecting your preferred time and confirming your details."},
            new String[]{"Can I cancel or reschedule?", "Yes, cancellations and rescheduling are allowed up to 24 hours before your appointment."},
            new String[]{"How do payments work?", "Payments can be made securely through the app using your preferred payment method."}
    );

    rvFaqs.setLayoutManager(new LinearLayoutManager(this));
    rvFaqs.setAdapter(new FaqAdapter(faqs));
    // Admin details
    String email = "support@mechanicapp.com";
    String phone = "+27 78 214 1216";
    String whatsapp = "+27782141216";

    llContactDetails.removeAllViews();
    TextView tvEmail = new TextView(this); tvEmail.setText("Email: " + email);
    TextView tvPhone = new TextView(this); tvPhone.setText("Phone: " + phone);
    TextView tvWhatsapp = new TextView(this); tvWhatsapp.setText("WhatsApp: " + whatsapp);
    llContactDetails.addView(tvEmail);
    llContactDetails.addView(tvPhone);
    llContactDetails.addView(tvWhatsapp);

    btnEmail.setOnClickListener(v -> Utils.sendEmail(this, email));
    btnWhatsApp.setOnClickListener(v -> Utils.openWhatsApp(this, whatsapp));
}
    // ----------------- Load Profile -----------------
    private void loadUserProfile() {
        String token = Utils.getAuthToken(this);
        apiHandler.getProfile(token, new ApiHandler.ApiCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                try {
                    String fullName = String.format("%s %s",
                            profile.getLastName() != null ? profile.getLastName() : "",
                            profile.getFirstName() != null ? profile.getFirstName() : "");

                    if (txtFullName != null) txtFullName.setText(fullName);
                    if (txtUsername != null) txtUsername.setText(profile.getUsername() != null ? profile.getUsername() : "");
                    if (txtEmail != null) txtEmail.setText(profile.getEmail() != null ? profile.getEmail() : "");
                    if (txtPhone != null) txtPhone.setText(profile.getPhoneNumber() != null ? profile.getPhoneNumber() : "");

                    if (txtRoles != null) {
                        List<String> roles = profile.getRoles();
                        if (roles != null && !roles.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (String r : roles) sb.append(r).append(", ");
                            sb.setLength(sb.length() - 2);
                            txtRoles.setText(sb.toString());
                        } else {
                            txtRoles.setText("No roles assigned");
                        }
                    }

                    if (txtCreatedAt != null) txtCreatedAt.setText(profile.getCreatedAt() != null ? profile.getCreatedAt() : "");
                    if (txtUpdatedAt != null) txtUpdatedAt.setText(profile.getUpdatedAt() != null ? profile.getUpdatedAt() : "");
                    if (loggedUserName != null) loggedUserName.setText(fullName);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ClientActivity.this, "Error displaying profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, "Failed to load profile: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----------------- Payments -----------------
    private void setupPayments() {
        apiHandler.getPaymentsByClient(Utils.getLoggedInUsername(this), new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                recyclerEarnings.setAdapter(new EarningsAdapter(payments));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, "Failed to load payments: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----------------- Job Requests -----------------
    private void loadJobRequests() {
        apiHandler.getMechanicRequestsByUsername(Utils.getLoggedInUsername(this), new ApiHandler.ApiCallback<List<MechanicRequest>>() {
            @Override
            public void onSuccess(List<MechanicRequest> requests) {
                RecyclerView jobRequestsRecycler = findViewById(R.id.recyclerServiceHistory);
                jobRequestsRecycler.setLayoutManager(new LinearLayoutManager(ClientActivity.this));
                jobRequestsRecycler.setAdapter(new JobRequestsAdapter(requests, Role.CLIENT));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, "Failed to load job history: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----------------- Car Wash Bookings ----------------- //

    private void loadManageBookings() {
        Long userId = Utils.getLoggedInUserId(this);
        apiHandler.getCarWashBookingById(userId, new ApiHandler.ApiCallback<CarWashBooking>() {
            @Override
            public void onSuccess(CarWashBooking booking) {
                System.out.println();
                RecyclerView manageBookings = findViewById(R.id.recyclerBookings);
                manageBookings.setLayoutManager(new LinearLayoutManager(ClientActivity.this));
                manageBookings.setAdapter(new ManageWashesAdapter(List.of(booking)));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, "Failed to load car wash bookings: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ----------------- Mechanic Requests -----------------
    private void setupRequestMechanic() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDescription.setAdapter(adapter);

        spinnerDescription.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                editCustomDescription.setVisibility(jobOptions[position].equals("Other") ? View.VISIBLE : View.GONE);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        radioForSelf.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_self) {
                getCurrentLocation();
                editLocation.setEnabled(false);
            } else {
                editLocation.setText("");
                editLocation.setEnabled(true);
            }
        });

        editDate.setOnClickListener(v -> showDatePicker());
        btnSubmitRequest.setOnClickListener(v -> submitMechanicRequest());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> editDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                editLocation.setText(location.getLatitude() + ", " + location.getLongitude());
            }
        }, null);
    }

    private void submitMechanicRequest() {
        String description = spinnerDescription.getSelectedItem().toString();
        if (description.equals("Other")) description = editCustomDescription.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
        String date = editDate.getText().toString().trim();

        if (description.isEmpty() || location.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        MechanicRequest request = new MechanicRequest();
        request.setUsername(Utils.getLoggedInUsername(this));
        request.setDescription(description);
        request.setLocation(location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) request.setDate(LocalDate.parse(date));

        apiHandler.createMechanicRequest(request, new ApiHandler.ApiCallback<MechanicRequest>() {
            @Override
            public void onSuccess(MechanicRequest result) {
                Toast.makeText(ClientActivity.this, "Mechanic request submitted successfully!", Toast.LENGTH_LONG).show();
                spinnerDescription.setSelection(0);
                editCustomDescription.setText("");
                editCustomDescription.setVisibility(View.GONE);
                editLocation.setText("");
                editDate.setText("");
                loadJobRequests();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, "Failed to submit request: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}