package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    private int categoryId;
    @SerializedName("categoryName")
    private String name;
    @SerializedName("imageUrl")
    private String urlImage;

    public CategoryModel() {
    }

    public CategoryModel(String name, String urlImage) {
        this.name = name;
        this.urlImage = urlImage;
    }

    public CategoryModel(int categoryId, String name, String urlImage) {
        this.categoryId = categoryId;
        this.name = name;
        this.urlImage = urlImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
