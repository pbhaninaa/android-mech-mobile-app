package com.example.android_mech_app.Endpints;

import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.MechanicRequest;
import com.example.android_mech_app.Models.RequestHistory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestHistoryAPI {

    // ========== GET ALL HISTORIES ==========
    @GET("api/request-history")
    Call<ApiResponse<List<MechanicRequest>>> getAllHistories();

    // ========== GET BY USERNAME ==========
    @GET("api/request-history/user/{username}")
    Call<ApiResponse<List<MechanicRequest>>> getHistoriesByUsername(
            @Path("username") String username
    );

    // ========== GET BY MECHANIC ID ==========
    @GET("api/request-history/mechanic/{mechanicId}")
    Call<ApiResponse<List<MechanicRequest>>> getHistoriesByMechanicId(
            @Path("mechanicId") Long mechanicId
    );

    // ========== CREATE REQUEST HISTORY ==========
    @POST("api/request-history")
    Call<ApiResponse<RequestHistory>> createHistory(
            @Body RequestHistory history
    );

    // ========== UPDATE BY USERNAME ==========
    @PUT("api/request-history/user/{username}")
    Call<ApiResponse<RequestHistory>> updateHistoryByUsername(
            @Path("username") String username,
            @Body RequestHistory history
    );

    // ========== DELETE BY USERNAME ==========
    @DELETE("api/request-history/user/{username}")
    Call<ApiResponse<Void>> deleteHistoriesByUsername(
            @Path("username") String username
    );
}
