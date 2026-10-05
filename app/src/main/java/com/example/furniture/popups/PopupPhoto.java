package com.example.furniture.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PopupPhoto extends BottomSheetDialogFragment {
    private ImageButton btnClose;
    private LinearLayout layoutTakePhoto, layoutChooseGallery, layoutDeletePhoto;

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

        btnClose.setOnClickListener(v -> {
            dismiss();
        });

        if (layoutTakePhoto != null) {
            layoutTakePhoto.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Mở Camera", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }

        if (layoutChooseGallery != null) {
            layoutChooseGallery.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Mở Thư viện", Toast.LENGTH_SHORT).show();
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
}
