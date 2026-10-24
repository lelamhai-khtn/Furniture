package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {
    @SerializedName(value = "cartId", alternate = {"id", "Id", "CartId"})
    private int cartId;

    @SerializedName(value = "userId", alternate = {"UserId"})
    private int userId;

    @SerializedName(value = "updatedAt", alternate = {"UpdatedAt"})
    private String updatedAt;

    @SerializedName(value = "totalItems", alternate = {"TotalItems"})
    private int totalItems;

    @SerializedName(value = "totalAmount", alternate = {"totalPrice", "TotalAmount", "TotalPrice", "total", "Total"})
    private double totalAmount;

    @SerializedName(value = "items", alternate = {"cartItems", "CartItems", "Items"})
    private List<CartItemModel> items = new ArrayList<>();

    public CartModel() {
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItemModel> getItems() {
        return items != null ? items : new ArrayList<>();
    }

    public void setItems(List<CartItemModel> items) {
        this.items = items;
    }
}
