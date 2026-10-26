package com.example.furniture.activity;

import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.graphics.Insets;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupPhoto;

public class PersonalScreen extends BaseActivity implements IEventListener {
    private ImageView iv_avatar;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_personal_screen;
    }

    @Override
    protected void init() {
        iv_avatar = findViewById(R.id.iv_avatar);

        ManageEvent.getInstance().addListener(this);
        getSupportFragmentManager().setFragmentResultListener(
                "photo_crop_result",
                this,
                (requestKey, bundle) -> {
                    String croppedImageUri = bundle.getString("cropped_image_uri");
                    if (croppedImageUri != null && iv_avatar != null) {
                        iv_avatar.setImageURI(Uri.parse(croppedImageUri));
                    }
                }
        );

        ImageButton imageButton = findViewById(R.id.bi_photo);
        if (imageButton != null) {
            imageButton.setOnClickListener(v -> {
                PopupPhoto popupPhoto = new PopupPhoto();
                popupPhoto.show(getSupportFragmentManager(), "popup_photo");
            });

            imageButton.setOnLongClickListener(v -> {
                iv_avatar.setImageResource(R.drawable.icon_person_off_120);
                return true;
            });
        }
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void data() {

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