package com.example.android_mech_app;

import static com.example.android_mech_app.api.ApiConstants.KEY_PROFILE_JSON;
import static com.example.android_mech_app.api.ApiConstants.KEY_TOKEN;
import static com.example.android_mech_app.api.ApiConstants.KEY_USERNAME;
import static com.example.android_mech_app.api.ApiConstants.KEY_USER_ID;
import static com.example.android_mech_app.api.ApiConstants.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.example.android_mech_app.Activities.ClientActivity;
import com.example.android_mech_app.Models.UserProfile;
import com.example.android_mech_app.api.ApiClient;
import com.google.gson.Gson;

public class Utils {




    /*
     * -------------------------------
     * TOKEN METHODS
     * -------------------------------
     */

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        prefs.edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public static String getAuthToken(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return prefs.getString(KEY_TOKEN, null);
    }

    /*
     * -------------------------------
     * PROFILE METHODS
     * -------------------------------
     */

    public static void saveProfile(Context context, UserProfile profile) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Gson gson = new Gson();

        prefs.edit()
                .putString(KEY_PROFILE_JSON, gson.toJson(profile))
                .putString(KEY_USERNAME, profile.getUsername())
                .putLong(KEY_USER_ID, profile.getId())
                .apply();
    }

    public static UserProfile getProfile(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(KEY_PROFILE_JSON, null);

        if (json == null) return null;

        return new Gson().fromJson(json, UserProfile.class);
    }

    public static String getLoggedInUsername(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return prefs.getString(KEY_USERNAME, null);
    }

    public static Long getLoggedInUserId(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return prefs.getLong(KEY_USER_ID, -1);
    }

    /*
     * -------------------------------
     * SESSION METHODS
     * -------------------------------
     */

    public static boolean isLoggedIn(Context context) {
        return getAuthToken(context) != null;
    }

    public static void logout(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Clear everything
        prefs.edit().clear().commit();

        // Reset retrofit client (IMPORTANT)
        ApiClient.resetClient();

        // Redirect to MainActivity
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        context.startActivity(intent);
    }

    /*
     * -------------------------------
     * EXTERNAL ACTIONS
     * -------------------------------
     */

    public static void sendEmail(ClientActivity activity, String email) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));

        intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, I need help with...");

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    public static void openWhatsApp(ClientActivity activity, String whatsapp) {

        String url = "https://wa.me/" + whatsapp.replaceAll("\\D", "");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
}