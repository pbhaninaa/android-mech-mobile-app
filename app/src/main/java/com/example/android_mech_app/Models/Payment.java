package com.example.android_mech_app.Models;

import android.os.Build;

import java.time.LocalDateTime;

public class Payment {

    private Long id;

    private Double amount;
    private String clientUsername;
    private Long jobId;
    private Long mechanicId;
    private Long carWashId;
    private Double platformFee;
    private LocalDateTime paidAt;
    private String jobDescription;

    // Constructors
    public Payment() {}

    public Payment(Double amount, String clientUsername, Long jobId, Long mechanicId, Long carWashId, Double platformFee,String jobDescription) {
        this.amount = amount;
        this.clientUsername = clientUsername;
        this.jobId = jobId;
        this.mechanicId = mechanicId;
        this.carWashId = carWashId;
        this.platformFee = platformFee;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.paidAt = LocalDateTime.now();
        }
        this.jobDescription = jobDescription;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getClientUsername() { return clientUsername; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }

    public Long getMechanicId() { return mechanicId; }
    public void setMechanicId(Long mechanicId) { this.mechanicId = mechanicId; }

    public Long getCarWashId() { return carWashId; }
    public void setCarWashId(Long carWashId) { this.carWashId = carWashId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Double getPlatformFee() { return platformFee; }
    public void setPlatformFee(Double platformFee) { this.platformFee = platformFee; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public String getJobDescription() { return jobDescription; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }
}
