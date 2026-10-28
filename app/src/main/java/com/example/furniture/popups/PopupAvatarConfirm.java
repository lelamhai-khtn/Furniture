package com.example.furniture.popups;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.furniture.R;
import com.example.furniture.observer.ManageEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PopupAvatarConfirm extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_avatar_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bottomSheet = (View) view.getParent();
        if (bottomSheet != null) {
            bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        }

        ImageButton btnClose = view.findViewById(R.id.btn_close);
        Button btnYes = view.findViewById(R.id.btn_yes);
        Button btnNo = view.findViewById(R.id.btn_no);

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> {
                ManageEvent.getInstance().notifyListeners(false);
                dismiss();
            });
        }

        if (btnNo != null) {
            btnNo.setOnClickListener(v -> {
                ManageEvent.getInstance().notifyListeners(false);
                dismiss();
            });
        }

        if (btnYes != null) {
            btnYes.setOnClickListener(v -> {
                ManageEvent.getInstance().notifyListeners(true);
                dismiss();
            });
        }
    }
}
