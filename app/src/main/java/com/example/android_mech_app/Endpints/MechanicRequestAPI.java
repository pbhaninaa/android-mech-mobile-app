package com.example.android_mech_app.Endpints;

import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.MechanicRequest;

import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MechanicRequestAPI {

    // ========== CREATE MECHANIC REQUEST ==========
    @POST("api/request-mechanic")
    Call<ApiResponse<MechanicRequest>> createRequest(
            @Body MechanicRequest request
    );

    // ========== UPDATE MECHANIC REQUEST ==========
    @PUT("api/request-mechanic")
    Call<ApiResponse<Optional<MechanicRequest>>> updateRequest(
            @Body MechanicRequest request
    );

    // ========== GET REQUESTS BY USERNAME ==========
    @GET("api/request-mechanic/user/{username}")
    Call<ApiResponse<List<MechanicRequest>>> getRequestsByUsername(
            @Path("username") String username
    );

    // ========== GET REQUEST BY ID ==========
    @GET("api/request-mechanic/user/{id}")
    Call<ApiResponse<MechanicRequest>> getRequestById(
            @Path("id") Long id
    );

    // ========== DELETE REQUEST BY USERNAME ==========
    @DELETE("api/request-mechanic/user/{username}")
    Call<ApiResponse<Void>> deleteRequestByUsername(
            @Path("username") String username
    );
}
