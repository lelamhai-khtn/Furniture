package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class DistanceResponse {
    @SerializedName("deliveryfee")
    private double deliveryFee;

    @SerializedName("distanceKm")
    private double distanceKm;

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public double getDistanceKm() {
        return distanceKm;
    }
}
