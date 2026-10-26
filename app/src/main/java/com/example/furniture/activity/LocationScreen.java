package com.example.furniture.activity;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.furniture.BaseActivity;
import com.example.furniture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationScreen extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_location_screen;
    }

    @Override
    protected void init() {
        TextView tvPhone = findViewById(R.id.tv_phone);
        if (tvPhone != null) {
            tvPhone.setOnClickListener(v -> {
                String phoneNumber = tvPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            });
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    protected void setTitle() {

    }

    @Override
    protected void data() {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(10.8480542987386, 106.78664738973153);
        mMap.addMarker(new MarkerOptions().position(location).title("Store Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f));
    }
}