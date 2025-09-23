package com.example.android_mech_app.Models;


import android.os.Build;

import com.example.android_mech_app.Role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserProfile {

    private Long idNumber;

    private String username;

    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    private Set<Role> roles = new HashSet<>();

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Account status
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

    public UserProfile(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.createdAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }

    }

    // Getters and Setters
    public Long  getId() { return idNumber; }
    public void setId(Long id) { this.idNumber = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public boolean isAccountNonExpired() { return accountNonExpired; }
    public void setAccountNonExpired(boolean accountNonExpired) { this.accountNonExpired = accountNonExpired; }

    public boolean isAccountNonLocked() { return accountNonLocked; }
    public void setAccountNonLocked(boolean accountNonLocked) { this.accountNonLocked = accountNonLocked; }

    public boolean isCredentialsNonExpired() { return credentialsNonExpired; }
    public void setCredentialsNonExpired(boolean credentialsNonExpired) { this.credentialsNonExpired = credentialsNonExpired; }
}
