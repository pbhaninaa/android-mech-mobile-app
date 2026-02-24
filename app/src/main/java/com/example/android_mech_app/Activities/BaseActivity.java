package com.example.android_mech_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android_mech_app.MainActivity;
import com.example.android_mech_app.Models.CallStructures.PaymentRequest;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

    }

    protected ApiHandler apiHandler;

    protected void initApi() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiHandler = new ApiHandler(apiService);
    }

    public void handleEditProfile(View view) {
        System.out.println("Going to Main Activity to edit the profile");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("editProfile", true);
        startActivity(intent);
    }

    protected void setupLogout(Button btnLogout) {
        btnLogout.setOnClickListener(v -> Utils.logout(this));
    }

    protected void setupToolbarDrawer(
            Toolbar toolbar,
            DrawerLayout drawerLayout,
            String title
    ) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
protected void updateJobStatus(String status){


}
protected void deleteProfile(UserProfile profile){
        apiHandler.deleteProfile(this, profile, new ApiHandler.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected List<User> getAllUsers(){
        List<User> users = Collections.emptyList();
        apiHandler.getAllUsers(this, new ApiHandler.ApiCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
        return  users;
}
protected void getPaymentsByMechanicId(Long id){
        apiHandler.getPaymentsByMechanic(this, id, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected void getPaymentsByCarWashId(Long id){
        apiHandler.getPaymentsByCarWash(this, id, new ApiHandler.ApiCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}protected void deletePaymentByIdId(Long id){
        apiHandler.deletePaymentById(this, id, new ApiHandler.ApiCallback<Payment>() {
            @Override
            public void onSuccess(Payment result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}protected void deletePaymentAllPayments(){
        apiHandler.deleteAllPayments(this, new ApiHandler.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected void createCarWashBooking(CarWashBooking carWashBooking){
        apiHandler.createCarWashBooking(this, carWashBooking, new ApiHandler.ApiCallback<CarWashBooking>() {
            @Override
            public void onSuccess(CarWashBooking result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}

protected void getPaymentHistory(PaymentRequest paymentRequest){
        apiHandler.processPayment(this, paymentRequest, new ApiHandler.ApiCallback<Payment>() {
            @Override
            public void onSuccess(Payment result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected void deleteMerchantRequestByUsername(String username){
        apiHandler.deleteMechanicRequestByUsername(this, username, new ApiHandler.ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected List<MechanicRequest> getMechantRequestById(Long id){
        List<MechanicRequest> mechanicRequestList = Collections.emptyList();
        apiHandler.getMechanicRequestById(this, id, new ApiHandler.ApiCallback<MechanicRequest>() {
            @Override
            public void onSuccess(MechanicRequest result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
                return mechanicRequestList;
}
protected void updateMechanicRequest(MechanicRequest request){
        apiHandler.updateMechanicRequest(this, request, new ApiHandler.ApiCallback<Optional<MechanicRequest>>() {
            @Override
            public void onSuccess(Optional<MechanicRequest> result) {

            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
}
protected List<UserProfile> getProfileByRole(String role){
        apiHandler.getProfilesByRole(this, role, new ApiHandler.ApiCallback<List<UserProfile>>() {
            @Override
            public void onSuccess(List<UserProfile> result) {
            }

            @Override
            public void onFailure(String errorMessage) {

            }
        });
    return Collections.emptyList();
}
    protected void loadProfile(
            TextView txtFullName,
            TextView txtUsername,
            TextView txtEmail,
            TextView txtPhone,
            TextView txtRoles,
            TextView txtCreatedAt,
            TextView txtUpdatedAt
    ) {
        apiHandler.getProfile(this, new ApiHandler.ApiCallback<UserProfile>() {

            @Override
            public void onSuccess(UserProfile profile) {

                txtFullName.setText(profile.getFirstName() + " " + profile.getLastName());
                txtUsername.setText(profile.getUsername());
                txtEmail.setText(profile.getEmail());
                txtPhone.setText(profile.getPhoneNumber());
                txtRoles.setText(profile.getRole());
                txtCreatedAt.setText(profile.getCreatedAt());
                txtUpdatedAt.setText(profile.getUpdatedAt());

                Utils.saveProfile(BaseActivity.this, profile);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(BaseActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                // Fallback to cached profile
                UserProfile cached = Utils.getProfile(BaseActivity.this);
                if (cached != null) {
                    txtFullName.setText(cached.getFirstName() + " " + cached.getLastName());
                    txtUsername.setText(cached.getUsername());
                    txtEmail.setText(cached.getEmail());
                    txtPhone.setText(cached.getPhoneNumber());
                    txtCreatedAt.setText(cached.getCreatedAt());
                    txtUpdatedAt.setText(cached.getUpdatedAt());
                    Toast.makeText(BaseActivity.this, "Loaded cached profile", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    protected void showOnly(View... views){
        for(View v : views){
            v.setVisibility(View.GONE);
        }
    }
}