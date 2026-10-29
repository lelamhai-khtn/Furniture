package com.example.furniture.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.models.CartItemModel;

import java.util.List;
import java.util.Locale;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {
    private List<CartItemModel> items;

    public OrderSummaryAdapter(List<CartItemModel> items) {
        this.items = items;
    }

    public void setItems(List<CartItemModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        CartItemModel item = items.get(position);
        if (item == null) {
            return;
        }

        if (item.getImageUrl() != null && !item.getImageUrl().trim().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(holder.ivProduct);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.chair)
                    .into(holder.ivProduct);
        }

        holder.tvTitle.setText(item.getProductName() != null ? item.getProductName() : "Sản phẩm");
        holder.tvQuantity.setText("x " + item.getQuantity());

        double totalItemPrice = item.getPrice() * item.getQuantity();
        if (totalItemPrice >= 1000) {
            holder.tvPrice.setText(String.format(Locale.US, "%,.0fđ", totalItemPrice));
        } else {
            holder.tvPrice.setText(String.format(Locale.US, "$%.1f", totalItemPrice));
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class OrderSummaryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvTitle;
        TextView tvQuantity;
        TextView tvPrice;

        public OrderSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvTitle = itemView.findViewById(R.id.tv_product_title);
            tvQuantity = itemView.findViewById(R.id.tv_product_quantity);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
        }
    }
}
