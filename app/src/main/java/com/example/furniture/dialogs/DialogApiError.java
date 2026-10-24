package com.example.furniture.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture.R;

public class DialogApiError extends Dialog {
    private int statusCode = 0;
    private String customMessage;
    private String title;
    private Runnable onRetry;

    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvCode;
    private Button btnRetry;

    public DialogApiError(@NonNull Context context) {
        super(context);
    }

    public DialogApiError(@NonNull Context context, @Nullable Runnable onRetry) {
        super(context);
        this.onRetry = onRetry;
    }

    public DialogApiError(@NonNull Context context, int statusCode, @Nullable Runnable onRetry) {
        super(context);
        this.statusCode = statusCode;
        this.onRetry = onRetry;
    }

    public DialogApiError(@NonNull Context context, @Nullable String customMessage, @Nullable Runnable onRetry) {
        super(context);
        this.customMessage = customMessage;
        this.onRetry = onRetry;
    }

    public DialogApiError(@NonNull Context context, int statusCode, @Nullable String customMessage, @Nullable Runnable onRetry) {
        super(context);
        this.statusCode = statusCode;
        this.customMessage = customMessage;
        this.onRetry = onRetry;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_api_error);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        tvTitle = findViewById(R.id.tv_api_error_title);
        tvMessage = findViewById(R.id.tv_api_error_message);
        tvCode = findViewById(R.id.tv_api_error_code);
        btnRetry = findViewById(R.id.btn_retry_api_error);

        btnRetry.setOnClickListener(v -> handleRetryClick());
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            window.setGravity(android.view.Gravity.CENTER);
        }
    }

    private void handleRetryClick() {
        dismiss();
        if (onRetry != null) {
            onRetry.run();
        }
    }
}
