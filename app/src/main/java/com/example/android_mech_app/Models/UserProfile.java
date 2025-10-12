package com.example.android_mech_app.Models;

import android.os.Build;
import java.time.LocalDateTime;

public class UserProfile {

    private Long idNumber;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String role; // Changed from Set<Role> to single String
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    // Constructors
    public UserProfile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public UserProfile(String username, String password, String email, String role) {
        this.username = username;
        this.email = email;
        this.role = role;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }

    // ----------------------------
    // Getters
    // ----------------------------
    public Long getIdNumber() { return idNumber; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getRole() { return role; } // changed
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isEnabled() { return enabled; }
    public boolean isAccountNonExpired() { return accountNonExpired; }
    public boolean isAccountNonLocked() { return accountNonLocked; }
    public boolean isCredentialsNonExpired() { return credentialsNonExpired; }

    // ----------------------------
    // Setters
    // ----------------------------
    public void setIdNumber(Long idNumber) { this.idNumber = idNumber; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setRole(String role) { this.role = role; } // changed
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setAccountNonExpired(boolean accountNonExpired) { this.accountNonExpired = accountNonExpired; }
    public void setAccountNonLocked(boolean accountNonLocked) { this.accountNonLocked = accountNonLocked; }
    public void setCredentialsNonExpired(boolean credentialsNonExpired) { this.credentialsNonExpired = credentialsNonExpired; }
}
