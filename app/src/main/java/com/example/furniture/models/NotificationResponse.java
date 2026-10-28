package com.example.furniture.models;

import com.example.furniture.R;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NotificationResponse {
    @SerializedName(value = "message", alternate = {"Message"})
    private String message;

    @SerializedName(value = "totalCount", alternate = {"TotalCount"})
    private int totalCount;

    @SerializedName(value = "count", alternate = {"Count"})
    private int count;

    @SerializedName(value = "notifications", alternate = {"Notifications", "items", "Items"})
    private List<NotificationItem> notifications = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getCount() {
        return count;
    }

    public List<NotificationItem> getNotifications() {
        return notifications != null ? notifications : new ArrayList<>();
    }

    public List<NotificationModel> getNotificationModels() {
        List<NotificationModel> list = new ArrayList<>();
        if (notifications != null) {
            for (NotificationItem item : notifications) {
                if (item != null) {
                    String dateStr = item.getCreatedAt() != null ? item.getCreatedAt() : "";
                    if (dateStr.length() >= 10) {
                        dateStr = dateStr.substring(0, 10);
                    }
                    NotificationModel model = new NotificationModel(
                            item.getNotificationId(),
                            item.getTitle(),
                            item.getBody(),
                            dateStr,
                            item.isRead() ? "Đã đọc" : "Chưa đọc",
                            R.drawable.icon_notifications_24
                    );
                    list.add(model);
                }
            }
        }
        return list;
    }

    public static class NotificationItem {
        @SerializedName(value = "notificationId", alternate = {"NotificationId", "id", "Id"})
        private int notificationId;

        @SerializedName(value = "title", alternate = {"Title"})
        private String title;

        @SerializedName(value = "body", alternate = {"Body", "description", "Description"})
        private String body;

        @SerializedName(value = "isRead", alternate = {"IsRead"})
        private boolean isRead;

        @SerializedName(value = "createdAt", alternate = {"CreatedAt"})
        private String createdAt;

        public int getNotificationId() {
            return notificationId;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public boolean isRead() {
            return isRead;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
