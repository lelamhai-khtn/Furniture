package com.example.furniture.popups;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.adapters.CategoryAdapter;
import com.example.furniture.adapters.FilterAdapter;
import com.example.furniture.models.CategoryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class PopupFilter extends BottomSheetDialogFragment {
    private List<CategoryModel> categories;
    private RecyclerView rvCategory;
    private FilterAdapter filterAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RangeSlider rangeSlider = view.findViewById(R.id.rangeSlider);
        rangeSlider.setCustomThumbDrawable(R.drawable.custom_thumb_slider);

        rangeSlider.setThumbRadius(
                getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius)
        );

        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        rvCategory = view.findViewById(R.id.rv_category);
        category();
    }

    private void category() {
        categories = new ArrayList<>();
        CategoryModel c1 = new CategoryModel("Chair", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c2 = new CategoryModel("Bed", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c3 = new CategoryModel("table", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c4 = new CategoryModel("Lamp", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
        categories.add(c4);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layoutManager);

        filterAdapter = new FilterAdapter(categories);
        rvCategory.setAdapter(filterAdapter);
    }
}
