package com.example.android_mech_app.Models.CallStructures;

public class ApiResponse<T> {
    private String message;
    private int statusCode;
    private T data;
    private boolean tokenExpired;

    public ApiResponse() {}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public boolean isTokenExpired() { return tokenExpired; }
    public void setTokenExpired(boolean tokenExpired) { this.tokenExpired = tokenExpired; }
}
