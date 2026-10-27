package com.example.furniture.api;

import com.example.furniture.models.CartResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WishlistApi {
    @POST("api/wishlist/products/{productId}/toggle")
    Call<CartResponse> toggleWishlist(@Path("productId") int productId);
}
