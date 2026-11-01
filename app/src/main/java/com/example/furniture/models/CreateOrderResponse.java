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

        @SerializedName(value = "qrCodeUrl", alternate = {"QrCodeUrl", "qr_code_url"})
        private String qrCodeUrl;

        @SerializedName(value = "bank", alternate = {"Bank"})
        private String bank;

        @SerializedName(value = "accountNumber", alternate = {"AccountNumber", "account_number"})
        private String accountNumber;

        @SerializedName(value = "transferContent", alternate = {"TransferContent", "transfer_content"})
        private String transferContent;

        public int getPaymentId() {
            return paymentId;
        }

        public String getPaymentGateway() {
            return paymentGateway;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public String getBank() {
            return bank;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getTransferContent() {
            return transferContent;
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
