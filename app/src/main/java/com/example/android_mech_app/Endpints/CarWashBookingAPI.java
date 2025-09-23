package com.example.android_mech_app.Endpints;


import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.CarWashBooking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarWashBookingAPI {

    // ========== CREATE BOOKING ==========
    @POST("api/carwash-bookings/create")
    Call<ApiResponse<CarWashBooking>> createBooking(@Body CarWashBooking booking);

    // ========== GET ALL BOOKINGS ==========
    @GET("api/carwash-bookings")
    Call<ApiResponse<List<CarWashBooking>>> getAllBookings();

    // ========== GET BOOKINGS BY CLIENT ==========
    @GET("api/carwash-bookings/client/{username}")
    Call<ApiResponse<List<CarWashBooking>>> getBookingsByClient(@Path("username") String username);

    // ========== GET BOOKING BY ID ==========
    @GET("api/carwash-bookings/{id}")
    Call<ApiResponse<CarWashBooking>> getBookingById(@Path("id") Long id);

    // ========== UPDATE BOOKING ==========
    @PUT("api/carwash-bookings/update/{id}")
    Call<ApiResponse<CarWashBooking>> updateBooking(
            @Path("id") Long id,
            @Body CarWashBooking booking
    );

    // ========== DELETE BOOKING ==========
    @DELETE("api/carwash-bookings/delete/{id}")
    Call<ApiResponse<Void>> deleteBooking(@Path("id") Long id);
}
