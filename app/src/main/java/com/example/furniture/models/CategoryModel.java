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

    public int getCategoryId() {
        return categoryId;
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
