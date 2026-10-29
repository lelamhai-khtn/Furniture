package com.example.furniture.fragments.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;
import com.example.furniture.activity.CheckoutScreen;
import com.example.furniture.adapters.OrderSummaryAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartItemModel;
import com.example.furniture.models.CartModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends Fragment {
    private TextView tvAddress;
    private TextView tvPhone;
    private RecyclerView rvOrderSummary;
    private RadioButton rbSepay;
    private RadioButton rbCod;
    private LinearLayout layoutSepay;
    private LinearLayout layoutCod;
    private TextView tvSubtotal;
    private TextView tvDeliveryFee;
    private TextView tvTotalPayment;
    private TextView tvTotalBottom;
    private View layoutPlaceOrder;

    private OrderSummaryAdapter orderSummaryAdapter;
    private final List<CartItemModel> cartItemsList = new ArrayList<>();
    private final double deliveryFee = 15.0;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAddress = view.findViewById(R.id.tv_address);
        tvPhone = view.findViewById(R.id.tv_phone);
        rvOrderSummary = view.findViewById(R.id.rv_order_summary);
        rbSepay = view.findViewById(R.id.rb_sepay);
        rbCod = view.findViewById(R.id.rb_cod);
        layoutSepay = view.findViewById(R.id.layout_sepay);
        layoutCod = view.findViewById(R.id.layout_cod);
        tvSubtotal = view.findViewById(R.id.tv_subtotal);
        tvDeliveryFee = view.findViewById(R.id.tv_delivery_fee);
        tvTotalPayment = view.findViewById(R.id.tv_total_payment);
        tvTotalBottom = view.findViewById(R.id.tv_total_bottom);
        layoutPlaceOrder = view.findViewById(R.id.layout_place_order);

        if (getActivity() instanceof CheckoutScreen) {
            CheckoutScreen activity = (CheckoutScreen) getActivity();
            String address = activity.getSelectedAddress();
            if (tvAddress != null && address != null && !address.trim().isEmpty()) {
                tvAddress.setText(address);
            } else if (activity.getUserProfile() != null && activity.getUserProfile().getAddress() != null) {
                tvAddress.setText(activity.getUserProfile().getAddress());
            }

            if (tvPhone != null && activity.getUserProfile() != null && activity.getUserProfile().getPhone() != null) {
                tvPhone.setText(activity.getUserProfile().getPhone());
            }
        }

        setupPaymentSelection();
        setupRecyclerView();
        loadCartFromApi();

        if (layoutPlaceOrder != null) {
            layoutPlaceOrder.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setupPaymentSelection() {
        View.OnClickListener selectSepay = v -> {
            if (rbSepay != null) rbSepay.setChecked(true);
            if (rbCod != null) rbCod.setChecked(false);
        };

        View.OnClickListener selectCod = v -> {
            if (rbSepay != null) rbSepay.setChecked(false);
            if (rbCod != null) rbCod.setChecked(true);
        };

        if (rbSepay != null) rbSepay.setOnClickListener(selectSepay);
        if (layoutSepay != null) layoutSepay.setOnClickListener(selectSepay);

        if (rbCod != null) rbCod.setOnClickListener(selectCod);
        if (layoutCod != null) layoutCod.setOnClickListener(selectCod);
    }

    private void setupRecyclerView() {
        if (rvOrderSummary != null) {
            rvOrderSummary.setLayoutManager(new LinearLayoutManager(getContext()));
            orderSummaryAdapter = new OrderSummaryAdapter(cartItemsList);
            rvOrderSummary.setAdapter(orderSummaryAdapter);
        }
    }

    private void loadCartFromApi() {
        ApiClient.getCartApi().getCart().enqueue(new Callback<CartModel>() {
            @Override
            public void onResponse(@NonNull Call<CartModel> call, @NonNull Response<CartModel> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    CartModel cart = response.body();
                    cartItemsList.clear();
                    cartItemsList.addAll(cart.getItems());
                    if (orderSummaryAdapter != null) {
                        orderSummaryAdapter.notifyDataSetChanged();
                    }
                    calculateTotals(cart.getTotalAmount());
                } else {
                    calculateTotals(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartModel> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                calculateTotals(0);
            }
        });
    }

    private void calculateTotals(double serverSubtotal) {
        double subtotal = serverSubtotal;
        if (subtotal <= 0 && cartItemsList != null) {
            for (CartItemModel item : cartItemsList) {
                subtotal += item.getSubTotal();
            }
        }

        double total = subtotal + deliveryFee;

        if (tvSubtotal != null) {
            tvSubtotal.setText(String.format(Locale.US, "$%,.2f", subtotal));
        }
        if (tvDeliveryFee != null) {
            tvDeliveryFee.setText(String.format(Locale.US, "$%,.2f", deliveryFee));
        }
        if (tvTotalPayment != null) {
            tvTotalPayment.setText(String.format(Locale.US, "$%,.2f", total));
        }
        if (tvTotalBottom != null) {
            tvTotalBottom.setText(String.format(Locale.US, "$%,.2f", total));
        }
    }
}
