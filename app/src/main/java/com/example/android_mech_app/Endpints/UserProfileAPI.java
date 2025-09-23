package com.example.android_mech_app.Activities;

import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface UserProfileAPI {

    // ========== CREATE PROFILE ==========
    @POST("api/user-profile")
    Call<ApiResponse<UserProfile>> createProfile(
            @Header("Authorization") String token,
            @Body UserProfile profile
    );

    // ========== GET OWN PROFILE ==========
    @GET("api/user-profile")
    Call<ApiResponse<UserProfile>> getProfile(
            @Header("Authorization") String token
    );

    // ========== GET PROFILES BY ROLE ==========
    @GET("api/user-profile/role/{role}")
    Call<ApiResponse<List<UserProfile>>> getProfilesByRole(
            @Header("Authorization") String token,
            @Path("role") String role
    );

    // ========== GET ALL PROFILES (ADMIN) ==========
    @GET("api/user-profile/all")
    Call<ApiResponse<List<UserProfile>>> getAllProfiles(
            @Header("Authorization") String token
    );

    // ========== UPDATE OWN PROFILE ==========
    @PUT("api/user-profile")
    Call<ApiResponse<UserProfile>> updateProfile(
            @Header("Authorization") String token,
            @Body UserProfile profile
    );

    // ========== DELETE OWN PROFILE ==========
    @DELETE("api/user-profile")
    Call<ApiResponse<Void>> deleteProfile(
            @Header("Authorization") String token,
            @Body UserProfile profile
    );
}
