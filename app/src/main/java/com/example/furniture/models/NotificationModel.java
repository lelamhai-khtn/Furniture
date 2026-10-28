package com.example.furniture.models;

public class NotificationModel {
    private int notificationId;
    private String title;
    private String description;
    private String date;
    private String sender;
    private int iconResId;

    public NotificationModel(int notificationId, String title, String description, String date, String sender, int iconResId) {
        this.notificationId = notificationId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.sender = sender;
        this.iconResId = iconResId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public int getIconResId() {
        return iconResId;
    }
}
