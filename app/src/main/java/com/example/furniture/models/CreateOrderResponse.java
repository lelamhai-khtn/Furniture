package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "orderId", alternate = {"OrderId", "id", "Id"})
    private int orderId;

    @SerializedName(value = "orderStatus", alternate = {"OrderStatus", "status", "Status"})
    private String orderStatus;

    @SerializedName(value = "productTotal", alternate = {"ProductTotal"})
    private double productTotal;

    @SerializedName(value = "deliveryFee", alternate = {"DeliveryFee"})
    private double deliveryFee;

    @SerializedName(value = "distanceKm", alternate = {"DistanceKm"})
    private double distanceKm;

    @SerializedName(value = "totalAmount", alternate = {"TotalAmount"})
    private double totalAmount;

    @SerializedName(value = "shippingAddress", alternate = {"ShippingAddress"})
    private String shippingAddress;

    @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
    private String createdAt;

    public String getMessage() {
        return message;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public double getProductTotal() {
        return productTotal;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
