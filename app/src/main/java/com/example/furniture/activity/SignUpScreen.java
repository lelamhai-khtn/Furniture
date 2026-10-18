package com.example.furniture.activity;

import android.content.Intent;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import com.example.furniture.api.ApiClient;
import com.example.furniture.api.GoogleAuthApi;
import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.dialogs.DialogLoading;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpScreen extends BaseActivity {
    private TextInputEditText fullNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextView tvError;
    private GoogleAuthApi authApi;
    private DialogLoading loadingDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_up_screen;
    }

    @Override
    protected void init() {
        fullNameInput = findViewById(R.id.etFullName);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        confirmPasswordInput = findViewById(R.id.etConfirmPassword);
        tvError = findViewById(R.id.tv_error);
        authApi = ApiClient.getAuthApi();
        loadingDialog = new DialogLoading(this);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(v -> register());
    }

    @Override
    protected void setTitle() {
        // The title is rendered by the layout.
    }

    @Override
    protected void data() {
        // Sign-up data is handled after form submission.
    }

    private void register() {
        hideError();

        String fullName = getText(fullNameInput);
        String email = getText(emailInput);
        String password = getText(passwordInput);
        String confirmPassword = getText(confirmPasswordInput);

        if (fullName.isEmpty()) {
            showError("Vui lòng nhập họ và tên");
        } else if (email.isEmpty()) {
            showError("Vui lòng nhập email");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Email không đúng định dạng");
        } else if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
        } else if (password.length() < 6) {
            showError("Mật khẩu phải có ít nhất 6 ký tự");
        } else if (confirmPassword.isEmpty()) {
            showError("Vui lòng xác nhận mật khẩu");
        } else if (!password.equals(confirmPassword)) {
            showError("Mật khẩu xác nhận không khớp");
        } else if (!hasInternetConnection()) {
            showNoConnectionDialog(this::register);
        } else {
            submitRegistration(fullName, email, password);
        }
    }

    private void submitRegistration(String fullName, String email, String password) {
        loadingDialog.showLoading("Đang đăng ký...");
        authApi.register(new GoogleAuthApi.RegisterRequest(fullName, email, password))
                .enqueue(new Callback<com.example.furniture.models.AuthResponse>() {
                    @Override
                    public void onResponse(
                            Call<com.example.furniture.models.AuthResponse> call,
                            Response<com.example.furniture.models.AuthResponse> response
                    ) {
                        hideLoading();
                        if (response.isSuccessful()) {
                            Toast.makeText(
                                    SignUpScreen.this,
                                    "Đăng ký thành công",
                                    Toast.LENGTH_SHORT
                            ).show();
                            startActivity(new Intent(SignUpScreen.this, SignInScreen.class));
                            finish();
                        } else {
                            showError(getApiError(response));
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<com.example.furniture.models.AuthResponse> call,
                            Throwable t
                    ) {
                        hideLoading();
                        showError("Không kết nối được server");
                    }
                });
    }

    private String getApiError(Response<?> response) {
        if (response.errorBody() != null) {
            try {
                String error = response.errorBody().string();
                com.google.gson.JsonObject json =
                        com.google.gson.JsonParser.parseString(error).getAsJsonObject();
                if (json.has("message") && !json.get("message").isJsonNull()) {
                    return json.get("message").getAsString();
                }
                if (json.has("title") && !json.get("title").isJsonNull()) {
                    return json.get("title").getAsString();
                }
            } catch (IOException | RuntimeException ignored) {
                // Use the status fallback when the server response is not JSON.
            }
        }
        return "Đăng ký thất bại (" + response.code() + ")";
    }

    private String getText(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setText("");
        tvError.setVisibility(View.GONE);
    }

    private void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}