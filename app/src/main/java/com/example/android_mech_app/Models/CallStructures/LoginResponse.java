package com.example.android_mech_app.Models.CallStructures;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private boolean hasProfile;

    public LoginResponse(String accessToken, String refreshToken, boolean hasProfile) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.hasProfile = hasProfile;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public boolean isHasProfile() { return hasProfile; }
    public void setHasProfile(boolean hasProfile) { this.hasProfile = hasProfile; }
}
