package com.example.furniture.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;

import com.bumptech.glide.Glide;
import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.AddToCartRequest;
import com.example.furniture.models.CartResponse;
import com.example.furniture.models.ProductModel;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailScreen extends BaseActivity {

    private ImageView imageView;
    private TextView tvNameProduct;
    private TextView tvPriceProduct;
    private TextView tvOldPriceProduct;
    private TextView tvDescriptionProduct;
    private TextView tvCategoryProduct;
    private TextView tvColorProduct;
    private TextView tvMaterialProduct;
    private TextView tvStockProduct;
    private TextView tvDimensionsProduct;
    private ImageButton ibAddCard;
    private LinearLayout btnCheckout;

    private int productId = -1;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_detail_screen;
    }

    @Override
    protected void init() {
        imageView = findViewById(R.id.imageView);
        tvNameProduct = findViewById(R.id.tv_name_product);
        tvPriceProduct = findViewById(R.id.tv_price_product);
        tvOldPriceProduct = findViewById(R.id.tv_old_price_product);
        tvDescriptionProduct = findViewById(R.id.tv_description_product);
        tvCategoryProduct = findViewById(R.id.tv_category_product);
        tvColorProduct = findViewById(R.id.tv_color_product);
        tvMaterialProduct = findViewById(R.id.tv_material_product);
        tvStockProduct = findViewById(R.id.tv_stock_product);
        tvDimensionsProduct = findViewById(R.id.tv_dimensions_product);
        ibAddCard = findViewById(R.id.ib_add_card);
        btnCheckout = findViewById(R.id.btn_checkout);

        if (getIntent() != null) {
            productId = getIntent().getIntExtra("product_id", getIntent().getIntExtra("productId", -1));
        }

        if (btnCheckout != null) {
            btnCheckout.setOnClickListener(v -> {
                Intent intent = new Intent(this, CheckoutScreen.class);
                startActivity(intent);
            });
        }

        if (ibAddCard != null) {
            ibAddCard.setOnClickListener(v -> {
                if (productId > 0) {
                    ibAddCard.setEnabled(false);
                    ApiClient.getCartApi().addToCart(new AddToCartRequest(productId, 1))
                            .enqueue(new Callback<CartResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                                    ibAddCard.setEnabled(true);
                                    if (response.isSuccessful() && response.body() != null) {
                                        String msg = response.body().getMessage();
                                        Toast.makeText(DetailScreen.this, msg != null ? msg : "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 401) {
                                        Toast.makeText(DetailScreen.this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(DetailScreen.this, "Thêm vào giỏ hàng thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                                    ibAddCard.setEnabled(true);
                                    Toast.makeText(DetailScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Detail");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void data() {
        if (productId > 0) {
            ApiClient.getProductsApi().getProductById(productId).enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                    if (!isFinishing() && response.isSuccessful() && response.body() != null) {
                        bindProductData(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
                    if (!isFinishing()) {
                        Toast.makeText(DetailScreen.this, "Không thể tải chi tiết sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void bindProductData(ProductModel product) {
        if (product == null) return;

        if (tvNameProduct != null) {
            tvNameProduct.setText(product.getName() != null ? product.getName() : "");
        }

        if (tvDescriptionProduct != null) {
            tvDescriptionProduct.setText(product.getDescription() != null ? product.getDescription() : "");
        }

        if (tvCategoryProduct != null) {
            tvCategoryProduct.setText("Category: " + (product.getCategoryName() != null ? product.getCategoryName() : "N/A"));
        }

        if (tvColorProduct != null) {
            tvColorProduct.setText("Color: " + (product.getColor() != null ? product.getColor() : "N/A"));
        }

        if (tvMaterialProduct != null) {
            String base = product.getBaseMaterial() != null ? product.getBaseMaterial() : "";
            String uph = product.getUpholsteryMaterial() != null ? product.getUpholsteryMaterial() : "";
            String material = base;
            if (!uph.isEmpty()) {
                material = material.isEmpty() ? uph : material + " / " + uph;
            }
            tvMaterialProduct.setText("Material: " + (material.isEmpty() ? "N/A" : material));
        }

        if (tvStockProduct != null) {
            tvStockProduct.setText("Stock: " + product.getStockQuantity() + " available");
        }

        if (tvDimensionsProduct != null) {
            if (product.getDimensions() != null) {
                ProductModel.ProductDimensions dim = product.getDimensions();
                String dimText = String.format(Locale.US, "Dimensions: %.0f x %.0f x %.0f cm (%.1f kg)",
                        dim.getLengthCm(), dim.getWidthCm(), dim.getHeightCm(), dim.getWeightKg());
                tvDimensionsProduct.setText(dimText);
            } else {
                tvDimensionsProduct.setText("Dimensions: N/A");
            }
        }

        double price = product.getPrice();
        double oldPrice = product.getOldPrice();

        if (tvPriceProduct != null) {
            if (price >= 1000) {
                tvPriceProduct.setText(String.format(Locale.US, "%,.0fđ", price));
            } else {
                tvPriceProduct.setText(String.format(Locale.US, "$%.0f", price));
            }
        }

        if (tvOldPriceProduct != null) {
            if (oldPrice > price && oldPrice > 0) {
                tvOldPriceProduct.setVisibility(View.VISIBLE);
                tvOldPriceProduct.setPaintFlags(tvOldPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                if (oldPrice >= 1000) {
                    tvOldPriceProduct.setText(String.format(Locale.US, "%,.0fđ", oldPrice));
                } else {
                    tvOldPriceProduct.setText(String.format(Locale.US, "$%.0f", oldPrice));
                }
            } else {
                tvOldPriceProduct.setVisibility(View.GONE);
            }
        }

        if (imageView != null && product.getImage() != null && !product.getImage().trim().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(imageView);
        }

        ImageButton ibFavorite = findViewById(R.id.ib_favorite);
        if (ibFavorite != null) {
            boolean[] isFav = {product.isWishlisted()};
            updateFavoriteIcon(ibFavorite, isFav[0]);
            ibFavorite.setOnClickListener(v -> {
                if (productId > 0) {
                    ibFavorite.setEnabled(false);
                    ApiClient.getWishlistApi().toggleWishlist(productId).enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                            ibFavorite.setEnabled(true);
                            if (response.isSuccessful()) {
                                isFav[0] = !isFav[0];
                                updateFavoriteIcon(ibFavorite, isFav[0]);
                                String msg = response.body() != null && response.body().getMessage() != null ? response.body().getMessage() : (isFav[0] ? "Đã thêm vào yêu thích" : "Đã xóa khỏi yêu thích");
                                Toast.makeText(DetailScreen.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(DetailScreen.this, "Vui lòng đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DetailScreen.this, "Thực hiện thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                            ibFavorite.setEnabled(true);
                            Toast.makeText(DetailScreen.this, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    private void updateFavoriteIcon(ImageButton ib, boolean isFav) {
        if (isFav) {
            ib.setImageResource(R.drawable.icon_favorite_fill_24);
        } else {
            ib.setImageResource(R.drawable.icon_favorite_24);
        }
    }

    @Override
    protected void applyWindowInsets(View view, Insets systemBars) {
        view.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
    }
}
