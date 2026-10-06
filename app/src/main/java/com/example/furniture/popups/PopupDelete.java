package com.example.furniture.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.furniture.R;
import com.example.furniture.observer.ManageEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

        TextView tvName = view.findViewById(R.id.tv_product_name);
        TextView tvPrice = view.findViewById(R.id.tv_product_price);
        ImageButton btnClose = view.findViewById(R.id.btn_close);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnRemove = view.findViewById(R.id.btn_remove);

        if ( productImage != null && productName != null && productPrice >= 0) {
            String name = productName;
            String price = String.valueOf(productPrice);
            tvName.setText(name);
            tvPrice.setText(price);
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
}
