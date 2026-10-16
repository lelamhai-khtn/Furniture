package com.example.furniture.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GoogleAuthApi {
    @Headers("Content-Type: application/json")
    @POST("api/Auth/google")
    Call<Void> login(@Body GoogleLoginRequest request);

    class GoogleLoginRequest {
        private final String idToken;

        public GoogleLoginRequest(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() {
            return idToken;
        }
    }
}
