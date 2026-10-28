package com.example.furniture.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.R;
import com.example.furniture.activity.CartScreen;
import com.example.furniture.adapters.CartAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartItemModel;
import com.example.furniture.models.CartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView rvCart;
    private TextView tvPrice;
    private View bottomScreen;
    private CartAdapter cartAdapter;
    private List<CartItemModel> cartItemsList = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCart = view.findViewById(R.id.rv_card);
        tvPrice = view.findViewById(R.id.tv_price);
        bottomScreen = view.findViewById(R.id.bottom_screen);

        TextView tvTitleHeader = view.findViewById(R.id.tv_title_header);
        if (tvTitleHeader != null) {
            tvTitleHeader.setText("My Cart");
        }

        if (bottomScreen != null) {
            bottomScreen.setOnClickListener(v -> {
                if (cartItemsList == null || cartItemsList.isEmpty()) {
                    Toast.makeText(getContext(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), CartScreen.class);
                startActivity(intent);
            });
        }

        setupRecyclerView();
        loadCartFromApi();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartFromApi();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartItemsList, this::calculateTotalPrice);
        rvCart.setAdapter(cartAdapter);
    }

    private void loadCartFromApi() {
        ApiClient.getCartApi().getCart().enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartModel cart = response.body();
                    cartItemsList = cart.getItems();
                    cartAdapter.setItems(cartItemsList);
                    if (cart.getTotalAmount() > 0) {
                        tvPrice.setText(String.format(Locale.US, "%.0f", cart.getTotalAmount()));
                    } else {
                        calculateTotalPrice();
                    }
                } else if (response.code() == 401) {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Không thể tải giỏ hàng (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calculateTotalPrice() {
        double total = 0;
        if (cartItemsList != null) {
            for (CartItemModel item : cartItemsList) {
                total += item.getPrice() * item.getQuantity();
            }
        }
        if (tvPrice != null) {
            tvPrice.setText(String.format(Locale.US, "%.0f", total));
        }
    }
}
