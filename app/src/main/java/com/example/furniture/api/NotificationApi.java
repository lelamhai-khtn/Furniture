package com.example.furniture.api;

import com.example.furniture.models.DeleteNotificationResponse;
import com.example.furniture.models.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NotificationApi {
    @GET("api/notifications")
    Call<NotificationResponse> getNotifications();

    @DELETE("api/notifications/{id}")
    Call<DeleteNotificationResponse> deleteNotification(@Path("id") int notificationId);
}
