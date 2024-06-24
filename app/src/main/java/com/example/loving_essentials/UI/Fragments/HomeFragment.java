package com.example.loving_essentials.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.loving_essentials.Domain.Entity.Category;
import com.example.loving_essentials.Domain.Entity.Product;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Services.IService.IBrandService;
import com.example.loving_essentials.Domain.Services.IService.ICategoryService;
import com.example.loving_essentials.Domain.Services.IService.IProductService;
import com.example.loving_essentials.Domain.Services.Service.BrandService;
import com.example.loving_essentials.Domain.Services.Service.CategoryService;
import com.example.loving_essentials.Domain.Services.Service.ProductService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CategoryAdapter;
import com.example.loving_essentials.UI.Adapter.ProductAdapter;
import com.example.loving_essentials.UI.Adapter.SliderAdapter;
import com.example.loving_essentials.UI.ProductListActivity;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private DrawerLayout drawerLayout;
    RecyclerView cateRecyclerView, productRecyclerView;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    List<Category> categoryList;
    List<ProductDTO> productList;
    IProductService productService;
    ICategoryService categoryService;
    IBrandService brandService;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        productService = ProductService.getProductService();
        categoryService = CategoryService.getCategoryService();
        brandService = BrandService.getBrandService();

        productRecyclerView = root.findViewById(R.id.new_product_rec);
        cateRecyclerView = root.findViewById(R.id.rec_category);

        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1, "Discount on some items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2, "Discount on some other items", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3, "70% OFF", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        cateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        cateRecyclerView.setAdapter(categoryAdapter);

        Call<Category[]> call = categoryService.getCategories();
        call.enqueue(new Callback<Category[]>() {
            @Override
            public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                Category[] categories = response.body();
                if (categories == null) {
                    return;
                }

                categoryList.addAll(Arrays.asList(categories));
                categoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<Category[]> call, Throwable t) {
                Log.e("Category get", "Category get error", t);
            }
        });
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecyclerView.setAdapter(productAdapter);

        Call<ProductDTO[]> callProduct = productService.getProducts();
        callProduct.enqueue(new Callback<ProductDTO[]>() {
            @Override
            public void onResponse(Call<ProductDTO[]> call, Response<ProductDTO[]> response) {
                ProductDTO[] products = response.body();
                if (products == null) {
                    return;
                }

                productList.addAll(Arrays.asList(products));
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductDTO[]> call, Throwable t) {

            }
        });
        productAdapter.notifyDataSetChanged();
        Button btnSeeAll = (Button) root.findViewById(R.id.newProducts_see_all);

        btnSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}