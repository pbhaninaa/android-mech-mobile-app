package com.example.android_mech_app.Models.CallStructures;

public class PaymentRequest {
    private Double amount;
    private String clientUsername;
    private Long jobId;
    private Long mechanicId;
    private Long carWashId; // <-- new

    // Getters and setters
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getClientUsername() { return clientUsername; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public Long getMechanicId() { return mechanicId; }
    public void setMechanicId(Long mechanicId) { this.mechanicId = mechanicId; }

    public Long getCarWashId() { return carWashId; }
    public void setCarWashId(Long carWashId) { this.carWashId = carWashId; }
}
