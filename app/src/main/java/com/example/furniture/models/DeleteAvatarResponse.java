package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class DeleteAvatarResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "userId", alternate = {"UserId"})
    private int userId;

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
}
