package com.example.furniture.adapters;

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
import com.example.furniture.models.ProductModel;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductVerticalViewHolder> {
    private List<ProductModel> products;
    public ProductsAdapter(List<ProductModel> list)
    {
        this.products = list;
    }

    @NonNull
    @Override
    public ProductVerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical, parent, false);
        return new ProductVerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVerticalViewHolder holder, int position) {
        ProductModel item = products.get(position);
        if(item == null)
        {
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .placeholder(android.R.color.darker_gray)
                .into(holder.iv_image_product);

        holder.tv_name_product.setText(item.getName());
        holder.tv_price_product.setText(String.valueOf(item.getPrice()));

    }

    @Override
    public int getItemCount() {
        if(products != null)
        {
            return products.size();
        }
        return 0;
    }

    class ProductVerticalViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView iv_image_product;
        private ImageButton ib_favorite;
        private TextView tv_name_product;
        private TextView tv_price_product;
        private ImageButton ib_cart;
        public ProductVerticalViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            ib_favorite = itemView.findViewById(R.id.ib_favorite);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            ib_cart = itemView.findViewById(R.id.ib_cart);
        }
    }
}
