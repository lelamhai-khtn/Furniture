package com.example.furniture.fragments.checkout;

import android.content.Intent;
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
import com.example.furniture.activity.SepayScreen;
import com.example.furniture.adapters.OrderSummaryAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CartItemModel;
import com.example.furniture.models.CartModel;
import com.example.furniture.models.CreateOrderRequest;
import com.example.furniture.models.CreateOrderResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private double deliveryFee = 15.0;

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
            deliveryFee = activity.getDeliveryFee();

            String phone = activity.getReceiverPhone();
            if ((phone == null || phone.trim().isEmpty()) && activity.getUserProfile() != null) {
                phone = activity.getUserProfile().getPhone();
            }
            if (tvPhone != null && phone != null && !phone.trim().isEmpty()) {
                tvPhone.setText(phone);
            }

            String address = activity.getReceiverAddress();
            if (address == null || address.trim().isEmpty()) {
                address = activity.getSelectedAddress();
            }
            if ((address == null || address.trim().isEmpty()) && activity.getUserProfile() != null) {
                address = activity.getUserProfile().getAddress();
            }
            if (tvAddress != null && address != null && !address.trim().isEmpty()) {
                tvAddress.setText(address);
            }
        }

        setupPaymentSelection();
        setupRecyclerView();
        loadCartFromApi();

        if (layoutPlaceOrder != null) {
            layoutPlaceOrder.setOnClickListener(v -> createOrderApi());
        }
    }

    private void createOrderApi() {
        if (cartItemsList == null || cartItemsList.isEmpty()) {
            Toast.makeText(getContext(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> cartIds = new ArrayList<>();
        for (CartItemModel item : cartItemsList) {
            if (item != null && item.getCartId() > 0) {
                cartIds.add(item.getCartId());
            }
        }

        String shippingAddress = "";
        if (getActivity() instanceof CheckoutScreen) {
            CheckoutScreen activity = (CheckoutScreen) getActivity();
            shippingAddress = activity.getReceiverAddress();
            if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
                shippingAddress = activity.getSelectedAddress();
            }
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            shippingAddress = tvAddress != null ? tvAddress.getText().toString().trim() : "";
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            shippingAddress = "97 Man Thiện, Phường Hiệp Phú, Thành phố Thủ Đức, TP. Hồ Chí Minh";
        }

        String paymentGateway = "COD";
        if (rbSepay != null && rbSepay.isChecked()) {
            paymentGateway = "Sepay";
        } else if (rbCod != null && rbCod.isChecked()) {
            paymentGateway = "COD";
        }

        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String deliveryTimeSlot = "Buổi chiều (14:00 - 17:00)";

        CreateOrderRequest request = new CreateOrderRequest(cartIds, shippingAddress, paymentGateway, deliveryDate, deliveryTimeSlot);

        if (layoutPlaceOrder != null) {
            layoutPlaceOrder.setEnabled(false);
        }
        Toast.makeText(getContext(), "Đang tạo đơn hàng...", Toast.LENGTH_SHORT).show();

        ApiClient.getOrderApi().createOrder(request).enqueue(new Callback<CreateOrderResponse>() {
            @Override
            public void onResponse(@NonNull Call<CreateOrderResponse> call, @NonNull Response<CreateOrderResponse> response) {
                if (!isAdded()) {
                    return;
                }
                if (layoutPlaceOrder != null) {
                    layoutPlaceOrder.setEnabled(true);
                }

                if (response.isSuccessful() && response.body() != null) {
                    CreateOrderResponse res = response.body();
                    String msg = res.getMessage() != null ? res.getMessage() : "Tạo đơn hàng thành công";
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();

                    if (res.getTotalAmount() > 0) {
                        if (tvTotalPayment != null) {
                            tvTotalPayment.setText(String.format(Locale.US, "$%,.2f", res.getTotalAmount()));
                        }
                        if (tvTotalBottom != null) {
                            tvTotalBottom.setText(String.format(Locale.US, "$%,.2f", res.getTotalAmount()));
                        }
                    }
                    if (tvAddress != null && res.getShippingAddress() != null && !res.getShippingAddress().trim().isEmpty()) {
                        tvAddress.setText(res.getShippingAddress());
                    }

                    if (res.getPayment() != null && "Sepay".equalsIgnoreCase(res.getPayment().getPaymentGateway())) {
                        Intent intent = new Intent(requireContext(), SepayScreen.class);
                        intent.putExtra("orderId", res.getOrderId());
                        intent.putExtra("totalAmount", res.getTotalAmount());
                        intent.putExtra("qrCodeUrl", res.getPayment().getQrCodeUrl());
                        intent.putExtra("bank", res.getPayment().getBank());
                        intent.putExtra("accountNumber", res.getPayment().getAccountNumber());
                        intent.putExtra("transferContent", res.getPayment().getTransferContent());
                        startActivity(intent);
                        requireActivity().finish();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Tạo đơn hàng thất bại (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CreateOrderResponse> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                if (layoutPlaceOrder != null) {
                    layoutPlaceOrder.setEnabled(true);
                }
                Toast.makeText(getContext(), "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
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
