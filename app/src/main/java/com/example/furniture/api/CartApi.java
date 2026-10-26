package com.example.furniture.api;

import com.example.furniture.models.AddToCartRequest;
import com.example.furniture.models.CartModel;
import com.example.furniture.models.CartResponse;
import com.example.furniture.models.UpdateCartQuantityRequest;
import com.example.furniture.models.UpdateCartQuantityResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {
    @POST(ApiClient.CART_ITEMS_ENDPOINT)
    Call<CartResponse> addToCart(@Body AddToCartRequest request);

    @GET(ApiClient.CART_ENDPOINT)
    Call<CartModel> getCart();

    @PUT("api/Cart/items/{cartItemId}")
    Call<UpdateCartQuantityResponse> updateQuantity(
            @Path("cartItemId") int cartItemId,
            @Body UpdateCartQuantityRequest request
    );

    @DELETE("api/Cart/items/{cartItemId}")
    Call<CartResponse> deleteCartItem(@Path("cartItemId") int cartItemId);
}
