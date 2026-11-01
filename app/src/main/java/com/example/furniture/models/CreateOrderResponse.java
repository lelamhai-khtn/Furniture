package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class CreateOrderResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "orderId", alternate = {"OrderId", "id", "Id"})
    private int orderId;

    @SerializedName(value = "orderStatus", alternate = {"OrderStatus", "status", "Status"})
    private String orderStatus;

    @SerializedName(value = "totalAmount", alternate = {"TotalAmount"})
    private double totalAmount;

    @SerializedName(value = "shippingAddress", alternate = {"ShippingAddress"})
    private String shippingAddress;

    @SerializedName(value = "payment", alternate = {"Payment"})
    private PaymentInfo payment;

    @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
    private String createdAt;

    public static class PaymentInfo {
        @SerializedName(value = "paymentId", alternate = {"PaymentId", "id", "Id"})
        private int paymentId;

        @SerializedName(value = "paymentGateway", alternate = {"PaymentGateway"})
        private String paymentGateway;

        @SerializedName(value = "paymentStatus", alternate = {"PaymentStatus"})
        private String paymentStatus;

        public int getPaymentId() {
            return paymentId;
        }

        public String getPaymentGateway() {
            return paymentGateway;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }
    }

    public String getMessage() {
        return message;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public PaymentInfo getPayment() {
        return payment;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
