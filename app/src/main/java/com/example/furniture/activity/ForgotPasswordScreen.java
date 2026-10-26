package com.example.furniture.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;

public class ForgotPasswordScreen extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_forgot_password_screen;
    }

    @Override
    protected void init() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void data() {

    }

    @Override
    protected void applyWindowInsets(View view, Insets systemBars) {
        view.setPadding(systemBars.left, 0, systemBars.right, 0);
    }
}