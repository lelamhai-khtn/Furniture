package com.example.furniture.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class AppPreferences {
    private static final String PREFERENCES_NAME = "sign_in_preferences";
    private static final String REMEMBERED_EMAIL_KEY = "remembered_email";
    private static final String REMEMBERED_PASSWORD_KEY = "remembered_password";

    private final SharedPreferences preferences;

    public AppPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }

        preferences.edit()
                .putString(REMEMBERED_EMAIL_KEY, email.trim())
                .apply();
    }

    @Nullable
    public String getEmail() {
        return preferences.getString(REMEMBERED_EMAIL_KEY, null);
    }

    public void savePassword(String password) {
        if (password == null || password.isEmpty()) {
            return;
        }

        preferences.edit()
                .putString(REMEMBERED_PASSWORD_KEY, password)
                .apply();
    }

    @Nullable
    public String getPassword() {
        return preferences.getString(REMEMBERED_PASSWORD_KEY, null);
    }

    public void clearEmail() {
        preferences.edit()
                .remove(REMEMBERED_EMAIL_KEY)
                .remove(REMEMBERED_PASSWORD_KEY)
                .apply();
    }
}
