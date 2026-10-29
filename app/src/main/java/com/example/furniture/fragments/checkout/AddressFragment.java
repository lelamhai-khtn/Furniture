package com.example.furniture.fragments.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniture.R;
import com.example.furniture.activity.CheckoutScreen;

public class AddressFragment extends Fragment {

    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnNext = view.findViewById(R.id.btn_next_b);
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (getActivity() instanceof CheckoutScreen) {
                    ((CheckoutScreen) getActivity()).goToStep(1);
                }
            });
        }
    }
}
