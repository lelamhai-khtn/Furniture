package com.example.furniture.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.activity.DetailScreen;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.AddToCartRequest;
import com.example.furniture.models.CartResponse;
import com.example.furniture.models.ProductModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.SearchProductVerticalViewHolder> {
    private List<ProductModel> products;
    public SearchProductsAdapter(List<ProductModel> list)
    {
        this.products = list;
    }

    @NonNull
    @Override
    public SearchProductVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_product, parent, false);
        return new SearchProductVerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductVerticalViewHolder holder, int position) {
        ProductModel item = products.get(position);
        boolean stateFavorite = false;
        if(item == null)
        {
            return;
        }

        if (item.getImage() != null && !item.getImage().trim().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(holder.iv_image_product);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.chair)
                    .into(holder.iv_image_product);
        }

        holder.tv_name_product.setText(item.getName());

        if (item.getPrice() >= 1000) {
            holder.tv_price_product.setText(String.format(java.util.Locale.US, "%,.0fđ", item.getPrice()));
        } else {
            holder.tv_price_product.setText("$" + String.valueOf(item.getPrice()));
        }

        if (item.getOldPrice() > item.getPrice() && item.getOldPrice() > 0) {
            holder.tv_old_price_product.setVisibility(View.VISIBLE);
            holder.tv_old_price_product.setPaintFlags(holder.tv_old_price_product.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (item.getOldPrice() >= 1000) {
                holder.tv_old_price_product.setText(String.format(java.util.Locale.US, "%,.0fđ", item.getOldPrice()));
            } else {
                holder.tv_old_price_product.setText("$" + String.valueOf(item.getOldPrice()));
            }
        } else {
            holder.tv_old_price_product.setVisibility(View.GONE);
        }

        View.OnClickListener clickListener = v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailScreen.class);
            intent.putExtra("product_id", item.getId());
            context.startActivity(intent);
        };

        holder.iv_image_product.setOnClickListener(clickListener);
        holder.tv_name_product.setOnClickListener(clickListener);

        holder.ib_cart.setOnClickListener(v -> {
            int productId = item.getId();
            v.setEnabled(false);
            ApiClient.getCartApi().addToCart(new AddToCartRequest(productId, 1))
                    .enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                            v.setEnabled(true);
                            Context context = v.getContext();
                            if (response.isSuccessful() && response.body() != null) {
                                String message = response.body().getMessage();
                                Toast.makeText(context,
                                        message != null ? message : "Đã thêm vào giỏ hàng thành công",
                                        Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Toast.makeText(context, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Thêm vào giỏ hàng thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                            v.setEnabled(true);
                            Context context = v.getContext();
                            Toast.makeText(context, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        if(products != null)
        {
            return products.size();
        }
        return 0;
    }

    class SearchProductVerticalViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView iv_image_product;
        private TextView tv_name_product;
        private TextView tv_price_product;

        private TextView tv_old_price_product;
        private ImageButton ib_cart;
        public SearchProductVerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_old_price_product = itemView.findViewById(R.id.tv_old_price);
            ib_cart = itemView.findViewById(R.id.ib_cart);
        }
    }
}
