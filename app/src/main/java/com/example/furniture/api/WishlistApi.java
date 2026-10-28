package com.example.furniture.api;

import com.example.furniture.models.CartResponse;
import com.example.furniture.models.WishlistResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WishlistApi {
    @GET("api/Wishlist/products")
    Call<WishlistResponse> getWishlistProducts();

    @POST("api/wishlist/products/{productId}/toggle")
    Call<CartResponse> toggleWishlist(@Path("productId") int productId);
}
