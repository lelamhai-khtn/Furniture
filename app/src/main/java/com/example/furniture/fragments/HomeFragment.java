package com.example.furniture.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.furniture.R;
import com.example.furniture.adapters.CategoryAdapter;
import com.example.furniture.adapters.ProductsAdapter;
import com.example.furniture.adapters.SaleAdapter;
import com.example.furniture.api.ApiClient;
import com.example.furniture.models.CategoryModel;
import com.example.furniture.models.ProductModel;
import com.example.furniture.models.AuthResponse;
import com.example.furniture.utils.AuthSessionStore;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView rvCategory;
    private List<CategoryModel> categories;
    private CategoryAdapter categoryAdapter;

    private RecyclerView rvProduct;
    private RecyclerView rvNewestProduct;
    private RecyclerView rvSale;

    private List<ProductModel> products;
    private ProductsAdapter productAdapter;
    private SaleAdapter saleAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSale = view.findViewById(R.id.rv_sale);
        rvCategory = view.findViewById(R.id.rv_category);
        rvProduct = view.findViewById(R.id.rv_product);
        rvNewestProduct = view.findViewById(R.id.rv_newest_product);
        bindUserInfo(view);

        sale();
        product();
        category();
        newestProduct();
    }

    private void bindUserInfo(View view) {
        ImageView avatarView = view.findViewById(R.id.iv_avatar);
        TextView usernameView = view.findViewById(R.id.tv_username);

        AuthResponse session = new AuthSessionStore(requireContext()).get();
        if (session == null || session.getUser() == null) {
            return;
        }

        AuthResponse.User user = session.getUser();
        if (user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
            usernameView.setText(user.getFullName());
        }
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.trim().isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .circleCrop()
                    .into(avatarView);
        }
    }

    private void sale() {
        List<ProductModel> saleProducts = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvSale.setLayoutManager(layoutManager);
        if (rvSale.getOnFlingListener() == null) {
            new PagerSnapHelper().attachToRecyclerView(rvSale);
        }

        saleAdapter = new SaleAdapter(saleProducts);
        rvSale.setAdapter(saleAdapter);

        ApiClient.getProductsApi().getTopDiscounts(3).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call,
                                   @NonNull Response<List<ProductModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    saleProducts.clear();
                    saleProducts.addAll(response.body());
                    saleAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tải sản phẩm khuyến mãi",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(),
                        "Không thể kết nối để tải sản phẩm khuyến mãi",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void category() {
        categories = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(categories);
        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(CategoryModel categoryModel) {
                if (categoryModel != null) {
                    loadProductsByCategory(categoryModel.getCategoryId());
                }
            }
        });
        rvCategory.setAdapter(categoryAdapter);

        ApiClient.getCategoriesApi().getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryModel>> call,
                                   @NonNull Response<List<CategoryModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();

                    if (!categories.isEmpty()) {
                        int selectedPos = categoryAdapter.getSelectedPosition();
                        if (selectedPos >= 0 && selectedPos < categories.size()) {
                            loadProductsByCategory(categories.get(selectedPos).getCategoryId());
                        } else {
                            loadProductsByCategory(categories.get(0).getCategoryId());
                        }
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tải danh mục sản phẩm",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(),
                        "Không thể kết nối để tải danh mục",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void product() {
        products = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvProduct.setLayoutManager(layoutManager);

        productAdapter = new ProductsAdapter(products);
        rvProduct.setAdapter(productAdapter);
    }

    private void loadProductsByCategory(int categoryId) {
        ApiClient.getProductsApi().getProductsByCategory(categoryId).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call,
                                   @NonNull Response<List<ProductModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    products.clear();
                    products.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tải sản phẩm",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(),
                        "Không thể kết nối để tải sản phẩm",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void newestProduct() {
        List<ProductModel> newestProducts = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvNewestProduct.setLayoutManager(layoutManager);

        ProductsAdapter newestProductAdapter = new ProductsAdapter(newestProducts);
        rvNewestProduct.setAdapter(newestProductAdapter);

        ApiClient.getProductsApi().getNewestProducts(5).enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call,
                                   @NonNull Response<List<ProductModel>> response) {
                if (!isAdded()) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    newestProducts.clear();
                    newestProducts.addAll(response.body());
                    newestProductAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tải sản phẩm mới nhất",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                if (!isAdded()) {
                    return;
                }
                Toast.makeText(requireContext(),
                        "Không thể kết nối để tải sản phẩm mới nhất",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
