package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class AddToCartRequest {
    @SerializedName(value = "productId", alternate = {"ProductId"})
    private int productId;

    @SerializedName(value = "quantity", alternate = {"Quantity"})
    private int quantity;

    public AddToCartRequest() {
    }

    public AddToCartRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
