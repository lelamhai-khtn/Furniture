package com.example.furniture.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.R;
import com.example.furniture.adapters.SearchProductsAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.ProductModel;
import com.example.furniture.popups.PopupFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView rvProduct;
    private SearchProductsAdapter productAdapter;
    private final List<ProductModel> productList = new ArrayList<>();
    private EditText etSearch;
    private ImageButton ibSearch;
    private ImageButton ib_filter;

    private View layoutCategoryChip;
    private TextView tvCategoryChipText;
    private ImageButton btnClearCategory;

    private View layoutPriceChip;
    private TextView tvPriceChipText;
    private ImageButton btnClearPrice;

    private Integer selectedCategoryId = null;
    private Double minPrice = null;
    private Double maxPrice = null;
    private String selectedCategoryName = "All";

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ib_filter = view.findViewById(R.id.ib_filter);
        ib_filter.setOnClickListener(v -> {
            PopupFilter popupFilter = new PopupFilter();
            popupFilter.show(getParentFragmentManager(), "popup_filter");
        });

        etSearch = view.findViewById(R.id.etSearch);
        ibSearch = view.findViewById(R.id.ib_search);
        rvProduct = view.findViewById(R.id.rv_search_product);

        layoutCategoryChip = view.findViewById(R.id.layoutCategoryChip);
        tvCategoryChipText = view.findViewById(R.id.tvCategoryChipText);
        btnClearCategory = view.findViewById(R.id.btnClearCategory);

        layoutPriceChip = view.findViewById(R.id.layoutPriceChip);
        tvPriceChipText = view.findViewById(R.id.tvPriceChipText);
        btnClearPrice = view.findViewById(R.id.btnClearPrice);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvProduct.setLayoutManager(gridLayoutManager);

        productAdapter = new SearchProductsAdapter(productList);
        rvProduct.setAdapter(productAdapter);

        ibSearch.setOnClickListener(v -> performSearch());
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            performSearch();
            return true;
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnClearCategory.setOnClickListener(v -> {
            selectedCategoryId = null;
            selectedCategoryName = "All";
            updateFilterChipUI();
            performSearch();
        });

        btnClearPrice.setOnClickListener(v -> {
            minPrice = null;
            maxPrice = null;
            updateFilterChipUI();
            performSearch();
        });

        getParentFragmentManager().setFragmentResultListener("filter_result", this, (requestKey, bundle) -> {
            int catId = bundle.getInt("categoryId", 0);
            String catName = bundle.getString("categoryName", "All");
            double min = bundle.getDouble("minPrice", 0);
            double max = bundle.getDouble("maxPrice", 10000);

            selectedCategoryId = (catId > 0) ? catId : null;
            selectedCategoryName = catName != null ? catName : "All";
            minPrice = (min > 0) ? min : null;
            maxPrice = (max < 10000) ? max : null;

            updateFilterChipUI();
            performSearch();
        });

        updateFilterChipUI();
        performSearch();
    }

    private void updateFilterChipUI() {
        // Category Chip
        if (selectedCategoryId != null && selectedCategoryId > 0 && !"All".equalsIgnoreCase(selectedCategoryName)) {
            layoutCategoryChip.setVisibility(View.VISIBLE);
            tvCategoryChipText.setText(selectedCategoryName);
        } else {
            layoutCategoryChip.setVisibility(View.GONE);
        }

        // Price Chip
        boolean isPriceFiltered = (minPrice != null && minPrice > 0.0) || (maxPrice != null && maxPrice < 10000.0);
        if (isPriceFiltered) {
            layoutPriceChip.setVisibility(View.VISIBLE);
            double min = minPrice != null ? minPrice : 0;
            double max = maxPrice != null ? maxPrice : 10000;
            String priceText = String.format(Locale.US, "$%.0f - $%.0f", min, max);
            tvPriceChipText.setText(priceText);
        } else {
            layoutPriceChip.setVisibility(View.GONE);
        }
    }

    private void performSearch() {
        String query = etSearch != null && etSearch.getText() != null ? etSearch.getText().toString().trim() : "";
        String name = query.isEmpty() ? null : query;

        ApiClient.getProductsApi().searchProducts(name, selectedCategoryId, minPrice, maxPrice).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Không thể tìm kiếm sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(), "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
