package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("userId")
    private int userId;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }
}
