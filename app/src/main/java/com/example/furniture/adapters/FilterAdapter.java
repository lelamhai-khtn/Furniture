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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>{
    private List<CategoryModel> categories;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public FilterAdapter(List<CategoryModel> model)
    {
        this.categories = model;
    }

    public CategoryModel getSelectedCategory() {
        if (categories != null && selectedPosition >= 0 && selectedPosition < categories.size()) {
            return categories.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new FilterAdapter.FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        CategoryModel item = categories.get(position);
        if(item == null)
        {
            return;
        }

        holder.tv_name.setText(item.getName());

        if (item.getName() != null && item.getName().equalsIgnoreCase("All")) {
            holder.iv_image.setImageResource(R.drawable.icon_all_64);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(item.getUrlImage())
                    .placeholder(android.R.color.darker_gray)
                    .into(holder.iv_image);
        }

        if (selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.card_selected_categories);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.card_unselect_categories);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                if (previousPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(previousPosition);
                }
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

    class FilterViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image;
        private TextView tv_name;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_item_category);
            tv_name = itemView.findViewById(R.id.tv_item_category);
        }
    }
}
