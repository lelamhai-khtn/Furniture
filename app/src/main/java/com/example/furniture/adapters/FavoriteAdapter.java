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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.activity.DetailScreen;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartResponse;
import com.example.furniture.models.ProductModel;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupFavoriteDelete;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> implements IEventListener {
    private int selectedPosition = -1;
    private List<ProductModel> products;

    public FavoriteAdapter(List<ProductModel> list) {
        this.products = list;
        ManageEvent.getInstance().addListener(this);
    }

    public void setItems(List<ProductModel> list) {
        this.products = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        ProductModel item = products.get(position);
        if (item == null) {
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

        holder.tv_name_product.setText(item.getName() != null ? item.getName() : "Sản phẩm");

        if (item.getPrice() >= 1000) {
            holder.tv_price_product.setText(String.format(java.util.Locale.US, "%,.0fđ", item.getPrice()));
        } else {
            holder.tv_price_product.setText("$" + String.valueOf(item.getPrice()));
        }

        if (holder.tv_old_price != null) {
            if (item.getOldPrice() > item.getPrice() && item.getOldPrice() > 0) {
                holder.tv_old_price.setVisibility(View.VISIBLE);
                holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                if (item.getOldPrice() >= 1000) {
                    holder.tv_old_price.setText(String.format(java.util.Locale.US, "%,.0fđ", item.getOldPrice()));
                } else {
                    holder.tv_old_price.setText("$" + String.valueOf(item.getOldPrice()));
                }
            } else {
                holder.tv_old_price.setVisibility(View.GONE);
            }
        }

        View.OnClickListener clickListener = v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, DetailScreen.class);
            intent.putExtra("product_id", item.getId());
            context.startActivity(intent);
        };

        holder.iv_image_product.setOnClickListener(clickListener);
        holder.tv_name_product.setOnClickListener(clickListener);

        if (holder.ib_delete != null) {
            holder.ib_delete.setOnClickListener(v -> {
                selectedPosition = holder.getAbsoluteAdapterPosition();
                PopupFavoriteDelete popup = new PopupFavoriteDelete(item.getName(), item.getPrice(), item.getImage());
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                popup.show(activity.getSupportFragmentManager(), "PopupFavoriteDelete");
            });
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    @Override
    public void onEvent(boolean success) {
        if (success && selectedPosition >= 0 && selectedPosition < products.size()) {
            int pos = selectedPosition;
            ProductModel item = products.get(pos);
            ApiClient.getWishlistApi().toggleWishlist(item.getId())
                    .enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                            if (response.isSuccessful()) {
                                if (pos >= 0 && pos < products.size()) {
                                    products.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, products.size());
                                }
                                selectedPosition = -1;
                            } else {
                                selectedPosition = -1;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<CartResponse> call, @NonNull Throwable t) {
                            selectedPosition = -1;
                        }
                    });
        } else {
            selectedPosition = -1;
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        ManageEvent.getInstance().removeListener(this);
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image_product;
        private TextView tv_name_product;
        private TextView tv_price_product;
        private TextView tv_old_price;
        private ImageButton ib_delete;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_old_price = itemView.findViewById(R.id.tv_old_price);
            ib_delete = itemView.findViewById(R.id.ib_delete);
        }
    }
}
