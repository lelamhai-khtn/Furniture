package com.example.furniture.activity;

import android.net.Uri;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.UserProfileResponse;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupA;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalScreen extends BaseActivity implements IEventListener {
    private ImageView iv_avatar;
    private TextView tvEmail;
    private EditText etFullName;
    private EditText etPhone;
    private AutoCompleteTextView tvGender;
    private EditText etAddress;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_personal_screen;
    }

    @Override
    protected void init() {
        iv_avatar = findViewById(R.id.iv_avatar);
        tvEmail = findViewById(R.id.tvEmail);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        tvGender = findViewById(R.id.tvGender);
        etAddress = findViewById(R.id.etAddress);

        ManageEvent.getInstance().addListener(this);
        getSupportFragmentManager().setFragmentResultListener(
                "photo_crop_result",
                this,
                (requestKey, bundle) -> {
                    if (bundle.getBoolean("avatar_deleted", false)) {
                        if (iv_avatar != null) {
                            iv_avatar.setImageResource(R.drawable.avatar);
                        }
                        return;
                    }
                    String croppedImageUri = bundle.getString("cropped_image_uri");
                    if (croppedImageUri != null && iv_avatar != null) {
                        Glide.with(this)
                                .load(Uri.parse(croppedImageUri))
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(iv_avatar);
                    }
                }
        );

        ImageButton imageButton = findViewById(R.id.bi_photo);
        if (imageButton != null) {
            imageButton.setOnClickListener(v -> {
                PopupA popupA = new PopupA();
                popupA.show(getSupportFragmentManager(), "popup_a");
            });

            imageButton.setOnLongClickListener(v -> {
                iv_avatar.setImageResource(R.drawable.icon_person_off_120);
                return true;
            });
        }
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Personal Profile");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void data() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        ApiClient.getUserApi().getUserProfile().enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserProfileResponse> call, @NonNull Response<UserProfileResponse> response) {
                if (isFinishing()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse profile = response.body();

                    if (profile.getAvatar() != null && !profile.getAvatar().trim().isEmpty() && iv_avatar != null) {
                        Glide.with(PersonalScreen.this)
                                .load(profile.getAvatar())
                                .placeholder(R.drawable.avatar)
                                .error(R.drawable.avatar)
                                .into(iv_avatar);
                    }

                    if (tvEmail != null) {
                        tvEmail.setText(profile.getEmail() != null ? profile.getEmail() : "");
                    }
                    if (etFullName != null) {
                        etFullName.setText(profile.getFullName() != null ? profile.getFullName() : "");
                    }
                    if (etPhone != null) {
                        etPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
                    }
                    if (tvGender != null) {
                        tvGender.setText(profile.getGender() != null ? profile.getGender() : "", false);
                    }
                    if (etAddress != null) {
                        etAddress.setText(profile.getAddress() != null ? profile.getAddress() : "");
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(PersonalScreen.this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalScreen.this, "Không thể tải thông tin cá nhân (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserProfileResponse> call, @NonNull Throwable t) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(PersonalScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEvent(boolean success) {
        if (success) {
            iv_avatar.setImageResource(R.drawable.icon_person_off_120);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManageEvent.getInstance().removeListener(this);
    }
}
