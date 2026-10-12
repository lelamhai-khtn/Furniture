package com.example.furniture.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.furniture.R;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupPhoto;

public class PersonalScreen extends AppCompatActivity implements IEventListener {
    private ImageView iv_avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.dark(ContextCompat.getColor(this, R.color.white))
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        iv_avatar = findViewById(R.id.iv_avatar);

        ManageEvent.getInstance().addListener(this);
        getSupportFragmentManager().setFragmentResultListener(
                "photo_crop_result",
                this,
                (requestKey, bundle) -> {
                    String croppedImageUri = bundle.getString("cropped_image_uri");
                    if (croppedImageUri != null && iv_avatar != null) {
                        iv_avatar.setImageURI(android.net.Uri.parse(croppedImageUri));
                    }
                }
        );

        ImageButton imageButton = findViewById(R.id.bi_photo);
        imageButton.setOnClickListener(v -> {
            PopupPhoto popupPhoto = new PopupPhoto();
            popupPhoto.show(getSupportFragmentManager(), "popup_photo");
        });

        imageButton .setOnLongClickListener(v -> {
            iv_avatar.setImageResource(R.drawable.icon_person_off_120);
            return true;
        });
    }

    @Override
    public void onEvent(boolean success) {
        if(success) {
            iv_avatar.setImageResource(R.drawable.icon_person_off_120);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManageEvent.getInstance().removeListener(this);
    }
}