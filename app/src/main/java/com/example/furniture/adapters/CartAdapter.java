package com.example.furniture.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.furniture.R;
import com.example.furniture.activity.ViewScreen;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartItemModel;
import com.example.furniture.models.CartResponse;
import com.example.furniture.models.UpdateCartQuantityRequest;
import com.example.furniture.models.UpdateCartQuantityResponse;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupDelete;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> implements IEventListener {
    private int selectedPosition = -1;
    private List<CartItemModel> items;
    private OnQuantityChangeListener quantityChangeListener;

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    public CartAdapter(List<CartItemModel> list) {
        this.items = list;
        ManageEvent.getInstance().addListener(this);
    }

    public CartAdapter(List<CartItemModel> list, OnQuantityChangeListener listener) {
        this.items = list;
        this.quantityChangeListener = listener;
        ManageEvent.getInstance().addListener(this);
    }

    public void setItems(List<CartItemModel> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CartItemModel item = items.get(position);
        if (item == null) {
            return;
        }

        if (item.getImageUrl() != null && !item.getImageUrl().trim().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.chair)
                    .error(R.drawable.chair)
                    .into(holder.iv_image_product);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.chair)
                    .into(holder.iv_image_product);
        }

        holder.tv_name_product.setText(item.getProductName() != null ? item.getProductName() : "Sản phẩm");
        holder.tv_price_product.setText("$" + String.valueOf(item.getPrice()));
        holder.tv_quantity.setText(String.valueOf(item.getQuantity()));

        View.OnClickListener viewItemClickListener = v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, ViewScreen.class);
            intent.putExtra("product_id", item.getProductId());
            context.startActivity(intent);
        };

        holder.iv_image_product.setOnClickListener(viewItemClickListener);
        holder.tv_name_product.setOnClickListener(viewItemClickListener);

        holder.btn_minus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                updateItemQuantity(holder, item, newQty);
            }
        });

        holder.btn_plus.setOnClickListener(v -> {
            int currentQty = item.getQuantity();
            int newQty = currentQty + 1;
            updateItemQuantity(holder, item, newQty);
        });

        holder.ib_delete.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            PopupDelete popup = new PopupDelete(item.getProductName(), item.getPrice(), item.getImageUrl());
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            popup.show(activity.getSupportFragmentManager(), "PopupDelete");
        });
    }

    private void updateItemQuantity(CardViewHolder holder, CartItemModel item, int newQuantity) {
        holder.btn_minus.setEnabled(false);
        holder.btn_plus.setEnabled(false);

        ApiClient.getCartApi().updateQuantity(item.getCartId(), new UpdateCartQuantityRequest(newQuantity))
                .enqueue(new Callback<UpdateCartQuantityResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateCartQuantityResponse> call, @NonNull Response<UpdateCartQuantityResponse> response) {
                        holder.btn_minus.setEnabled(true);
                        holder.btn_plus.setEnabled(true);
                        Context context = holder.itemView.getContext();

                        if (response.isSuccessful() && response.body() != null) {
                            int updatedQty = response.body().getQuantity() > 0 ? response.body().getQuantity() : newQuantity;
                            item.setQuantity(updatedQty);
                            holder.tv_quantity.setText(String.valueOf(updatedQty));
                            if (quantityChangeListener != null) {
                                quantityChangeListener.onQuantityChanged();
                            }
                        } else if (response.code() == 401) {
                            Toast.makeText(context, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cập nhật số lượng thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UpdateCartQuantityResponse> call, @NonNull Throwable t) {
                        holder.btn_minus.setEnabled(true);
                        holder.btn_plus.setEnabled(true);
                        Context context = holder.itemView.getContext();
                        Toast.makeText(context, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public void onEvent(boolean success) {
        if (success && selectedPosition >= 0 && selectedPosition < items.size()) {
            int pos = selectedPosition;
            CartItemModel item = items.get(pos);
            ApiClient.getCartApi().deleteCartItem(item.getCartId())
                    .enqueue(new Callback<CartResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<CartResponse> call, @NonNull Response<CartResponse> response) {
                            if (response.isSuccessful()) {
                                if (pos >= 0 && pos < items.size()) {
                                    items.remove(pos);
                                    notifyItemRemoved(pos);
                                    notifyItemRangeChanged(pos, items.size());
                                }
                                selectedPosition = -1;
                                if (quantityChangeListener != null) {
                                    quantityChangeListener.onQuantityChanged();
                                }
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

    class CardViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_image_product;
        private TextView tv_name_product;
        private TextView tv_price_product;
        private TextView tv_quantity;
        private Button btn_minus;
        private Button btn_plus;
        private ImageButton ib_delete;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image_product = itemView.findViewById(R.id.iv_image_product);
            tv_name_product = itemView.findViewById(R.id.tv_name_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            btn_minus = itemView.findViewById(R.id.btn_minus);
            btn_plus = itemView.findViewById(R.id.btn_plus);
            ib_delete = itemView.findViewById(R.id.ib_delete);
        }
    }
}
