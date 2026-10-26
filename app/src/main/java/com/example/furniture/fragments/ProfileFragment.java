package com.example.furniture.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.furniture.R;
import com.example.furniture.activity.FavoriteScreen;
import com.example.furniture.activity.NotificationScreen;
import com.example.furniture.activity.OrderScreen;
import com.example.furniture.activity.PersonalScreen;
import com.example.furniture.activity.StoreLocationScreen;
import com.example.furniture.observer.IEventListener;
import com.example.furniture.observer.ManageEvent;
import com.example.furniture.popups.PopupPhoto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout layoutOrders = view.findViewById(R.id.layout_orders);
        LinearLayout layoutPersonal = view.findViewById(R.id.layout_personal_info);
        LinearLayout layoutFavorite = view.findViewById(R.id.layout_favorite);
        LinearLayout layoutNotifications = view.findViewById(R.id.layout_notifications);
        LinearLayout layoutStoreLocation = view.findViewById(R.id.layout_store_location);

        layoutOrders.setOnClickListener(v -> {
             Intent intent = new Intent(getActivity(), OrderScreen.class);
             startActivity(intent);
        });

        layoutPersonal.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonalScreen.class);
            startActivity(intent);
        });

        layoutFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FavoriteScreen.class);
            startActivity(intent);
        });

        layoutNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationScreen.class);
            startActivity(intent);
        });

        layoutStoreLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreLocationScreen.class);
            startActivity(intent);
        });
    }
}