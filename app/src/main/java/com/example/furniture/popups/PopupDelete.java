package com.example.furniture.popups;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.observer.ManageEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Locale;

public class PopupDelete extends BottomSheetDialogFragment {
    private String productName;
    private double productPrice;
    private String productImage;

    public PopupDelete(String productName, double productPrice, String productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_delete, container, false);

        ImageView ivProduct = view.findViewById(R.id.iv_product);
        TextView tvName = view.findViewById(R.id.tv_product_name);
        TextView tvPrice = view.findViewById(R.id.tv_product_price);
        ImageButton btnClose = view.findViewById(R.id.btn_close);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnRemove = view.findViewById(R.id.btn_remove);

        if (productName != null) {
            tvName.setText(productName);
        }
        if (productPrice >= 0) {
            String priceText = String.format(Locale.US, "$%.0f", productPrice);
            tvPrice.setText(priceText);
        }
        if (productImage != null && !productImage.trim().isEmpty() && ivProduct != null) {
            Glide.with(this)
                    .load(productImage)
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(ivProduct);
        }

        btnClose.setOnClickListener(v -> {
            ManageEvent.getInstance().notifyListeners(false);
            dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            ManageEvent.getInstance().notifyListeners(false);
            dismiss();
        });

        btnRemove.setOnClickListener(v -> {
            ManageEvent.getInstance().notifyListeners(true);
            dismiss();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View bottomSheet = (View) view.getParent();
        bottomSheet.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
    }
}
