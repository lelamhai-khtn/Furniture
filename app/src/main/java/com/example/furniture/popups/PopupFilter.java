package com.example.furniture.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.furniture.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

public class PopupFilter extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RangeSlider rangeSlider = view.findViewById(R.id.rangeSlider); // Tìm view
        rangeSlider.setCustomThumbDrawable(R.drawable.custom_thumb_slider);

        rangeSlider.setThumbRadius(
                getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius)
        );
    }
}
