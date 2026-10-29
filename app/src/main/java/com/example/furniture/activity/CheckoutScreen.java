package com.example.furniture.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.example.furniture.fragments.checkout.AddressFragment;
import com.example.furniture.fragments.checkout.CheckoutFragment;
import com.example.furniture.fragments.checkout.ReceiverFragment;

public class CheckoutScreen extends BaseActivity {
    private int currentStep = 0;
    private TextView tvStepAddress;
    private TextView tvStepReceiver;
    private TextView tvStepCheckout;
    private TextView dividerAddressReceiver;
    private TextView dividerReceiverCheckout;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_checkout_screen;
    }

    @Override
    protected void init() {
        tvStepAddress = findViewById(R.id.tv_step_address);
        tvStepReceiver = findViewById(R.id.tv_step_receiver);
        tvStepCheckout = findViewById(R.id.tv_step_checkout);
        dividerAddressReceiver = findViewById(R.id.divider_address_receiver);
        dividerReceiverCheckout = findViewById(R.id.divider_receiver_checkout);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackNavigation();
            }
        });
    }

    @Override
    protected void setTitle() {
        TextView tvTitle = findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Checkout");
        }
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> handleBackNavigation());
        }
    }

    @Override
    protected void data() {
        goToStep(0);
    }

    public void goToStep(int step) {
        this.currentStep = step;
        Fragment fragment;
        switch (step) {
            case 1:
                fragment = new ReceiverFragment();
                break;
            case 2:
                fragment = new CheckoutFragment();
                break;
            case 0:
            default:
                fragment = new AddressFragment();
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        updateStepIndicators();
    }

    private void updateStepIndicators() {
        int primaryColor = ContextCompat.getColor(this, R.color.Primary100);
        int neutralColor = ContextCompat.getColor(this, R.color.Neutral60);

        // Address (Step 0) - Active/Passed: Primary100, Active: BOLD
        if (tvStepAddress != null) {
            tvStepAddress.setTextColor(currentStep >= 0 ? primaryColor : neutralColor);
            tvStepAddress.setTypeface(null, currentStep == 0 ? Typeface.BOLD : Typeface.NORMAL);
        }

        // Divider Address - Receiver (between step 0 and 1) - Passed if step >= 1: Primary100
        if (dividerAddressReceiver != null) {
            dividerAddressReceiver.setTextColor(currentStep >= 1 ? primaryColor : neutralColor);
            dividerAddressReceiver.setTypeface(null, Typeface.NORMAL);
        }

        // Receiver (Step 1) - Active/Passed if step >= 1: Primary100, Active if step == 1: BOLD
        if (tvStepReceiver != null) {
            tvStepReceiver.setTextColor(currentStep >= 1 ? primaryColor : neutralColor);
            tvStepReceiver.setTypeface(null, currentStep == 1 ? Typeface.BOLD : Typeface.NORMAL);
        }

        // Divider Receiver - Checkout (between step 1 and 2) - Passed if step >= 2: Primary100
        if (dividerReceiverCheckout != null) {
            dividerReceiverCheckout.setTextColor(currentStep >= 2 ? primaryColor : neutralColor);
            dividerReceiverCheckout.setTypeface(null, Typeface.NORMAL);
        }

        // Checkout (Step 2) - Active if step >= 2: Primary100, Active if step == 2: BOLD
        if (tvStepCheckout != null) {
            tvStepCheckout.setTextColor(currentStep >= 2 ? primaryColor : neutralColor);
            tvStepCheckout.setTypeface(null, currentStep == 2 ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    private void handleBackNavigation() {
        if (currentStep > 0) {
            goToStep(currentStep - 1);
        } else {
            finish();
        }
    }
}
