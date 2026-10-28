package com.example.furniture.popups;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.furniture.R;
import com.example.furniture.activity.CropImageScreen;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.AuthResponse;
import com.example.furniture.models.DeleteAvatarResponse;
import com.example.furniture.utils.AuthSessionStore;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupA extends BottomSheetDialogFragment {
    private Uri photoURI;

    private final ActivityResultLauncher<Intent> editImageScreenLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null
                        && result.getData().hasExtra("cropped_image_uri")) {
                    String croppedImageUri = result.getData().getStringExtra("cropped_image_uri");
                    Bundle bundle = new Bundle();
                    bundle.putString("cropped_image_uri", croppedImageUri);
                    getParentFragmentManager().setFragmentResult("photo_crop_result", bundle);
                }
                dismiss();
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && photoURI != null) {
                    Intent intent = new Intent(requireContext(), CropImageScreen.class);
                    intent.putExtra("captured_image_uri", photoURI.toString());
                    editImageScreenLauncher.launch(intent);
                } else {
                    dismiss();
                }
            }
    );

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        Intent intent = new Intent(requireContext(), CropImageScreen.class);
                        intent.putExtra("captured_image_uri", selectedImageUri.toString());
                        editImageScreenLauncher.launch(intent);
                        return;
                    }
                }
                dismiss();
            }
    );

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "Camera permission is required!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnClose = view.findViewById(R.id.btn_close);
        View buttonA = view.findViewById(R.id.buttonA);
        View buttonB = view.findViewById(R.id.buttonB);
        View buttonC = view.findViewById(R.id.buttonC);

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dismiss());
        }

        if (buttonA != null) {
            buttonA.setOnClickListener(v -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
            });
        }

        if (buttonB != null) {
            buttonB.setOnClickListener(v -> openGallery());
        }

        if (buttonC != null) {
            buttonC.setOnClickListener(v -> deleteAvatar());
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT).show();
        }

        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(requireContext(),
                    "com.example.furniture.fileprovider",
                    photoFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                cameraLauncher.launch(takePictureIntent);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            galleryLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Unable to open gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAvatar() {
        if (getContext() != null) {
            Toast.makeText(getContext(), "Đang xóa ảnh đại diện...", Toast.LENGTH_SHORT).show();
        }
        ApiClient.getUserApi().deleteAvatar().enqueue(new Callback<DeleteAvatarResponse>() {
            @Override
            public void onResponse(@NonNull Call<DeleteAvatarResponse> call, @NonNull Response<DeleteAvatarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DeleteAvatarResponse res = response.body();
                    String msg = res.getMessage() != null ? res.getMessage() : "Xóa avatar thành công";
                    if (getContext() != null) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        AuthSessionStore store = new AuthSessionStore(getContext());
                        AuthResponse session = store.get();
                        if (session != null && session.getUser() != null) {
                            session.getUser().setAvatar(null);
                            store.save(session);
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("avatar_deleted", true);
                    getParentFragmentManager().setFragmentResult("photo_crop_result", bundle);
                    dismiss();
                } else if (response.code() == 401) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Xóa avatar thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeleteAvatarResponse> call, @NonNull Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}
