package com.example.furniture.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.furniture.models.ProductModel;
import com.example.furniture.popups.PopupDelete;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> {
    private List<ProductModel> products;
    public CartAdapter(List<ProductModel> list) {
        this.products = list;
    }
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ProductModel item = products.get(position);
        if(item == null)
        {
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.chair)
                .into(holder.iv_image_product);

        holder.tv_name_product.setText(item.getName());
        holder.tv_price_product.setText(String.valueOf(item.getPrice()));

        holder.iv_image_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailScreen.class);
                intent.putExtra("product_id", 1);
                context.startActivity(intent);
            }
        });

        holder.tv_name_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailScreen.class);
                intent.putExtra("product_id", 1);
                context.startActivity(intent);
            }
        });

        holder.ib_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupDelete popup = PopupDelete.newInstance(item.getName(), item.getPrice() + " $", item.getImage(), position);
                popup.setOnDeleteClickListener(new PopupDelete.OnDeleteClickListener() {
                    @Override
                    public void onDeleteConfirm(int itemPosition) {
                        products.remove(itemPosition);
                        notifyItemRemoved(itemPosition);
                        notifyItemRangeChanged(itemPosition, products.size());
                    }
                });

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                popup.show(activity.getSupportFragmentManager(), "PopupDelete");
            }
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

    class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image_product;
        private TextView tv_name_product;
        private TextView tv_price_product;

        private ImageButton ib_delete;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            ib_delete = itemView.findViewById(R.id.ib_delete);
        }
    }
}
