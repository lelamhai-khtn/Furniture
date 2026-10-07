package com.example.furniture.module;

import android.content.Intent;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;

public class GalleryPhotoAction implements IPhotoActionHandler{
    private final ActivityResultLauncher<Intent> galleryLauncher;

    public GalleryPhotoAction(ActivityResultLauncher<Intent> galleryLauncher) {
        this.galleryLauncher = galleryLauncher;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }
}
