package com.example.furniture.activity;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.adapters.NotificationAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.DeleteNotificationResponse;
import com.example.furniture.models.NotificationModel;
import com.example.furniture.models.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationScreen extends BaseActivity {
    private RecyclerView rvNotification;
    private NotificationAdapter notificationAdapter;
    private final List<NotificationModel> notificationList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notification_screen;
    }

    @Override
    protected void init() {
        rvNotification = findViewById(R.id.rv_notification);
        if (rvNotification != null) {
            rvNotification.setLayoutManager(new LinearLayoutManager(this));
        }
        notificationAdapter = new NotificationAdapter(notificationList);
        if (rvNotification != null) {
            rvNotification.setAdapter(notificationAdapter);

            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < notificationList.size()) {
                        NotificationModel item = notificationList.get(position);
                        int notificationId = item.getNotificationId();

                        notificationList.remove(position);
                        notificationAdapter.notifyItemRemoved(position);

                        ApiClient.getNotificationApi().deleteNotification(notificationId).enqueue(new Callback<DeleteNotificationResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<DeleteNotificationResponse> call, @NonNull Response<DeleteNotificationResponse> response) {
                                if (isFinishing()) {
                                    return;
                                }
                                if (response.isSuccessful() && response.body() != null) {
                                    DeleteNotificationResponse res = response.body();
                                    String msg = res.getMessage() != null ? res.getMessage() : "Xóa thông báo thành công";
                                    Toast.makeText(NotificationScreen.this, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NotificationScreen.this, "Xóa thông báo thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DeleteNotificationResponse> call, @NonNull Throwable t) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(NotificationScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            };
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvNotification);
        }
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Notification");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void data() {
        ApiClient.getNotificationApi().getNotifications().enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                if (isFinishing()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    notificationList.clear();
                    notificationList.addAll(response.body().getNotificationModels());
                    notificationAdapter.notifyDataSetChanged();
                } else if (response.code() == 401) {
                    Toast.makeText(NotificationScreen.this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotificationScreen.this, "Không thể tải thông báo (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(NotificationScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
