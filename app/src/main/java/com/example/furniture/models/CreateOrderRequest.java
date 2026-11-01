package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateOrderRequest {
    @SerializedName("cartIds")
    private List<Integer> cartIds;

    @SerializedName("shippingAddress")
    private String shippingAddress;

    @SerializedName("paymentGateway")
    private String paymentGateway;

    @SerializedName("deliveryDate")
    private String deliveryDate;

    @SerializedName("deliveryTimeSlot")
    private String deliveryTimeSlot;

    public CreateOrderRequest(List<Integer> cartIds, String shippingAddress, String paymentGateway, String deliveryDate, String deliveryTimeSlot) {
        this.cartIds = cartIds;
        this.shippingAddress = shippingAddress;
        this.paymentGateway = paymentGateway;
        this.deliveryDate = deliveryDate;
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }
}
