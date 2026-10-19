package com.example.furniture;
import android.graphics.Color;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furniture.dialogs.DialogOffWifi;

public abstract class BaseActivity extends AppCompatActivity {
    private DialogOffWifi networkConnectionDialog;
    protected abstract int getLayoutResourceId();
    protected abstract void init();
    protected abstract void setTitle();
    protected abstract void data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        init();
        setTitle();
        setup();
        data();
        getWindow().getDecorView().post(this::showNoConnectionDialogIfNeeded);
    }

    protected void setup() {
        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.dark(Color.TRANSPARENT)
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }

        View rootView = findViewById(R.id.main);
        if (rootView == null) {
            rootView = findViewById(android.R.id.content);
        }

        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
                return insets;
            });
        }
    }

    private void showNoConnectionDialogIfNeeded() {
        if (!hasInternetConnection()) {
            showNoConnectionDialog(() -> {
                // The dialog is only used to notify about the initial state.
            });
        }
    }

    protected boolean hasInternetConnection() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = manager == null ? null : manager.getActiveNetwork();
        NetworkCapabilities capabilities = activeNetwork == null || manager == null
                ? null : manager.getNetworkCapabilities(activeNetwork);
        return capabilities != null
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
    }

    protected void showNoConnectionDialog(Runnable onConnected) {
        if (isFinishing()) {
            return;
        }

        Runnable callback = () -> {
            networkConnectionDialog = null;
            onConnected.run();
        };
        if (networkConnectionDialog == null) {
            networkConnectionDialog = new DialogOffWifi(this, callback);
        } else {
            networkConnectionDialog.setOnConnected(callback);
        }
        if (!networkConnectionDialog.isShowing()) {
            networkConnectionDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        if (networkConnectionDialog != null && networkConnectionDialog.isShowing()) {
            networkConnectionDialog.dismiss();
        }
        networkConnectionDialog = null;
        super.onDestroy();
    }
}