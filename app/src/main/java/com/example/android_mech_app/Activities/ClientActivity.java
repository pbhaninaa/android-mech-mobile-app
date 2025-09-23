package com.example.android_mech_app.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.android_mech_app.Adapters.JobRequestsAdapter;
import com.example.android_mech_app.Adapters.ManageWashesAdapter;
import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.R;
import com.example.android_mech_app.Role;
import com.example.android_mech_app.Utils;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.internal.Util;

public class ClientActivity extends AppCompatActivity {

    // Dashboard views
    DrawerLayout drawerLayout;
    ConstraintLayout HomeScreen, ProfileScreen, CarWashBookingsScreen, CarWashHistoryScreen,
            ServiceHistoryScreen, PaymentsScreen, SettingsScreen, ServiceRequestScreen;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView loggedUserName;

    // Profile views
    TextView txtFullName, txtUsername, txtEmail, txtPhone, txtRoles, txtStatus, txtCreatedAt, txtUpdatedAt;
    Button btnEditProfile;

    // Earnings
    RecyclerView recyclerEarnings;
    EarningsAdapter earningsAdapter;

    // Request Mechanic Views
    RadioGroup radioForSelf;
    RadioButton radioSelf, radioOther;
    Spinner spinnerDescription;
    EditText editCustomDescription, editLocation, editDate;
    Button btnSubmitRequest,Logout;


    // Job options
    String[] jobOptions = {
            "Fix car engine",
            "Replace brake pads",
            "Change oil",
            "Battery replacement",
            "Tire replacement",
            "AC repair",
            "Suspension repair",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        // Initialize views
        initialiseViews();

        // Toolbar and Drawer
        setSupportActionBar(toolbar);
        toolbar.setTitle("Car Management App");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation click handling
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) showScreen(HomeScreen);
            else if (id == R.id.nav_profile) showScreen(ProfileScreen);
            else if (id == R.id.nav_service_request) showScreen(ServiceRequestScreen);
            else if (id == R.id.nav_book_car_wash) {
                showScreen(CarWashBookingsScreen);
                handleBooking("TBhani", "SUV", "Exterior Wash", 150.0, "2025-09-25", "Sandton, Johannesburg");
            }
            else if (id == R.id.nav_service_history) {
                showScreen(ServiceHistoryScreen);
                loadJobRequests();
            }
            else if (id == R.id.nav_payments) {
                showScreen(PaymentsScreen);
                setupPayments();
            }
            else if (id == R.id.my_washes) {
                showScreen(CarWashHistoryScreen);
                loadManageBookings();
            }
            else if (id == R.id.nav_settings) showScreen(SettingsScreen);

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Show Home screen by default
        showScreen(HomeScreen);

