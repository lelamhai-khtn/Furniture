package com.example.furniture.fragments.checkout;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniture.R;
import com.example.furniture.activity.CheckoutScreen;
import com.example.furniture.models.UserProfileResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText etSearchLocation;
    private ImageButton btnClearSearch;
    private ImageView ivSearch;

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

        etSearchLocation = view.findViewById(R.id.et_search_location);
        btnClearSearch = view.findViewById(R.id.btn_clear_search);
        ivSearch = view.findViewById(R.id.iv_search);
        Button btnNext = view.findViewById(R.id.btn_next_b);

        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (getActivity() instanceof CheckoutScreen) {
                    String address = etSearchLocation != null ? etSearchLocation.getText().toString() : "";
                    ((CheckoutScreen) getActivity()).setSelectedAddress(address);
                    ((CheckoutScreen) getActivity()).goToStep(1);
                }
            });
        }

        if (ivSearch != null) {
            ivSearch.setOnClickListener(v -> {
                if (etSearchLocation != null) {
                    searchLocation(etSearchLocation.getText().toString());
                }
            });
        }

        if (etSearchLocation != null) {
            etSearchLocation.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    searchLocation(etSearchLocation.getText().toString());
                    return true;
                }
                return false;
            });

            etSearchLocation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (btnClearSearch != null) {
                        btnClearSearch.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        if (btnClearSearch != null) {
            btnClearSearch.setOnClickListener(v -> {
                if (etSearchLocation != null) {
                    etSearchLocation.setText("");
                }
            });
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(10.8480542987386, 106.78664738973153);

        boolean hasProfileAddress = false;
        if (getActivity() instanceof CheckoutScreen) {
            CheckoutScreen activity = (CheckoutScreen) getActivity();
            UserProfileResponse profile = activity.getUserProfile();
            if (profile != null && profile.getAddress() != null && !profile.getAddress().trim().isEmpty()) {
                hasProfileAddress = true;
                if (etSearchLocation != null) {
                    etSearchLocation.setText(profile.getAddress());
                }
                searchLocation(profile.getAddress());
            }
        }

        if (!hasProfileAddress) {
            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Store Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f));
        }

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    String addressStr = address.getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(addressStr));
                    if (etSearchLocation != null) {
                        etSearchLocation.setText(addressStr);
                    }
                } else {
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                    if (etSearchLocation != null) {
                        etSearchLocation.setText(latLng.latitude + ", " + latLng.longitude);
                    }
                }
            } catch (IOException e) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            }
        });
    }

    private void searchLocation(String locationName) {
        if (locationName == null || locationName.trim().isEmpty()) {
            return;
        }

        Geocoder geocoder = new Geocoder(requireContext(), new Locale("vi", "VN"));
        try {
            String query = locationName.trim();
            String lower = query.toLowerCase();
            if (!lower.contains("vietnam") && !lower.contains("việt nam") && !lower.contains("hồ chí minh") && !lower.contains("hà nội") && !lower.contains("tp.")) {
                query = query + ", Hồ Chí Minh, Việt Nam";
            }
            List<Address> addressList = geocoder.getFromLocationName(query, 1);
            if ((addressList == null || addressList.isEmpty()) && !query.equals(locationName.trim())) {
                addressList = geocoder.getFromLocationName(locationName.trim(), 1);
            }

            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (mMap != null) {
                    mMap.clear();
                    String title = address.getAddressLine(0) != null ? address.getAddressLine(0) : locationName;
                    mMap.addMarker(new MarkerOptions().position(latLng).title(title));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                    if (etSearchLocation != null) {
                        etSearchLocation.setText(title);
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Unable to search location", Toast.LENGTH_SHORT).show();
        }
    }
}
