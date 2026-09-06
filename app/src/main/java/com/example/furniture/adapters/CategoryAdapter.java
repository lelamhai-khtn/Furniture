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
import com.example.furniture.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<CategoryModel> categories;

    public CategoryAdapter(List<CategoryModel> model)
    {
        this.categories = model;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel item = categories.get(position);
        if(item == null)
        {
            return;
        }

        holder.tv_name.setText(item.getName());

        Glide.with(holder.itemView.getContext())
                .load(item.getUrlImage())
                .placeholder(android.R.color.darker_gray)
                .into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        if(categories != null)
        {
            return categories.size();
        }
        return 0;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image;
        private TextView tv_name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_item_category);
            tv_name = itemView.findViewById(R.id.tv_item_category);
        }
    }
}
