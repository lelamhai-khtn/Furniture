package com.example.furniture.popups;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.furniture.R;
import com.example.furniture.activity.ImageScreen;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PopupPhoto extends BottomSheetDialogFragment {
    private ImageButton btnClose;
    private LinearLayout layoutTakePhoto, layoutChooseGallery, layoutDeletePhoto;

    // Biến lưu trữ đường dẫn (URI) của bức ảnh chất lượng cao
    private Uri photoURI;

    // 1. Launcher xử lý xin quyền Camera
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Bạn cần cấp quyền Camera để chụp ảnh!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // 2. Launcher xử lý kết quả sau khi Camera chụp xong
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Kiểm tra xem người dùng có bấm chụp và lưu thành công không
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (photoURI != null) {
                        // Ảnh đã lưu vào photoURI, truyền đường dẫn URI sang ImageScreen
                        Intent intent = new Intent(requireContext(), ImageScreen.class);
                        intent.putExtra("captured_image_uri", photoURI.toString());
                        startActivity(intent);
                        Toast.makeText(getContext(), "Đã chụp ảnh thành công!", Toast.LENGTH_SHORT).show();
                    }
                }
                dismiss();
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnClose = view.findViewById(R.id.btn_close);
        layoutTakePhoto = view.findViewById(R.id.layoutTakePhoto);
        layoutChooseGallery = view.findViewById(R.id.layoutChooseGallery);
        layoutDeletePhoto = view.findViewById(R.id.layoutDeletePhoto);

        btnClose.setOnClickListener(v -> dismiss());

        if (layoutTakePhoto != null) {
            layoutTakePhoto.setOnClickListener(v -> {
                checkCameraPermissionAndOpen();
            });
        }

        if (layoutChooseGallery != null) {
            layoutChooseGallery.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Open Gallery", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }

        if (layoutDeletePhoto != null) {
            layoutDeletePhoto.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Đã xóa ảnh", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }
    }

    private void checkCameraPermissionAndOpen() {
        if (getContext() == null) return;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Tạo một file rỗng để hứng ảnh
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Lỗi tạo file ảnh", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                // Lấy URI an toàn thông qua FileProvider
                photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.example.furniture.fileprovider",
                        photoFile);

                // Yêu cầu Camera lưu ảnh vào đường dẫn URI này
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                cameraLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(getContext(), "Không tìm thấy ứng dụng Camera trên máy", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm phụ trợ: Tạo file rỗng trong bộ nhớ máy với tên theo ngày giờ
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
}