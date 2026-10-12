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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.activity.DetailScreen;
import com.example.furniture.models.ProductModel;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<ProductModel> products;
    public FavoriteAdapter(List<ProductModel> list)
    {
        this.products = list;
    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_product, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        ProductModel item = products.get(position);
        boolean stateFavorite = false;
        if(item == null)
        {
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.chair)
                .into(holder.iv_image_product);

        holder.tv_name_product.setText(item.getName());
        holder.tv_price_product.setText(String.valueOf(item.getPrice()));
        holder.tv_old_price_product.setPaintFlags(holder.tv_old_price_product.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.ib_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stateFavorite) {
                    holder.ib_favorite.setImageResource(R.drawable.icon_favorite_fill_24);
                } else {
                    holder.ib_favorite.setImageResource(R.drawable.icon_favorite_24);
                }
            }
        });

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
    }

    @Override
    public int getItemCount() {
        if(products != null)
        {
            return products.size();
        }
        return 0;
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_image_product;
        TextView tv_name_product;
        TextView tv_price_product;
        TextView tv_old_price_product;
        ImageButton ib_favorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_old_price_product = itemView.findViewById(R.id.tv_old_price);
            ib_favorite = itemView.findViewById(R.id.ib_favorite);
        }

    }
}
