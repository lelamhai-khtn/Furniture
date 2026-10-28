package com.example.furniture.models;

import com.example.furniture.api.ApiClient;
import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName(value = "userId", alternate = {"UserId", "id", "Id"})
    private int userId;

    @SerializedName(value = "email", alternate = {"Email"})
    private String email;

    @SerializedName(value = "fullName", alternate = {"FullName", "name", "Name"})
    private String fullName;

    @SerializedName(value = "phone", alternate = {"Phone"})
    private String phone;

    @SerializedName(value = "gender", alternate = {"Gender"})
    private String gender;

    @SerializedName(value = "address", alternate = {"Address"})
    private String address;

    @SerializedName(value = "avatar", alternate = {"Avatar"})
    private String avatar;

    @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
    private String createdAt;

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
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

    public String getAvatar() {
        if (avatar != null && avatar.startsWith("/")) {
            return ApiClient.BASE_URL.replaceAll("/$", "") + avatar;
        }
        return avatar;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
