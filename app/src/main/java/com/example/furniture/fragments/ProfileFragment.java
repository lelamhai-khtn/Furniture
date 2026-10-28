package com.example.furniture.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniture.R;
import com.example.furniture.activity.FavoriteScreen;
import com.example.furniture.activity.LocationScreen;
import com.example.furniture.activity.NotificationScreen;
import com.example.furniture.activity.OrderScreen;
import com.example.furniture.activity.PersonalScreen;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvTitle = view.findViewById(R.id.tv_title_header);
        if (tvTitle != null) {
            tvTitle.setText("Profile");
        }

        LinearLayout layoutOrders = view.findViewById(R.id.layout_orders);
        LinearLayout layoutPersonal = view.findViewById(R.id.layout_personal_info);
        LinearLayout layoutFavorite = view.findViewById(R.id.layout_favorite);
        LinearLayout layoutNotifications = view.findViewById(R.id.layout_notifications);
        LinearLayout layoutStoreLocation = view.findViewById(R.id.layout_store_location);

        if (layoutOrders != null) {
            layoutOrders.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), OrderScreen.class);
                startActivity(intent);
            });
        }

        if (layoutPersonal != null) {
            layoutPersonal.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), PersonalScreen.class);
                startActivity(intent);
            });
        }

        if (layoutFavorite != null) {
            layoutFavorite.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), FavoriteScreen.class);
                startActivity(intent);
            });
        }

        if (layoutNotifications != null) {
            layoutNotifications.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), NotificationScreen.class);
                startActivity(intent);
            });
        }

        if (layoutStoreLocation != null) {
            layoutStoreLocation.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LocationScreen.class);
                startActivity(intent);
            });
        }
    }
}
