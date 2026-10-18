package com.example.furniture.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.furniture.models.AuthResponse;
import com.google.gson.Gson;

public class AuthSessionStore {
    private static final String PREFERENCES_NAME = "auth_session";
    private static final String SESSION_KEY = "session";

    private final SharedPreferences preferences;
    private final Gson gson = new Gson();

    public AuthSessionStore(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void save(AuthResponse response) {
        if (response == null || response.getUser() == null) {
            throw new IllegalArgumentException("Auth response and user are required");
        }
        preferences.edit()
                .putString(SESSION_KEY, gson.toJson(response))
                .apply();
    }

    @Nullable
    public AuthResponse get() {
        String json = preferences.getString(SESSION_KEY, null);
        return json == null ? null : gson.fromJson(json, AuthResponse.class);
    }

    public boolean isLoggedIn() {
        AuthResponse session = get();
        return session != null
                && session.getAccessToken() != null
                && !session.getAccessToken().isEmpty();
    }

    public void clear() {
        preferences.edit().remove(SESSION_KEY).apply();
    }
}
