package com.example.furniture.fragments.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.furniture.R;
import com.example.furniture.activity.CheckoutScreen;
import com.example.furniture.api.ApiClient;
import android.location.Address;
import android.location.Geocoder;
import com.example.furniture.models.DistanceResponse;
import com.example.furniture.models.UpdateProfileRequest;
import com.example.furniture.models.UpdateProfileResponse;
import com.example.furniture.models.UserProfileResponse;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiverFragment extends Fragment {
    private EditText etAddress;
    private EditText etFullName;
    private EditText etPhone;
    private AutoCompleteTextView tvGender;

    public ReceiverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etFullName = view.findViewById(R.id.etFullName);
        etPhone = view.findViewById(R.id.etPhone);
        tvGender = view.findViewById(R.id.tvGender);
        etAddress = view.findViewById(R.id.etAddress);

        if (getActivity() instanceof CheckoutScreen) {
            CheckoutScreen activity = (CheckoutScreen) getActivity();
            UserProfileResponse profile = activity.getUserProfile();
            if (profile != null) {
                if (etFullName != null) {
                    etFullName.setText(profile.getFullName() != null ? profile.getFullName() : "");
                }
                if (etPhone != null) {
                    etPhone.setText(profile.getPhone() != null ? profile.getPhone() : "");
                }
                if (tvGender != null) {
                    tvGender.setText(profile.getGender() != null ? profile.getGender() : "", false);
                }
            }

            String address = activity.getSelectedAddress();
            if (etAddress != null && address != null && !address.trim().isEmpty()) {
                etAddress.setText(address);
            } else if (profile != null && profile.getAddress() != null) {
                etAddress.setText(profile.getAddress());
            }
        }

        Button btnSave = view.findViewById(R.id.btn_save);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> {
                String fullName = etFullName != null ? etFullName.getText().toString().trim() : "";
                String phone = etPhone != null ? etPhone.getText().toString().trim() : "";
                String gender = tvGender != null ? tvGender.getText().toString().trim() : "";
                String address = etAddress != null ? etAddress.getText().toString().trim() : "";

                UpdateProfileRequest request = new UpdateProfileRequest(fullName, phone, gender, address);
                btnSave.setEnabled(false);
                Toast.makeText(getContext(), "Updating information...", Toast.LENGTH_SHORT).show();

                ApiClient.getUserApi().updateProfile(request).enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                        if (!isAdded()) {
                            return;
                        }
                        btnSave.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            UpdateProfileResponse res = response.body();
                            String msg = res.getMessage() != null ? res.getMessage() : "Information updated successfully";
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(getContext(), "Please login again", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Update failed (" + response.code() + ")", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
                        if (!isAdded()) {
                            return;
                        }
                        btnSave.setEnabled(true);
                        Toast.makeText(getContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        Button btnNext = view.findViewById(R.id.btn_next_c);
        if (btnNext != null) {
            btnNext.setOnClickListener(v -> {
                if (getActivity() instanceof CheckoutScreen) {
                    CheckoutScreen activity = (CheckoutScreen) getActivity();
                    String fullName = etFullName != null ? etFullName.getText().toString().trim() : "";
                    String phone = etPhone != null ? etPhone.getText().toString().trim() : "";
                    String gender = tvGender != null ? tvGender.getText().toString().trim() : "";
                    String address = etAddress != null ? etAddress.getText().toString().trim() : "";

                    btnNext.setEnabled(false);
                    Toast.makeText(getContext(), "Calculating distance & delivery fee...", Toast.LENGTH_SHORT).show();

                    new Thread(() -> {
                        double lat = 10.7769; // default HCMC
                        double lng = 106.7009;
                        if (!address.isEmpty() && Geocoder.isPresent()) {
                            try {
                                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    Address loc = addresses.get(0);
                                    lat = loc.getLatitude();
                                    lng = loc.getLongitude();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        double finalLat = lat;
                        double finalLng = lng;
                        if (isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                ApiClient.getDistanceApi().getDistance(finalLat, finalLng).enqueue(new Callback<DistanceResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {
                                        if (!isAdded()) return;
                                        btnNext.setEnabled(true);
                                        double fee = 15.0;
                                        if (response.isSuccessful() && response.body() != null) {
                                            fee = response.body().getDeliveryFee();
                                        }
                                        activity.setReceiverInfo(fullName, phone, gender, address);
                                        activity.setDeliveryFee(fee);
                                        activity.goToStep(2);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<DistanceResponse> call, @NonNull Throwable t) {
                                        if (!isAdded()) return;
                                        btnNext.setEnabled(true);
                                        activity.setReceiverInfo(fullName, phone, gender, address);
                                        activity.setDeliveryFee(15.0);
                                        activity.goToStep(2);
                                    }
                                });
                            });
                        }
                    }).start();
                }
            });
        }
    }
}
