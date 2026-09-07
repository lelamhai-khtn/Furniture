package com.example.furniture.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<CategoryModel> categories;
    private int selectedPosition = 0;
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

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#808080"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            }
        });
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
