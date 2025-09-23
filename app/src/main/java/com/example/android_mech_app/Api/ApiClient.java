package com.example.android_mech_app.Api;

import com.example.android_mech_app.Activities.UserProfileAPI;
import com.example.android_mech_app.Endpints.AuthApi;
import com.example.android_mech_app.Endpints.CarWashBookingAPI;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.1.100:8080/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static AuthApi authApi() {
        return getClient().create(AuthApi.class);
    }
    public static UserProfileAPI userProfileApi() {
        return getClient().create(UserProfileAPI.class);
    }
    public static CarWashBookingAPI carWashBookingAPI(){
        return getClient().create(CarWashBookingAPI.class);
    }
}
