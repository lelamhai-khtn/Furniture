package com.example.furniture.api;

import com.example.furniture.models.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleAuthApi {
    @Headers("Content-Type: application/json")
    @POST(ApiClient.AUTH_GOOGLE_ENDPOINT)
    Call<AuthResponse> loginWithGoogle(@Body GoogleLoginRequest request);

    @Headers("Content-Type: application/json")
    @POST(ApiClient.AUTH_LOGIN_ENDPOINT)
    Call<AuthResponse> login(@Body LoginRequest request);

    @Headers("Content-Type: application/json")
    @POST(ApiClient.AUTH_REGISTER_ENDPOINT)
    Call<AuthResponse> register(@Body RegisterRequest request);

    class GoogleLoginRequest {
        private final String idToken;

        public GoogleLoginRequest(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() {
            return idToken;
        }
    }

    class LoginRequest {
        private final String email;
        private final String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    class RegisterRequest {
        private final String fullName;
        private final String email;
        private final String password;

        public RegisterRequest(String fullName, String email, String password) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
        }

        public String getFullName() {
            return fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
