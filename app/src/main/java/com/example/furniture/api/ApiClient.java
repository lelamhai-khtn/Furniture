package com.example.furniture.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {
    public static final String BASE_URL = "http://192.168.1.11:7279/";
    public static final String AUTH_LOGIN_ENDPOINT = "api/Auth/login";
    public static final String AUTH_GOOGLE_ENDPOINT = "api/Auth/google";
    public static final String AUTH_REGISTER_ENDPOINT = "api/Auth/register";
    public static final String CATEGORIES_ENDPOINT = "api/Categories";

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final GoogleAuthApi AUTH_API = RETROFIT.create(GoogleAuthApi.class);
    private static final CategoriesApi CATEGORIES_API = RETROFIT.create(CategoriesApi.class);

    private ApiClient() {
        // Utility class.
    }

    public static GoogleAuthApi getAuthApi() {
        return AUTH_API;
    }

    public static CategoriesApi getCategoriesApi() {
        return CATEGORIES_API;
    }
}
