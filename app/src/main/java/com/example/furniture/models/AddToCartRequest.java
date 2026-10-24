package com.example.furniture.models;

public class AddToCartRequest {
    private int variantId;
    private int quantity;

    public AddToCartRequest(int variantId, int quantity) {
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
