package com.example.furniture.api;

import com.example.furniture.models.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductsApi {
    @GET(ApiClient.PRODUCTS_TOP_DISCOUNTS_ENDPOINT)
    Call<List<ProductModel>> getTopDiscounts(@Query("limit") int limit);

    @GET(ApiClient.PRODUCTS_ENDPOINT)
    Call<List<ProductModel>> getProductsByCategory(@Query("categoryId") Integer categoryId);
}
