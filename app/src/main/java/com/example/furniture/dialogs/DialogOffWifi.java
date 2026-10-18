package com.example.furniture.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.furniture.R;

public class DialogOffWifi extends Dialog {
    private Runnable onConnected;

    public DialogOffWifi(
            @NonNull Context context,
            @NonNull Runnable onConnected
    ) {
        super(context);
        this.onConnected = onConnected;
    }

    public void setOnConnected(@NonNull Runnable onConnected) {
        this.onConnected = onConnected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_no_connection);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        TextView retryButton = findViewById(R.id.btn_retry_connection);
        retryButton.setOnClickListener(v -> retryConnection());
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

    private void retryConnection() {
        ConnectivityManager manager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = manager == null ? null : manager.getActiveNetwork();
        boolean connected = activeNetwork != null
                && manager.getNetworkCapabilities(activeNetwork) != null
                && manager.getNetworkCapabilities(activeNetwork).hasCapability(
                android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && manager.getNetworkCapabilities(activeNetwork).hasCapability(
                android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED);

        if (connected) {
            dismiss();
            onConnected.run();
        }
    }
}
