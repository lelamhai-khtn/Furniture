package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {
    @SerializedName("fullName")
    private String fullName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("gender")
    private String gender;

    @SerializedName("address")
    private String address;

    public UpdateProfileRequest(String fullName, String phone, String gender, String address) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
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
