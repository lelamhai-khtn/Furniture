package com.example.furniture.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.activity.DetailScreen;
import com.example.furniture.models.ProductModel;

import java.util.List;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.SaleViewHolder> {
    private List<ProductModel> products;

    public SaleAdapter(List<ProductModel> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        ProductModel item = products.get(position);
        if (item == null) {
            return;
        }

        if (item.getImage() != null && !item.getImage().trim().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(holder.iv_image_sale);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.chair)
                    .into(holder.iv_image_sale);
        }

        String desc;
        if (item.getDiscountPercent() > 0) {
            if (item.getDiscountPercent() % 1 == 0) {
                desc = "Discount " + (int) item.getDiscountPercent() + "%.";
            } else {
                desc = "Discount " + item.getDiscountPercent() + "%.";
            }
        } else if (item.getDescription() != null && !item.getDescription().trim().isEmpty()) {
            desc = item.getDescription();
        } else if (item.getName() != null && !item.getName().trim().isEmpty()) {
            desc = item.getName();
        } else {
            desc = "";
        }
        holder.tv_description_sale.setText(desc);

        holder.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, DetailScreen.class);
                intent.putExtra("product_id", item.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        }
        return 0;
    }

    class SaleViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image_sale;
        private TextView tv_description_sale;
        private Button btnBuyNow;

        public SaleViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_sale = itemView.findViewById(R.id.iv_sale_image);
            tv_description_sale = itemView.findViewById(R.id.tv_sale_description);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
        }
    }
}
