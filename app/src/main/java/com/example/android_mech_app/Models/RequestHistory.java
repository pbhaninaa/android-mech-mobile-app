package com.example.android_mech_app.Models;


import java.time.LocalDateTime;

public class RequestHistory {

    private Long id;

    private String username;

    private String description;
    private String location;

    private LocalDateTime date;

    private String status;

    // --- Constructors ---
    public RequestHistory() {}

    public RequestHistory(String username, String description, String location, LocalDateTime date, String status) {
        this.username = username;
        this.description = description;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

