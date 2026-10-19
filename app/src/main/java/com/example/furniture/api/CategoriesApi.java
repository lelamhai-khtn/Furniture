package com.example.furniture.api;

import com.example.furniture.models.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesApi {
    @GET(ApiClient.CATEGORIES_ENDPOINT)
    Call<List<CategoryModel>> getCategories();
}
