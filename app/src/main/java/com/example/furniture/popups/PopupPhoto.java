package com.example.furniture.popups;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.example.furniture.R;
import com.example.furniture.activity.ImageScreen;
import com.example.furniture.module.GalleryPhotoAction;
import com.example.furniture.module.IPhotoActionHandler;
import com.example.furniture.module.TakePhotoAction;
import com.example.furniture.observer.ManageEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PopupPhoto extends BottomSheetDialogFragment {
    private Uri photoURI;

    private final ActivityResultLauncher<Intent> imageScreenLauncher = registerForActivityResult(
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

    // 1. DECLARE CAMERA LAUNCHER FIRST (To avoid uninitialized error)
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && photoURI != null) {
                    Intent intent = new Intent(requireContext(), ImageScreen.class);
                    intent.putExtra("captured_image_uri", photoURI.toString());
                    imageScreenLauncher.launch(intent);
                } else {
                    dismiss();
                }
            }
    );

    // 2. DECLARE REQUEST PERMISSION LAUNCHER AFTER CAMERA
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    TakePhotoAction takePhotoAction = new TakePhotoAction(
                            requireActivity(),
                            null,
                            cameraLauncher,
                            uri -> photoURI = uri
                    );
                    takePhotoAction.openCamera();
                } else {
                    Toast.makeText(getContext(), "Camera permission is required to take photos!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // 3. DECLARE GALLERY LAUNCHER
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        Intent intent = new Intent(requireContext(), ImageScreen.class);
                        intent.putExtra("captured_image_uri", selectedImageUri.toString());
                        imageScreenLauncher.launch(intent);
                    } else {
                        dismiss();
                    }
                } else {
                    dismiss();
                }
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

        ImageButton btnClose = view.findViewById(R.id.btn_close);
        LinearLayout layoutTakePhoto = view.findViewById(R.id.layoutTakePhoto);
        LinearLayout layoutChooseGallery = view.findViewById(R.id.layoutChooseGallery);
        LinearLayout layoutDeletePhoto = view.findViewById(R.id.layoutDeletePhoto);

        btnClose.setOnClickListener(v -> dismiss());

        IPhotoActionHandler takePhotoAction = new TakePhotoAction(requireActivity(), requestPermissionLauncher, cameraLauncher, uri -> photoURI = uri);
        IPhotoActionHandler galleryAction = new GalleryPhotoAction(galleryLauncher);

        if (layoutTakePhoto != null) {
            layoutTakePhoto.setOnClickListener(v -> takePhotoAction.execute());
        }

        if (layoutChooseGallery != null) {
            layoutChooseGallery.setOnClickListener(v -> galleryAction.execute());
        }

        if( layoutDeletePhoto != null) {
            layoutDeletePhoto.setOnClickListener(v -> {
                ManageEvent.getInstance().notifyListeners(true);
                dismiss();
            });
        }
    }
}