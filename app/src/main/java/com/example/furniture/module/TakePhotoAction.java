package com.example.furniture.module;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TakePhotoAction implements IPhotoActionHandler {
    private final FragmentActivity activity;
    private final ActivityResultLauncher<String> permissionLauncher;
    private final ActivityResultLauncher<Intent> cameraLauncher;
    private final OnUriCreatedListener uriListener;

    public interface OnUriCreatedListener {
        void onUriCreated(Uri uri);
    }

    public TakePhotoAction(FragmentActivity activity,
                           ActivityResultLauncher<String> permissionLauncher,
                           ActivityResultLauncher<Intent> cameraLauncher,
                           OnUriCreatedListener uriListener) {
        this.activity = activity;
        this.permissionLauncher = permissionLauncher;
        this.cameraLauncher = cameraLauncher;
        this.uriListener = uriListener;
    }

    @Override
    public void execute() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            if (permissionLauncher != null) {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(activity, "Failed to create image file", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.example.furniture.fileprovider",
                        photoFile);

                if (uriListener != null) {
                    uriListener.onUriCreated(photoURI);
                }

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if (cameraLauncher != null) {
                    cameraLauncher.launch(takePictureIntent);
                }
            }
        } else {
            Toast.makeText(activity, "No camera application found", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}