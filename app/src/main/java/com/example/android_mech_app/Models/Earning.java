package com.example.android_mech_app.Models;

public class Earning {
    private int id;
    private double amount;
    private String clientUsername;
    private int jobId;
    private Integer mechanicId;  // Nullable
    private int carWashId;
    private double platformFee;
    private String paidAt;       // Could be LocalDateTime
    private String jobDescription;

    public Earning(int id, double amount, String clientUsername, int jobId,
                   Integer mechanicId, int carWashId, double platformFee,
                   String paidAt, String jobDescription) {
        this.id = id;
        this.amount = amount;
        this.clientUsername = clientUsername;
        this.jobId = jobId;
        this.mechanicId = mechanicId;
        this.carWashId = carWashId;
        this.platformFee = platformFee;
        this.paidAt = paidAt;
        this.jobDescription = jobDescription;
    }

    // Getters
    public int getId() { return id; }
    public double getAmount() { return amount; }
    public String getClientUsername() { return clientUsername; }
    public int getJobId() { return jobId; }
    public Integer getMechanicId() { return mechanicId; }
    public int getCarWashId() { return carWashId; }
    public double getPlatformFee() { return platformFee; }
    public String getPaidAt() { return paidAt; }
    public String getJobDescription() { return jobDescription; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setClientUsername(String clientUsername) { this.clientUsername = clientUsername; }
    public void setJobId(int jobId) { this.jobId = jobId; }
    public void setMechanicId(Integer mechanicId) { this.mechanicId = mechanicId; }
    public void setCarWashId(int carWashId) { this.carWashId = carWashId; }
    public void setPlatformFee(double platformFee) { this.platformFee = platformFee; }
    public void setPaidAt(String paidAt) { this.paidAt = paidAt; }
    public void setJobDescription(String jobDescription) { this.jobDescription = jobDescription; }

    @Override
    public String toString() {
        return "Earning{" +
                "id=" + id +
                ", amount=" + amount +
                ", clientUsername='" + clientUsername + '\'' +
                ", jobId=" + jobId +
                ", mechanicId=" + mechanicId +
                ", carWashId=" + carWashId +
                ", platformFee=" + platformFee +
                ", paidAt='" + paidAt + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                '}';
    }
}
