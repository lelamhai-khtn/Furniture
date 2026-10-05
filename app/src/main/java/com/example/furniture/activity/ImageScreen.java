package com.example.furniture.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.canhub.cropper.CropImageView;
import com.example.furniture.R;

public class ImageScreen extends AppCompatActivity {
    private CropImageView cropImageView;
    private Button btnCropAndSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.light(Color.WHITE, Color.BLACK)
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }
        cropImageView = findViewById(R.id.cropImageView);
        btnCropAndSave = findViewById(R.id.btn_crop_and_save);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("captured_image_uri")) {
            String uriString = intent.getStringExtra("captured_image_uri");
            Uri imageUri = Uri.parse(uriString);

            cropImageView.setImageUriAsync(imageUri);
        }

        btnCropAndSave.setOnClickListener(v -> {
            Bitmap croppedBitmap = cropImageView.getCroppedImage();
            if (croppedBitmap != null) {
                Toast.makeText(this, "Cắt ảnh thành công!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}