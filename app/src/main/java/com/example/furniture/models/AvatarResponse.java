package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class AvatarResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "userId", alternate = {"UserId"})
    private int userId;

    @SerializedName(value = "avatar", alternate = {"Avatar"})
    private String avatar;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
