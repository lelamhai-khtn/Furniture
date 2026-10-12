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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.adapters.FavoriteAdapter;
import com.example.furniture.adapters.SearchProductsAdapter;
import com.example.furniture.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteScreen extends AppCompatActivity {
    private RecyclerView rvProduct;
    private FavoriteAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EdgeToEdge.enable(this,
                SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                SystemBarStyle.dark(ContextCompat.getColor(this, R.color.white))
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().setNavigationBarContrastEnforced(false);
        }

        rvProduct = findViewById(R.id.rv_favorite_product);
        product();
    }

    private void product() {

        List<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel(
                1,
                "iPhone 15 Pro Max",
                "Điện thoại cao cấp của Apple với chip A17 Pro, camera 48MP.",
                100,
                120,
                "https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=800&auto=format&fit=crop"
        ));

        products.add(new ProductModel(
                2,
                "Samsung Galaxy S24 Ultra",
                "Flagship của Samsung tích hợp Galaxy AI, bút S-Pen.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                3,
                "MacBook Air M2",
                "Laptop mỏng nhẹ, pin trâu, phù hợp cho dân văn phòng.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                4,
                "Tai nghe Sony WH-1000XM5",
                "Tai nghe chụp tai chống ồn chủ động tốt nhất phân khúc.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                5,
                "Bàn phím cơ Keychron K2",
                "Bàn phím cơ không dây layout 75%, switch Gateron.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(gridLayoutManager);

        productAdapter = new FavoriteAdapter(products);
        rvProduct.setAdapter(productAdapter);
    }
}