package com.example.furniture.api;

import com.example.furniture.models.DistanceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DistanceApi {
    @GET("api/Distance")
    Call<DistanceResponse> getDistance(
            @Query("destLat") double destLat,
            @Query("destLng") double destLng
    );
}
