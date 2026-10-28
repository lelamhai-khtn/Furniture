package com.example.furniture.models;

import com.google.gson.annotations.SerializedName;

public class DeleteNotificationResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "notificationId", alternate = {"NotificationId", "id", "Id"})
    private int notificationId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
}
