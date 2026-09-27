package com.example.furniture.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.furniture.R;
import com.example.furniture.adapters.CategoryAdapter;
import com.example.furniture.adapters.ProductsAdapter;
import com.example.furniture.models.CategoryModel;
import com.example.furniture.models.ProductModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rvCategory;
    private List<CategoryModel> categories;
    private CategoryAdapter categoryAdapter;

    private RecyclerView rvProduct;
    private RecyclerView rvBestProduct;
    private List<ProductModel> products;
    private ProductsAdapter productAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCategory = view.findViewById(R.id.rv_category);
        rvProduct = view.findViewById(R.id.rv_product);
        rvBestProduct = view.findViewById(R.id.rv_best_product);
        category();
        product();
        BestProduct();
    }
    private void category() {
        categories = new ArrayList<>();
        CategoryModel c1 = new CategoryModel("Chair", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c2 = new CategoryModel("Bed", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c3 = new CategoryModel("table", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        CategoryModel c4 = new CategoryModel("Lamp", "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo");
        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
        categories.add(c4);
        // Thiết lập LayoutManager chiều ngang (HORIZONTAL)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategory.setLayoutManager(layoutManager);

        // Khởi tạo và gắn Adapter vào RecyclerView (đảm bảo hoàn toàn KHÔNG có DividerItemDecoration ở đây)
        categoryAdapter = new CategoryAdapter(categories);
        rvCategory.setAdapter(categoryAdapter);
    }
    private void product() {

        List<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel(
                1,
                "iPhone 15 Pro Max",
                "Điện thoại cao cấp của Apple với chip A17 Pro, camera 48MP.",
                100,
                120,
                "https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=800&auto=format&fit=crop"
        ));

        products.add(new ProductModel(
                2,
                "Samsung Galaxy S24 Ultra",
                "Flagship của Samsung tích hợp Galaxy AI, bút S-Pen.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                3,
                "MacBook Air M2",
                "Laptop mỏng nhẹ, pin trâu, phù hợp cho dân văn phòng.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                4,
                "Tai nghe Sony WH-1000XM5",
                "Tai nghe chụp tai chống ồn chủ động tốt nhất phân khúc.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                5,
                "Bàn phím cơ Keychron K2",
                "Bàn phím cơ không dây layout 75%, switch Gateron.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvProduct.setLayoutManager(layoutManager);

        productAdapter = new ProductsAdapter(products);
        rvProduct.setAdapter(productAdapter);
    }

    private void BestProduct() {

        List<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel(
                1,
                "iPhone 15 Pro Max",
                "Điện thoại cao cấp của Apple với chip A17 Pro, camera 48MP.",
                100,
                120,
                "https://images.unsplash.com/photo-1595225476474-87563907a212?q=80&w=800&auto=format&fit=crop"
        ));

        products.add(new ProductModel(
                2,
                "Samsung Galaxy S24 Ultra",
                "Flagship của Samsung tích hợp Galaxy AI, bút S-Pen.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                3,
                "MacBook Air M2",
                "Laptop mỏng nhẹ, pin trâu, phù hợp cho dân văn phòng.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                4,
                "Tai nghe Sony WH-1000XM5",
                "Tai nghe chụp tai chống ồn chủ động tốt nhất phân khúc.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        products.add(new ProductModel(
                5,
                "Bàn phím cơ Keychron K2",
                "Bàn phím cơ không dây layout 75%, switch Gateron.",
                100,
                120,
                "https://drive.google.com/uc?export=view&id=1t3JeV1XONuAKGyYLZYAywFuZQ26B-iSo"
        ));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvBestProduct.setLayoutManager(layoutManager);

        productAdapter = new ProductsAdapter(products);
        rvBestProduct.setAdapter(productAdapter);
    }
}