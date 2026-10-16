package com.example.furniture.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furniture.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInScreen extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.light(Color.WHITE, Color.BLACK)
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }


        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpScreen.class);
            startActivity(intent);
        });

        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v->{
            Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);
        });



        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data == null) {
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

        SignInButton btnGoogleSignIn = findViewById(R.id.btn_google_sign_in);
        btnGoogleSignIn.setOnClickListener(v -> signIn());

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.google_web_client_id))
                        .requestEmail()
                        .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void signIn() {
        if (mGoogleSignInClient == null) {
            Toast.makeText(this, "Google Sign-In chưa được khởi tạo", Toast.LENGTH_SHORT).show();
            return;
        }

        googleSignInLauncher.launch(mGoogleSignInClient.getSignInIntent());
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account == null) {
                showGoogleError("Google không trả về tài khoản");
                return;
            }

            String idToken = account.getIdToken();
            if (idToken == null || idToken.isEmpty()) {
                Log.e("GoogleAuth", "Google account returned without an ID token");
                showGoogleError("Không nhận được idToken");
                return;
            }

            Log.d("GoogleAuth", "Google ID token received");
            Toast.makeText(
                    this,
                    "Đăng nhập Google thành công: " + account.getEmail(),
                    Toast.LENGTH_SHORT
            ).show();

            // Gửi idToken này lên ASP.NET Core API tại đây.
            // sendTokenToApi(idToken);
        } catch (ApiException e) {
            Log.e("GoogleAuth", "Google sign-in failed, statusCode=" + e.getStatusCode(), e);
            showGoogleError("Đăng nhập Google thất bại (" + e.getStatusCode() + ")");
        }
    }

    private void showGoogleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}