package com.example.android_mech_app.Endpints;


import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.Payment;
import com.example.android_mech_app.Models.CallStructures.PaymentRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentAPI {

    // ========== CREATE / PROCESS PAYMENT ==========
    @POST("api/payments/pay")
    Call<ApiResponse<Payment>> processPayment(@Body PaymentRequest request);

    // ========== GET ALL PAYMENTS ==========
    @GET("api/payments")
    Call<ApiResponse<List<Payment>>> getAllPayments();

    // ========== GET PAYMENTS BY CLIENT USERNAME ==========
    @GET("api/payments/client/{username}")
    Call<ApiResponse<List<Payment>>> getPaymentsByClient(
            @Path("username") String username
    );

    // ========== GET PAYMENTS BY MECHANIC ID ==========
    @GET("api/payments/mechanic/{mechanicId}")
    Call<ApiResponse<List<Payment>>> getPaymentsByMechanic(
            @Path("mechanicId") Long mechanicId
    );

    // ========== GET PAYMENTS BY CAR WASH ID ==========
    @GET("api/payments/carWash/{carWashId}")
    Call<ApiResponse<List<Payment>>> getPaymentsByCarWash(
            @Path("carWashId") Long carWashId
    );

    // ========== DELETE PAYMENT BY ID ==========
    @DELETE("api/payments/{paymentId}")
    Call<ApiResponse<Payment>> deletePaymentById(@Path("paymentId") Long paymentId);

    // ========== DELETE ALL PAYMENTS ==========
    @DELETE("api/payments/all")
    Call<ApiResponse<Void>> deleteAllPayments();
}