        // Setup Request Mechanic functionality
        setupRequestMechanic();
    }

    private void initialiseViews() {
        // Dashboard
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        loggedUserName = findViewById(R.id.loggedUsername);

        // Screens
        HomeScreen = findViewById(R.id.dashboard);
        ProfileScreen = findViewById(R.id.profile);
        CarWashBookingsScreen = findViewById(R.id.carWashBookings);
        ServiceHistoryScreen = findViewById(R.id.service_history);
        PaymentsScreen = findViewById(R.id.earnings);
        SettingsScreen = findViewById(R.id.settings);
        CarWashHistoryScreen = findViewById(R.id.bookings);
        ServiceRequestScreen = findViewById(R.id.request_service);

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

        // Request Mechanic
        radioForSelf = findViewById(R.id.radio_for_self);
        radioSelf = findViewById(R.id.radio_self);
        radioOther = findViewById(R.id.radio_other);
        spinnerDescription = findViewById(R.id.spinner_description);
        editCustomDescription = findViewById(R.id.edit_custom_description);
        editLocation = findViewById(R.id.edit_location);
        editDate = findViewById(R.id.edit_date);
        btnSubmitRequest = findViewById(R.id.btn_submit_request);
        Logout=findViewById(R.id.btnLogout);



        Logout.setOnClickListener(v->{
            Utils.logout(this);
        });
    }

    private void showScreen(ConstraintLayout screenToShow) {
        // Hide all screens
        HomeScreen.setVisibility(View.GONE);
        ProfileScreen.setVisibility(View.GONE);
        CarWashBookingsScreen.setVisibility(View.GONE);
        ServiceHistoryScreen.setVisibility(View.GONE);
        CarWashHistoryScreen.setVisibility(View.GONE);
        PaymentsScreen.setVisibility(View.GONE);
        SettingsScreen.setVisibility(View.GONE);
        ServiceRequestScreen.setVisibility(View.GONE);

        // Show selected screen
        screenToShow.setVisibility(View.VISIBLE);
    }

    // ------------------- Car Wash Management -------------------
    private void loadManageWashes() {
        List<CarWashBooking> bookings = new ArrayList<>();
        // Add sample bookings here ...
        RecyclerView manageWashes = findViewById(R.id.recyclerManageWashes);
        manageWashes.setLayoutManager(new LinearLayoutManager(this));
        ManageWashesAdapter adapter = new ManageWashesAdapter(bookings);
        manageWashes.setAdapter(adapter);
    }

    private void loadManageBookings() {
        List<CarWashBooking> bookings = new ArrayList<>();
        // Add sample bookings here ...
        RecyclerView manageBookings = findViewById(R.id.recyclerBookings);
        manageBookings.setLayoutManager(new LinearLayoutManager(this));
        ManageWashesAdapter adapter = new ManageWashesAdapter(bookings);
        manageBookings.setAdapter(adapter);
    }

    private void setupPayments() {
        List<Earning> demoEarnings = new ArrayList<>();
        // Add sample earnings here ...
        EarningsAdapter adapter = new EarningsAdapter(demoEarnings);
        recyclerEarnings.setAdapter(adapter);
    }

    // ------------------- Job Requests -------------------
    private void loadJobRequests() {
        List<MechanicRequest> requests = new ArrayList<>();
        requests.add(new MechanicRequest() {{
            setId(1L);
            setUsername("TBhani");
            setDescription("Replace brake pads");
            setLocation("Buccleuch, Johannesburg Ward 32, Sandton, Gauteng, 2054, South Africa");
            setLatitude(-26.0679378);
            setLongitude(28.1027465);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) setDate(LocalDate.of(2025, 9, 18));
            setStatus("pending");
        }});
        // Add more sample requests as needed ...

        Role role = Role.CLIENT;
        RecyclerView jobRequestsRecycler = findViewById(R.id.recyclerServiceHistory);
        jobRequestsRecycler.setLayoutManager(new LinearLayoutManager(this));
        JobRequestsAdapter adapter = new JobRequestsAdapter(requests, role);
        jobRequestsRecycler.setAdapter(adapter);
    }

    // ------------------- Car Wash Booking Helper -------------------
    private boolean handleBooking(String clientName, String carModel, String serviceType,
                                  double price, String bookingDate, String location) {
        // Validation
        if (clientName == null || clientName.trim().isEmpty()) {
            Toast.makeText(this, "Client name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (carModel == null || carModel.trim().isEmpty()) {
            Toast.makeText(this, "Car model is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (serviceType == null || serviceType.trim().isEmpty()) {
            Toast.makeText(this, "Service type is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price <= 0) {
            Toast.makeText(this, "Price must be greater than 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bookingDate == null || bookingDate.trim().isEmpty()) {
            Toast.makeText(this, "Booking date is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                LocalDate selectedDate = LocalDate.parse(bookingDate);
                if (selectedDate.isBefore(LocalDate.now())) {
                    Toast.makeText(this, "Booking date cannot be in the past", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Invalid booking date format (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (location == null || location.trim().isEmpty()) {
            Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        CarWashBooking booking = new CarWashBooking(
                clientName,
                "AUTO-ID-" + System.currentTimeMillis(),
                carModel,
                location,
                List.of(serviceType),
                price,
                bookingDate,
                "pending"
        );

        Toast.makeText(this, "Booking created for " + carModel + " on " + bookingDate, Toast.LENGTH_LONG).show();
        return true;
    }

    // ------------------- Request Mechanic Functionality -------------------
    private void setupRequestMechanic() {
        // Spinner setup
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDescription.setAdapter(adapter);

        spinnerDescription.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (jobOptions[position].equals("Other")) editCustomDescription.setVisibility(View.VISIBLE);
                else editCustomDescription.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
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
                calendar.get(Calendar.DAY_OF_MONTH)
        );
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
        if (description.equals("Other")) description = editCustomDescription.getText().toString();
        String location = editLocation.getText().toString();
        String date = editDate.getText().toString();

        if (description.isEmpty() || location.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        MechanicRequest request = new MechanicRequest();
        request.setUsername("CurrentUser"); // replace with actual username
        request.setDescription(description);
        request.setLocation(location);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            request.setDate(LocalDate.parse(date));
        request.setStatus("pending");

        Toast.makeText(this, "Mechanic request submitted successfully!", Toast.LENGTH_LONG).show();

        // Reset form
        spinnerDescription.setSelection(0);
        editCustomDescription.setText("");
        editCustomDescription.setVisibility(View.GONE);
        editLocation.setText("");
        editDate.setText("");

        // Reload history
        loadJobRequests();
    }
}
