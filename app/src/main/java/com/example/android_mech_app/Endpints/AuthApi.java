package com.example.android_mech_app.Endpints;

import com.example.android_mech_app.Models.CallStructures.ApiResponse;
import com.example.android_mech_app.Models.CallStructures.LoginRequest;
import com.example.android_mech_app.Models.CallStructures.LoginResponse;
import com.example.android_mech_app.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthApi {

    @POST("api/users/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("api/users")
    Call<ApiResponse<User>> createUser(@Body User user);

    @GET("api/users")
    Call<ApiResponse<List<User>>> getAllUsers(@Header("Authorization") String token);

    @GET("api/users/{username}")
    Call<ApiResponse<User>> getUser(@Header("Authorization") String token, @Path("username") String username);

    @PUT("api/users/{username}")
    Call<ApiResponse<User>> updateUser(@Header("Authorization") String token,
                                       @Path("username") String username,
                                       @Body User user);

    @DELETE("api/users/{username}")
    Call<ApiResponse<Void>> deleteUser(@Header("Authorization") String token,
                                       @Path("username") String username);
}
