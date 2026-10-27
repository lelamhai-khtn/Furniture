package com.example.furniture.api;

import android.content.Context;

import com.example.furniture.models.AuthResponse;
import com.example.furniture.utils.AuthSessionStore;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {
    public static final String BASE_URL = "http://192.168.1.10:7279/";
    public static final String AUTH_LOGIN_ENDPOINT = "api/Auth/login";
    public static final String AUTH_GOOGLE_ENDPOINT = "api/Auth/google";
    public static final String AUTH_REGISTER_ENDPOINT = "api/Auth/register";
    public static final String CATEGORIES_ENDPOINT = "api/Categories";
    public static final String PRODUCTS_ENDPOINT = "api/Products";
    public static final String PRODUCTS_TOP_DISCOUNTS_ENDPOINT = "api/Products/top-discounts";
    public static final String PRODUCTS_NEWEST_ENDPOINT = "api/Products/newest";
    public static final String CART_ITEMS_ENDPOINT = "api/Cart/items";
    public static final String CART_ENDPOINT = "api/Cart";

    private static Context appContext;

    public static void init(Context context) {
        if (context != null) {
            appContext = context.getApplicationContext();
        }
    }

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                String path = originalRequest.url().encodedPath();

                if (isAuthEndpoint(path)) {
                    return chain.proceed(originalRequest);
                }

                String token = getToken();
                if (token != null && !token.isEmpty()) {
                    Request authenticatedRequest = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(authenticatedRequest);
                }

                return chain.proceed(originalRequest);
            })
            .build();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OK_HTTP_CLIENT)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final GoogleAuthApi AUTH_API = RETROFIT.create(GoogleAuthApi.class);
    private static final CategoriesApi CATEGORIES_API = RETROFIT.create(CategoriesApi.class);
    private static final ProductsApi PRODUCTS_API = RETROFIT.create(ProductsApi.class);
    private static final CartApi CART_API = RETROFIT.create(CartApi.class);
    private static final WishlistApi WISHLIST_API = RETROFIT.create(WishlistApi.class);

    private ApiClient() {
        // Utility class.
    }

    private static boolean isAuthEndpoint(String path) {
        return path.endsWith(AUTH_LOGIN_ENDPOINT) ||
                path.endsWith(AUTH_GOOGLE_ENDPOINT) ||
                path.endsWith(AUTH_REGISTER_ENDPOINT);
    }

    private static String getToken() {
        if (appContext == null) {
            return null;
        }
        AuthSessionStore sessionStore = new AuthSessionStore(appContext);
        AuthResponse session = sessionStore.get();
        if (session != null && session.getAccessToken() != null) {
            return session.getAccessToken();
        }
        return null;
    }

    public static GoogleAuthApi getAuthApi() {
        return AUTH_API;
    }

    public static CategoriesApi getCategoriesApi() {
        return CATEGORIES_API;
    }

    public static ProductsApi getProductsApi() {
        return PRODUCTS_API;
    }

    public static CartApi getCartApi() {
        return CART_API;
    }

    public static WishlistApi getWishlistApi() {
        return WISHLIST_API;
    }
}
