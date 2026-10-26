package com.example.furniture.popups;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.adapters.FilterAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CategoryModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        rangeSlider.post(() -> {
            try {
                rangeSlider.setMinSeparation(50f);
            } catch (Exception e) {
                // ignore
            }
        });

        EditText etMin = view.findViewById(R.id.et_min);
        EditText etMax = view.findViewById(R.id.et_max);

        List<Float> values = rangeSlider.getValues();
        if (values.size() >= 2) {
            etMin.setText(String.valueOf(values.get(0).intValue()));
            etMax.setText(String.valueOf(values.get(1).intValue()));
        }

        rangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> currentValues = slider.getValues();
            if (currentValues.size() >= 2) {
                etMin.setText(String.valueOf(currentValues.get(0).intValue()));
                etMax.setText(String.valueOf(currentValues.get(1).intValue()));
            }
        });

        Button btnApply = view.findViewById(R.id.btn_apply);
        Button btnReset = view.findViewById(R.id.btn_reset);
        ImageButton btnClose = view.findViewById(R.id.btn_close);

        btnApply.setOnClickListener(v -> {
            CategoryModel selectedCat = filterAdapter != null ? filterAdapter.getSelectedCategory() : null;
            double min = 0;
            double max = 10000;
            try {
                min = Double.parseDouble(etMin.getText().toString().trim());
            } catch (NumberFormatException ignored) {}
            try {
                max = Double.parseDouble(etMax.getText().toString().trim());
            } catch (NumberFormatException ignored) {}

            Bundle result = new Bundle();
            result.putInt("categoryId", selectedCat != null ? selectedCat.getCategoryId() : 0);
            result.putString("categoryName", selectedCat != null ? selectedCat.getName() : null);
            result.putDouble("minPrice", min);
            result.putDouble("maxPrice", max);

            getParentFragmentManager().setFragmentResult("filter_result", result);
            dismiss();
        });

        btnReset.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putInt("categoryId", 0);
            result.putString("categoryName", null);
            result.putDouble("minPrice", 0.0);
            result.putDouble("maxPrice", 10000.0);

            getParentFragmentManager().setFragmentResult("filter_result", result);
            dismiss();
        });

        btnClose.setOnClickListener(v -> dismiss());

        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));

        rvCategory = view.findViewById(R.id.rv_category);
        category();
    }

    private void category() {
        categories = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layoutManager);

        filterAdapter = new FilterAdapter(categories);
        rvCategory.setAdapter(filterAdapter);

        ApiClient.getCategoriesApi().getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call,
                                   @NonNull Response<List<CategoryModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body());
                    filterAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tải danh mục sản phẩm",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(),
                        "Không thể kết nối để tải danh mục",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
