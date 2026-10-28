package com.example.furniture.activity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.adapters.FavoriteAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.ProductModel;
import com.example.furniture.models.WishlistResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteScreen extends BaseActivity {
    private RecyclerView rvProduct;
    private FavoriteAdapter productAdapter;
    private final List<ProductModel> productsList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_favorite_screen;
    }

    @Override
    protected void init() {
        rvProduct = findViewById(R.id.rv_favorite_product);
        setupRecyclerView();
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Favorite");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void data() {
        loadFavoriteProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteProducts();
    }

    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(gridLayoutManager);
        productAdapter = new FavoriteAdapter(productsList);
        rvProduct.setAdapter(productAdapter);
    }

    private void loadFavoriteProducts() {
        ApiClient.getWishlistApi().getWishlistProducts().enqueue(new Callback<WishlistResponse>() {
            @Override
            public void onResponse(@NonNull Call<WishlistResponse> call, @NonNull Response<WishlistResponse> response) {
                if (isFinishing()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body().getProductModels());
                    productAdapter.notifyDataSetChanged();
                } else if (response.code() == 401) {
                    Toast.makeText(FavoriteScreen.this, "Vui lòng đăng nhập để xem sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FavoriteScreen.this, "Không thể tải danh sách yêu thích (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<WishlistResponse> call, @NonNull Throwable t) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(FavoriteScreen.this, "Không thể kết nicotine đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void applyWindowInsets(View view, Insets systemBars) {
        view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
    }
}
