package com.example.furniture.api;

import com.example.furniture.models.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductsApi {
    @GET(ApiClient.PRODUCTS_ENDPOINT)
    Call<List<ProductModel>> getAllProducts();

    @GET(ApiClient.PRODUCTS_TOP_DISCOUNTS_ENDPOINT)
    Call<List<ProductModel>> getTopDiscounts(@Query("limit") int limit);

    @GET(ApiClient.PRODUCTS_ENDPOINT)
    Call<List<ProductModel>> getProductsByCategory(@Query("categoryId") Integer categoryId);

    @GET(ApiClient.PRODUCTS_NEWEST_ENDPOINT)
    Call<List<ProductModel>> getNewestProducts(@Query("limit") int limit);

    @GET(ApiClient.PRODUCTS_ENDPOINT + "/{id}")
    Call<ProductModel> getProductById(@Path("id") int id);
}
