package com.example.android_mech_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.example.android_mech_app.Activities.ClientActivity;
import com.example.android_mech_app.Activities.MechanicActivity;
import com.example.android_mech_app.Models.CarWashBooking;
import com.example.android_mech_app.Models.Earning;
import com.example.android_mech_app.Models.MechanicRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String PREFS_NAME = "MyAppPrefs";
    public static void logout(Context context) {
        // Clear all shared preferences
        SharedPreferences prefs = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Redirect to MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears back stack
        context.startActivity(intent);
    }

    public static List<Earning> parsePayments(JsonObject result) {
        List<Earning> earningsList = new ArrayList<>();

        // Assuming your API response contains a "payments" array
        // Example: { "payments": [ { "id": 1, "amount": 100.0, ... }, ... ] }
        if (result.has("payments")) {
            JsonArray paymentsArray = result.getAsJsonArray("payments");

            for (JsonElement element : paymentsArray) {
                JsonObject paymentObj = element.getAsJsonObject();

                Earning earning = new Earning(
                        paymentObj.has("id") ? paymentObj.get("id").getAsInt() : 0,
                        paymentObj.has("amount") ? paymentObj.get("amount").getAsDouble() : 0.0,
                        paymentObj.has("client_username") ? paymentObj.get("client_username").getAsString() : "",
                        paymentObj.has("job_id") ? paymentObj.get("job_id").getAsInt() : 0,
                        paymentObj.has("mechanic_id") && !paymentObj.get("mechanic_id").isJsonNull() ? paymentObj.get("mechanic_id").getAsInt() : null,
                        paymentObj.has("car_wash_id") ? paymentObj.get("car_wash_id").getAsInt() : 0,
                        paymentObj.has("platform_fee") ? paymentObj.get("platform_fee").getAsDouble() : 0.0,
                        paymentObj.has("paid_at") ? paymentObj.get("paid_at").getAsString() : "",
                        paymentObj.has("job_description") ? paymentObj.get("job_description").getAsString() : ""
                );

                earningsList.add(earning);
            }
        }

        return earningsList;
    }

    public static List<MechanicRequest> parseJobRequests(JsonObject result) {
        List<MechanicRequest> requestsList = new ArrayList<>();

        // Assuming the API response contains a "requests" array
        // Example: { "requests": [ { "id": 1, "username": "...", ... }, ... ] }
        if (result.has("requests")) {
            JsonArray requestsArray = result.getAsJsonArray("requests");

            for (JsonElement element : requestsArray) {
                JsonObject requestObj = element.getAsJsonObject();

                MechanicRequest request = new MechanicRequest();

                request.setId(requestObj.has("id") ? requestObj.get("id").getAsLong() : 0L);
                request.setUsername(requestObj.has("username") ? requestObj.get("username").getAsString() : "");
                request.setDescription(requestObj.has("job_description") ? requestObj.get("job_description").getAsString() : "");
                request.setLocation(requestObj.has("location") ? requestObj.get("location").getAsString() : "");

                // Latitude & Longitude
                request.setLatitude(requestObj.has("latitude") ? requestObj.get("latitude").getAsDouble() : 0.0);
                request.setLongitude(requestObj.has("longitude") ? requestObj.get("longitude").getAsDouble() : 0.0);

                // Date (as LocalDate)
                if (requestObj.has("date") && !requestObj.get("date").isJsonNull()) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            request.setDate(LocalDate.parse(requestObj.get("date").getAsString()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setDate(null);
                    }
                }

                request.setStatus(requestObj.has("status") ? requestObj.get("status").getAsString() : "");

                requestsList.add(request);
            }
        }

        return requestsList;
    }

    public static List<CarWashBooking> parseCarWashBookings(JsonObject result) {
        List<CarWashBooking> bookings = new ArrayList<>();

        if (result.has("bookings")) {
            JsonArray array = result.getAsJsonArray("bookings");
            for (JsonElement e : array) {
                JsonObject obj = e.getAsJsonObject();
                CarWashBooking booking = new CarWashBooking(
                        obj.has("clientUsername") ? obj.get("clientUsername").getAsString() : "",
                        obj.has("vehiclePlate") ? obj.get("vehiclePlate").getAsString() : "",
                        obj.has("vehicleType") ? obj.get("vehicleType").getAsString() : "",
                        obj.has("description") ? obj.get("description").getAsString() : "",
                        obj.has("services") ? new Gson().fromJson(obj.get("services"), List.class) : new ArrayList<>(),
                        obj.has("amount") ? obj.get("amount").getAsDouble() : 0.0,
                        obj.has("date") ? obj.get("date").getAsString() : "",
                        obj.has("id") ? obj.get("id").getAsString() : ""
                );
                booking.setStatus(obj.has("status") ? obj.get("status").getAsString() : "Pending");
                bookings.add(booking);
            }
        }

        return bookings;
    }

    // -------------------- GET LOGGED IN INFO --------------------
    public static String getLoggedInUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("PROFILE_USERNAME", null);
    }

    public static Long getLoggedInUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Assuming the user ID was stored as long during login/profile fetch
        return prefs.getLong("PROFILE_USER_ID", -1);
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("ACCESS_TOKEN", null);
    }

    public static void sendEmail(ClientActivity clientActivity, String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, I need help with...");

        if (intent.resolveActivity(clientActivity.getPackageManager()) != null) {
            clientActivity.startActivity(intent);
        } else {
            Toast.makeText(clientActivity, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
    public static void openWhatsApp(ClientActivity clientActivity, String whatsapp) {
        String url = "https://wa.me/" + whatsapp.replaceAll("\\D", ""); // remove non-digits
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        if (intent.resolveActivity(clientActivity.getPackageManager()) != null) {
            clientActivity.startActivity(intent);
        } else {
            Toast.makeText(clientActivity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}
