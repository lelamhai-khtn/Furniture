package com.example.furniture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.canhub.cropper.CropImageView;
import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.AuthResponse;
import com.example.furniture.models.AvatarResponse;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupAvatarConfirm;
import com.example.furniture.utils.AuthSessionStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CropImageScreen extends BaseActivity implements IEventListener {
    private CropImageView cropImageView;
    private Button btnCropAndSave;
    private Bitmap pendingCroppedBitmap;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_crop_image_screen;
    }

    @Override
    protected void init() {
        ManageEvent.getInstance().addListener(this);

        cropImageView = findViewById(R.id.cropImageView);
        btnCropAndSave = findViewById(R.id.btn_crop_and_save);

        if (btnCropAndSave != null) {
            btnCropAndSave.setOnClickListener(v -> {
                pendingCroppedBitmap = cropImageView.getCroppedImage();
                if (pendingCroppedBitmap == null) {
                    Toast.makeText(this, "Không thể lấy hình ảnh đã cắt", Toast.LENGTH_SHORT).show();
                    return;
                }

                PopupAvatarConfirm popup = new PopupAvatarConfirm();
                popup.show(getSupportFragmentManager(), "PopupAvatarConfirm");
            });
        }
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Crop Image");
        }

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> closeWithoutResult());
        }
    }

    @Override
    protected void data() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("captured_image_uri")) {
            String uriString = intent.getStringExtra("captured_image_uri");
            if (uriString != null && !uriString.trim().isEmpty()) {
                Uri imageUri = Uri.parse(uriString);
                cropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onEvent(boolean success) {
        if (pendingCroppedBitmap == null) {
            return;
        }

        if (success) {
            uploadCroppedAvatar(pendingCroppedBitmap);
        }
    }

    private void uploadCroppedAvatar(Bitmap bitmap) {
        File croppedFile = saveBitmapToFile(bitmap);
        if (croppedFile == null) {
            Toast.makeText(this, "Lỗi khi lưu hình ảnh tạm", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), croppedFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", croppedFile.getName(), requestFile);

        btnCropAndSave.setEnabled(false);
        Toast.makeText(this, "Đang tải ảnh đại diện lên...", Toast.LENGTH_SHORT).show();

        ApiClient.getUserApi().uploadAvatar(body).enqueue(new Callback<AvatarResponse>() {
            @Override
            public void onResponse(@NonNull Call<AvatarResponse> call, @NonNull Response<AvatarResponse> response) {
                btnCropAndSave.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    AvatarResponse res = response.body();
                    String avatarUrl = res.getAvatar();
                    String msg = res.getMessage() != null ? res.getMessage() : "Cập nhật avatar thành công";
                    Toast.makeText(CropImageScreen.this, msg, Toast.LENGTH_SHORT).show();

                    AuthSessionStore store = new AuthSessionStore(CropImageScreen.this);
                    AuthResponse session = store.get();
                    if (session != null && session.getUser() != null) {
                        session.getUser().setAvatar(avatarUrl);
                        store.save(session);
                    }

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("cropped_image_uri", avatarUrl);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else if (response.code() == 401) {
                    Toast.makeText(CropImageScreen.this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CropImageScreen.this, "Tải ảnh lên thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AvatarResponse> call, @NonNull Throwable t) {
                btnCropAndSave.setEnabled(true);
                Toast.makeText(CropImageScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File croppedFile = new File(
                    getExternalFilesDir("Pictures"),
                    "cropped_avatar_" + System.currentTimeMillis() + ".jpg"
            );
            try (FileOutputStream outputStream = new FileOutputStream(croppedFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }
            return croppedFile;
        } catch (IOException e) {
            return null;
        }
    }

    private void closeWithoutResult() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManageEvent.getInstance().removeListener(this);
    }
}
