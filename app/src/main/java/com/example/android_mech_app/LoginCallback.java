package com.example.android_mech_app;

public interface LoginCallback {
    void onSuccess(String message);
    void onError(String error);
}
