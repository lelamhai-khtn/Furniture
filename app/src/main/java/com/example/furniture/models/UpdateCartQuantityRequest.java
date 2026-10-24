package com.example.furniture.models;

public class UpdateCartQuantityRequest {
    private int quantity;

    public UpdateCartQuantityRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
