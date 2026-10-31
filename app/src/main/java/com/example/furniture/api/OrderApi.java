package com.example.furniture.api;

import com.example.furniture.models.CreateOrderRequest;
import com.example.furniture.models.CreateOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("api/orders")
    Call<CreateOrderResponse> createOrder(@Body CreateOrderRequest request);
}
