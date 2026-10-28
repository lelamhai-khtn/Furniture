package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WishlistResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "totalCount", alternate = {"TotalCount"})
    private int totalCount;

    @SerializedName(value = "products", alternate = {"Products", "items", "Items"})
    private List<WishlistItem> products = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<WishlistItem> getProducts() {
        return products != null ? products : new ArrayList<>();
    }

    public List<ProductModel> getProductModels() {
        List<ProductModel> list = new ArrayList<>();
        if (products != null) {
            for (WishlistItem item : products) {
                if (item != null && item.getProduct() != null) {
                    ProductModel p = item.getProduct();
                    if (p.getId() <= 0 && item.getProductId() > 0) {
                        p.setProductId(item.getProductId());
                    }
                    p.setWishlisted(true);
                    list.add(p);
                }
            }
        }
        return list;
    }

    public static class WishlistItem {
        @SerializedName(value = "wishlistId", alternate = {"id", "Id", "WishlistId"})
        private int wishlistId;

        @SerializedName(value = "productId", alternate = {"ProductId"})
        private int productId;

        @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
        private String createdAt;

        @SerializedName(value = "product", alternate = {"Product"})
        private ProductModel product;

        public int getWishlistId() {
            return wishlistId;
        }

        public int getProductId() {
            return productId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public ProductModel getProduct() {
            return product;
        }
    }
}
