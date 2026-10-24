package com.example.furniture.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.adapters.CartAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartItemModel;
import com.example.furniture.models.CartModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutScreen extends AppCompatActivity {
    private RecyclerView rvCart;
    private CartAdapter cartAdapter;
    private List<CartItemModel> cartItemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.dark(ContextCompat.getColor(this, R.color.white))
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }

        rvCart = findViewById(R.id.rv_card);
        setupRecyclerView();
        loadCartFromApi();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCart.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(cartItemsList);
        rvCart.setAdapter(cartAdapter);
    }

    private void loadCartFromApi() {
        ApiClient.getCartApi().getCart().enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(Call<CartModel> call, Response<CartModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartItemsList = response.body().getItems();
                    cartAdapter.setItems(cartItemsList);
                }
            }

            @Override
            public void onFailure(Call<CartModel> call, Throwable t) {
                // Keep default or empty
            }
        });
    }
}
