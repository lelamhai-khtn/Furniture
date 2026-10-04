package com.example.furniture.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.furniture.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PopupDelete extends BottomSheetDialogFragment {
    private static final String ARG_NAME = "product_name";
    private static final String ARG_PRICE = "product_price";
    private static final String ARG_IMAGE = "product_image";
    private static final String ARG_POSITION = "product_position";

    public static PopupDelete newInstance(String productName, String productPrice, String productImage, int position) {
        PopupDelete fragment = new PopupDelete();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, productName);
        args.putString(ARG_PRICE, productPrice);
        args.putString(ARG_IMAGE, productImage);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDeleteClickListener {
        void onDeleteConfirm(int position);
    }

    private OnDeleteClickListener listener;
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_delete, container, false);

        TextView tvName = view.findViewById(R.id.tv_product_name);
        TextView tvPrice = view.findViewById(R.id.tv_product_price);
        ImageButton btnClose = view.findViewById(R.id.btn_close);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnRemove = view.findViewById(R.id.btn_remove);

        if (getArguments() != null) {
            String name = getArguments().getString(ARG_NAME);
            String price = getArguments().getString(ARG_PRICE);
            String image = getArguments().getString(ARG_IMAGE);
            int position = getArguments().getInt(ARG_POSITION);
            tvName.setText(name);
            tvPrice.setText(price);
        }

        // Xử lý sự kiện nút
        btnClose.setOnClickListener(v -> dismiss());
        btnCancel.setOnClickListener(v -> dismiss());

        btnRemove.setOnClickListener(v -> {
            if (listener != null && getArguments() != null) {
                // Lấy lại position đã truyền vào và gửi ngược về Adapter
                int position = getArguments().getInt(ARG_POSITION);
                listener.onDeleteConfirm(position);
            }
            dismiss();
        });

        return view;
    }
}
