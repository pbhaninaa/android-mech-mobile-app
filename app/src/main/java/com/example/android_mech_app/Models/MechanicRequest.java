package com.example.android_mech_app.Models;


import java.time.LocalDate;

public class MechanicRequest {

    private Long id;

    private String username;

    private String description;

    private String location;
    private Long mechanicId;

    private Double latitude;
    private Double longitude;

    private LocalDate date;

    private String status = "pending";

    // ===== Getters & Setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getMechanicId() { return mechanicId; }
    public void setMechanicId(Long mechanicId) { this.mechanicId = mechanicId; }
}
