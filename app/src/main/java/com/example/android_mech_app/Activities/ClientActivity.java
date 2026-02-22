package com.example.android_mech_app.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Geocoder;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    // ---------------- Views ----------------
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ConstraintLayout HomeScreen, ProfileScreen, CarWashBookingsScreen,
            CarWashHistoryScreen, HelpScreen, ServiceHistoryScreen,
            PaymentsScreen, SettingsScreen, ServiceRequestScreen;

    private TextView txtFullName, txtUsername, txtEmail, txtPhone,
            txtRoles, txtCreatedAt, txtUpdatedAt, loggedUserName;

    private RecyclerView recyclerEarnings;

    private RadioGroup radioForSelf;
    private RadioButton radioSelf, radioOther;
    private Spinner spinnerDescription;
    private EditText editCustomDescription, editLocation, editDate;
    private Button btnSubmitRequest, btnLogout;

    private RecyclerView rvFaqs;
    private LinearLayout llContactDetails;
    private Button btnEmail, btnWhatsApp;

    private ApiHandler apiHandler;

    private String[] jobOptions = {
            "Fix car engine", "Replace brake pads", "Change oil",
            "Battery replacement", "Tire replacement", "AC repair",
            "Suspension repair", "Other"
    };

    // ---------------- Lifecycle ----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        initialiseViews();
        setupToolbarDrawer();
        setupNavigation();

        setupRequestMechanic();
        openHome();
        loadUserProfile();
    }

    // ---------------- Initialize Views ----------------
    private void initialiseViews() {
        editLocation = findViewById(R.id.edit_location);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

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
        txtCreatedAt = findViewById(R.id.txtCreatedAt);
        txtUpdatedAt = findViewById(R.id.txtUpdatedAt);
        loggedUserName = findViewById(R.id.loggedUsername);

        recyclerEarnings = findViewById(R.id.recyclerEarnings);
        recyclerEarnings.setLayoutManager(new LinearLayoutManager(this));
        editLocation = findViewById(R.id.edit_location);
        radioForSelf = findViewById(R.id.radio_for_self);
        radioSelf = findViewById(R.id.radio_self);
        radioOther = findViewById(R.id.radio_other);

        spinnerDescription = findViewById(R.id.spinner_description);
        editCustomDescription = findViewById(R.id.edit_custom_description);

        editDate = findViewById(R.id.edit_date);
        btnSubmitRequest = findViewById(R.id.btn_submit_request);
        btnLogout = findViewById(R.id.btnLogout);

        rvFaqs = findViewById(R.id.rvFaqs);
        llContactDetails = findViewById(R.id.llContactDetails);
        btnEmail = findViewById(R.id.btnEmail);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);

        btnLogout.setOnClickListener(v -> Utils.logout(this));

        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);
    }

    // ---------------- Toolbar ----------------
    private void setupToolbarDrawer() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Client Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // ---------------- Navigation ----------------
    private void setupNavigation() {

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) openHome();
            else if (id == R.id.nav_profile) openProfile();
            else if (id == R.id.nav_service_request) openServiceRequest();
            else if (id == R.id.nav_book_car_wash) openCarWashBooking();
            else if (id == R.id.nav_service_history) openServiceHistory();
            else if (id == R.id.nav_payments) openPayments();
            else if (id == R.id.my_washes) openMyWashes();
            else if (id == R.id.nav_settings) openSettings();
            else if (id == R.id.nav_help) openHelp();

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.setCheckedItem(R.id.nav_home);
    }

    // ---------------- Screen Switch ----------------
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

    // ---------------- Screen Methods ----------------

    private void openHome() {
        showScreen(HomeScreen);
    }

    private void openProfile() {
        showScreen(ProfileScreen);
        loadUserProfile();
    }

    private void openServiceRequest() {
        showScreen(ServiceRequestScreen);
        radioSelf.setChecked(true);
        getCurrentLocationWithName();
    }

    private void openCarWashBooking() {
        showScreen(CarWashBookingsScreen);
        getCurrentLocationWithName();
    }

    private void openServiceHistory() {
        showScreen(ServiceHistoryScreen);
        loadJobRequests();
    }

    private void openPayments() {
        showScreen(PaymentsScreen);
        setupPayments();
    }

    private void openMyWashes() {
        showScreen(CarWashHistoryScreen);
        loadBookings();
    }

    private void openSettings() {
        showScreen(SettingsScreen);
    }

    private void openHelp() {
        showScreen(HelpScreen);
        loadHelpData();
    }

    // ---------------- Help Page ----------------
    private void loadHelpData() {

        List<String[]> faqs = List.of(
                new String[]{"How do I book a service?", "Select date and confirm."},
                new String[]{"Can I cancel?", "Yes, before 24 hours."},
                new String[]{"How do payments work?", "Secure in-app payment."}
        );

        rvFaqs.setLayoutManager(new LinearLayoutManager(this));
        rvFaqs.setAdapter(new FaqAdapter(faqs));

        String email = "support@mechanicapp.com";
        String phone = "+27 78 214 1216";
        String whatsapp = "+27782141216";

        llContactDetails.removeAllViews();

        TextView tvEmail = new TextView(this);
        tvEmail.setText("Email: " + email);

        TextView tvPhone = new TextView(this);
        tvPhone.setText("Phone: " + phone);

        TextView tvWhatsapp = new TextView(this);
        tvWhatsapp.setText("WhatsApp: " + whatsapp);

        llContactDetails.addView(tvEmail);
        llContactDetails.addView(tvPhone);
        llContactDetails.addView(tvWhatsapp);

        btnEmail.setOnClickListener(v -> Utils.sendEmail(this, email));
        btnWhatsApp.setOnClickListener(v -> Utils.openWhatsApp(this, whatsapp));
    }

    // ---------------- Profile ----------------
    private void loadUserProfile() {
        String token = Utils.getAuthToken(this);

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

                // Save profile to session
                Utils.saveProfile(ClientActivity.this, profile);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                // Fallback to cached profile
                UserProfile cached = Utils.getProfile(ClientActivity.this);
                if (cached != null) {
                    txtFullName.setText(cached.getFirstName() + " " + cached.getLastName());
                    txtUsername.setText(cached.getUsername());
                    txtEmail.setText(cached.getEmail());
                    txtPhone.setText(cached.getPhoneNumber());
                    txtCreatedAt.setText(cached.getCreatedAt());
                    txtUpdatedAt.setText(cached.getUpdatedAt());
                    Toast.makeText(ClientActivity.this, "Loaded cached profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // ---------------- Payments ----------------
    private void setupPayments() {
        String username = Utils.getLoggedInUsername(this);

        apiHandler.getPaymentsByClient(this,username, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                recyclerEarnings.setAdapter(new EarningsAdapter(payments));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ---------------- Service History ----------------
    private void loadJobRequests() {
        String username = Utils.getLoggedInUsername(this);
        apiHandler.getMechanicRequestsByUsername(this,
                username,
                new ApiHandler.ApiCallback<List<MechanicRequest>>() {

                    @Override
                    public void onSuccess(List<MechanicRequest> requests) {

                        RecyclerView recycler = findViewById(R.id.recyclerServiceHistory);
                        recycler.setLayoutManager(new LinearLayoutManager(ClientActivity.this));
                        recycler.setAdapter(new JobRequestsAdapter(requests, Role.CLIENT));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ---------------- Car Wash History ----------------
    private void loadBookings() {
        Long userId = Utils.getLoggedInUserId(this);

        apiHandler.getCarWashBookingById(this,userId, new ApiHandler.ApiCallback<CarWashBooking>() {
            @Override
            public void onSuccess(CarWashBooking booking) {
                System.out.println("===============================================================================================");
                System.out.println(booking);
                RecyclerView recycler = findViewById(R.id.recyclerBookings);
                recycler.setLayoutManager(new LinearLayoutManager(ClientActivity.this));
                recycler.setAdapter(new ManageWashesAdapter(List.of(booking)));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ---------------- Request Mechanic ----------------
    private void setupRequestMechanic() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobOptions);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDescription.setAdapter(adapter);

        // Show custom description if "Other"
        spinnerDescription.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (jobOptions[position].equals("Other")) {
                    editCustomDescription.setVisibility(View.VISIBLE);
                } else {
                    editCustomDescription.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // ⭐ IMPORTANT PART (BOOKING TYPE)
        radioForSelf.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radio_self) {

                // Disable manual typing
                editLocation.setEnabled(false);

                // Fetch current GPS location
                getCurrentLocationWithName();

            } else if (checkedId == R.id.radio_other) {

                // Enable manual typing
                editLocation.setEnabled(true);

                // Clear auto location
                editLocation.setText("");
            }
        });

        editDate.setOnClickListener(v -> showDatePicker());
        btnSubmitRequest.setOnClickListener(v -> submitMechanicRequest());
    }

    private void showDatePicker() {

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, day) ->
                                editDate.setText(year + "-" + (month + 1) + "-" + day),
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

//    private void getCurrentLocation() {
//
//        editLocation.setText("Fetching location...");
//
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    100);
//            return;
//        }
//
//        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        lm.requestSingleUpdate(
//                LocationManager.GPS_PROVIDER,
//                new LocationListener() {
//                    @Override
//                    public void onLocationChanged(@NonNull Location location) {
//
//                        editLocation.setText(
//                                location.getLatitude() + ", " + location.getLongitude());
//                    }
//                },
//                null);
//    }
private void getCurrentLocationWithName() {

    editLocation.setText("Fetching location...");

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        return;
    }

    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

    lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            // Default: coordinates
            String locationName = latitude + ", " + longitude;

            // Reverse geocode in background thread
            new Thread(() -> {
                Geocoder geocoder = new Geocoder(ClientActivity.this);
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        android.location.Address address = addresses.get(0);

                        String name;
                        if (address.getMaxAddressLineIndex() >= 0) {
                            name = address.getAddressLine(0); // full readable address
                        } else {
                            name = latitude + ", " + longitude;
                        }

                        final String finalName = name;

                        // Update UI on main thread
                        runOnUiThread(() -> editLocation.setText(finalName));
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // fallback if geocoding failed
                runOnUiThread(() -> editLocation.setText(locationName));

            }).start();
        }

        @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override public void onProviderEnabled(@NonNull String provider) {}
        @Override public void onProviderDisabled(@NonNull String provider) {}
    }, null);
}

    private void submitMechanicRequest() {

        String description = spinnerDescription.getSelectedItem().toString();
        String location = editLocation.getText().toString();
        String date = editDate.getText().toString();

        if (description.isEmpty() || location.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        MechanicRequest request = new MechanicRequest();
        request.setUsername(Utils.getLoggedInUsername(this));
        request.setDescription(description);
        request.setLocation(location);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            request.setDate(LocalDate.parse(date));

        apiHandler.createMechanicRequest(this,request,
                new ApiHandler.ApiCallback<MechanicRequest>() {

                    @Override
                    public void onSuccess(MechanicRequest result) {
                        Toast.makeText(ClientActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                        loadJobRequests();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}