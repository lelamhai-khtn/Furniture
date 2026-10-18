package com.example.furniture.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class DialogLoading extends Dialog {
    private final TextView messageView;

    public DialogLoading(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        LinearLayout container = new LinearLayout(context);
        container.setGravity(Gravity.CENTER_VERTICAL);
        container.setOrientation(LinearLayout.HORIZONTAL);
        int horizontalPadding = dp(context, 24);
        int verticalPadding = dp(context, 18);
        container.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE);
        background.setCornerRadius(dp(context, 12));
        container.setBackground(background);

        ProgressBar progressBar = new ProgressBar(context);
        container.addView(progressBar, new LinearLayout.LayoutParams(
                dp(context, 32),
                dp(context, 32)
        ));

        messageView = new TextView(context);
        messageView.setTextColor(Color.BLACK);
        messageView.setTextSize(14);
        messageView.setText("Đang xử lý...");
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        messageParams.setMarginStart(dp(context, 16));
        container.addView(messageView, messageParams);

        setContentView(container);

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

    public void showLoading(String message) {
        messageView.setText(message);
        if (!isShowing()) {
            show();
        }
        Window window = getWindow();
        if (window != null) {
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
