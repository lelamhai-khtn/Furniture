package com.example.furniture.activity;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.textfield.TextInputEditText;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.api.GoogleAuthApi;
import com.example.furniture.models.AuthResponse;
import com.example.furniture.utils.AuthSessionStore;
import com.example.furniture.utils.AppPreferences;
import com.example.furniture.dialogs.DialogLoading;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInScreen extends BaseActivity {
    private static final String API_BASE_URL = "http://192.168.1.11:7279/";
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleAuthApi googleAuthApi;
    private DialogLoading loadingDialog;
    private AuthSessionStore authSessionStore;
    private AppPreferences appPreferences;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private CheckBox rememberMe;
    private TextView tvError;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in_screen;
    }

    @Override
    protected void init() {
        initializeGoogleSignIn();
        initializeGoogleAuthApi();
        loadingDialog = new DialogLoading(this);
        authSessionStore = new AuthSessionStore(this);
        appPreferences = new AppPreferences(this);
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        rememberMe = findViewById(R.id.cbRemember);
        tvError = findViewById(R.id.tv_error);
        loadRememberedEmail();
        registerGoogleSignInLauncher();
        initializeClickListeners();
    }

    @Override
    protected void setTitle() {
        // The title is rendered by the layout.
    }

    @Override
    protected void data() {
        // Sign-in data is loaded after a successful authentication.
    }

    private void initializeClickListeners() {
        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpScreen.class);
            startActivity(intent);
        });

        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> signInWithEmailAndPassword());

        findViewById(R.id.btnSignInGoogle).setOnClickListener(v -> signIn());

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                saveRememberedEmail();
            } else {
                clearRememberedEmail();
            }
        });
    }

    private void registerGoogleSignInLauncher() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data == null) {
                        hideLoading();
                        Log.e("GoogleAuth", "Google sign-in returned no data, resultCode="
                                + result.getResultCode());
                        showGoogleError("Google không trả về kết quả ("
                                + result.getResultCode() + ")");
                        return;
                    }

                    Task<GoogleSignInAccount> task =
                            GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
        );
    }

    private void initializeGoogleSignIn() {
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_web_client_id))
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void initializeGoogleAuthApi() {
        googleAuthApi = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GoogleAuthApi.class);
    }

    private void signIn() {
        if (!hasInternetConnection()) {
            showNoConnectionDialog(this::signIn);
            return;
        }

        if (mGoogleSignInClient == null) {
            Toast.makeText(this, "Google Sign-In chưa được khởi tạo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loadingDialog != null && loadingDialog.isShowing()) {
            return;
        }

        loadingDialog.showLoading("Đang kết nối với Google...");
        try {
            googleSignInLauncher.launch(mGoogleSignInClient.getSignInIntent());
        } catch (RuntimeException e) {
            hideLoading();
            Log.e("GoogleAuth", "Could not launch Google sign-in", e);
            showGoogleError("Không thể mở đăng nhập Google");
        }
    }

    private void signInWithEmailAndPassword() {
        hideError();

        String email = emailInput.getText() == null
                ? "" : emailInput.getText().toString().trim();
        String password = passwordInput.getText() == null
                ? "" : passwordInput.getText().toString();

        if (email.isEmpty()) {
            showError("Vui lòng nhập email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Email không đúng định dạng");
            return;
        }
        if (password.trim().isEmpty()) {
            showError("Vui lòng nhập mật khẩu");
            return;
        }

        if (!hasInternetConnection()) {
            showNoConnectionDialog(this::signInWithEmailAndPassword);
            return;
        }

        loadingDialog.showLoading("Đang đăng nhập...");
        googleAuthApi.login(new GoogleAuthApi.LoginRequest(email, password))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(
                            Call<AuthResponse> call,
                            Response<AuthResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateRememberedEmail(email);
                            saveSessionAndOpenMain(response.body());
                            return;
                        }

                        hideLoading();
                        showGoogleError("Email hoặc mật khẩu không đúng");
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        hideLoading();
                        Log.e("Auth", "Cannot connect to server", t);
                        showGoogleError("Không kết nối được server");
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account == null) {
                hideLoading();
                showGoogleError("Google không trả về tài khoản");
                return;
            }

            String idToken = account.getIdToken();
            if (idToken == null || idToken.isEmpty()) {
                hideLoading();
                Log.e("GoogleAuth", "Google account returned without an ID token");
                showGoogleError("Không nhận được idToken");
                return;
            }

            sendIdTokenToServer(idToken, account.getEmail());
        } catch (ApiException e) {
            hideLoading();
            Log.e("GoogleAuth", "Google sign-in failed, statusCode=" + e.getStatusCode(), e);
            showGoogleError("Đăng nhập Google thất bại (" + e.getStatusCode() + ")");
        }
    }

    private void sendIdTokenToServer(String idToken, String email) {
        googleAuthApi.loginWithGoogle(new GoogleAuthApi.GoogleLoginRequest(idToken))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(
                            Call<AuthResponse> call,
                            Response<AuthResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            saveSessionAndOpenMain(response.body());
                            Log.d("GoogleAuth", "Server accepted Google ID token");
                            Toast.makeText(
                                    SignInScreen.this,
                                    "Đăng nhập thành công: " + email,
                                    Toast.LENGTH_SHORT
                            ).show();
                            return;
                        }

                        hideLoading();
                        Log.e("GoogleAuth", "Server rejected ID token. HTTP "
                                + response.code());
                        showGoogleError("Server từ chối đăng nhập (HTTP "
                                + response.code() + ")");
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        hideLoading();
                        Log.e("GoogleAuth", "Cannot connect to server", t);
                        String message = t.getMessage() == null
                                ? t.getClass().getSimpleName()
                                : t.getMessage();
                        showGoogleError("Không kết nối được server: " + message);
                    }
                });
    }

    private void saveSessionAndOpenMain(AuthResponse response) {
        try {
            authSessionStore.save(response);
        } catch (IllegalArgumentException e) {
            hideLoading();
            Log.e("Auth", "Server returned an invalid auth response", e);
            showGoogleError("Thông tin đăng nhập từ server không hợp lệ");
            return;
        }

        hideLoading();
        startActivity(new Intent(this, MainScreen.class));
        finish();
    }

    private void showGoogleError(String message) {
        showError(message);
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setText("");
        tvError.setVisibility(View.GONE);
    }

    private void loadRememberedEmail() {
        String rememberedEmail = appPreferences.getEmail();

        if (rememberedEmail == null || rememberedEmail.isEmpty()) {
            return;
        }

        emailInput.setText(rememberedEmail);
        rememberMe.setChecked(true);
    }

    private void saveRememberedEmail() {
        String email = emailInput.getText() == null
                ? "" : emailInput.getText().toString().trim();
        if (!email.isEmpty()) {
            appPreferences.saveEmail(email);
        }
    }

    private void updateRememberedEmail(String email) {
        if (rememberMe.isChecked()) {
            appPreferences.saveEmail(email);
        } else {
            clearRememberedEmail();
        }
    }

    private void clearRememberedEmail() {
        appPreferences.clearEmail();
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