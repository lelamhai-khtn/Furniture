package com.example.furniture.models;

public class CategoryModel {
    private String name;
    private String urlImage;

    public CategoryModel(String name, String urlImage) {
        this.name = name;
        this.urlImage = urlImage;
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
