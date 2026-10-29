package com.example.furniture.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
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

public class CartScreen extends BaseActivity {
    private RecyclerView rvCart;
    private TextView tvPrice;
    private View bottomScreen;
    private TextView tvCheckout;
    private CartAdapter cartAdapter;
    private List<CartItemModel> cartItemsList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_cart_screen;
    }

    @Override
    protected void init() {
        rvCart = findViewById(R.id.rv_card);
        tvPrice = findViewById(R.id.tv_price);
        bottomScreen = findViewById(R.id.bottom_screen);
        tvCheckout = findViewById(R.id.tv_checkout);

        View.OnClickListener checkoutClickListener = v -> {
            if (cartItemsList == null || cartItemsList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, CheckoutScreen.class);
            startActivity(intent);
        };

        if (bottomScreen != null) {
            bottomScreen.setOnClickListener(checkoutClickListener);
        }

        if (tvCheckout != null) {
            tvCheckout.setOnClickListener(checkoutClickListener);
        }

        setupRecyclerView();
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("My Cart");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void data() {
        loadCartFromApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartFromApi();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartItemsList, this::calculateTotalPrice);
        rvCart.setAdapter(cartAdapter);
    }

    private void loadCartFromApi() {
        ApiClient.getCartApi().getCart().enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                if (isFinishing()) {
                    return;
                }
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
                    Toast.makeText(CartScreen.this, "Vui lòng đăng nhập để xem giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CartScreen.this, "Không thể tải giỏ hàng (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(CartScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void applyWindowInsets(View view, Insets systemBars) {
        view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
    }
}
