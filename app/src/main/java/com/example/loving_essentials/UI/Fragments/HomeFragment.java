package com.example.loving_essentials.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.loving_essentials.Domain.Entity.Category;
import com.example.loving_essentials.Domain.Entity.Product;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CategoryAdapter;
import com.example.loving_essentials.UI.Adapter.ProductAdapter;
import com.example.loving_essentials.UI.Adapter.SliderAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView cateRecyclerView, productRecyclerView;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;
    List<Category> categoryList;
    List<Product> productList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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
        categoryList.add(new Category(1, "Toddler", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/asp-net7/cyptudn2kkyz0lzbcdsu"));
        categoryList.add(new Category(1, "Baby", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/asp-net7/cyptudn2kkyz0lzbcdsu"));
        categoryList.add(new Category(1, "Children", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/asp-net7/cyptudn2kkyz0lzbcdsu"));
        categoryList.add(new Category(1, "Elder", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/asp-net7/cyptudn2kkyz0lzbcdsu"));
        categoryList.add(new Category(1, "Mom", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/asp-net7/cyptudn2kkyz0lzbcdsu"));
        categoryAdapter.notifyDataSetChanged();

        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecyclerView.setAdapter(productAdapter);

        productList.add(new Product(1, "Abbort milk", 10000, LocalDateTime.now(), LocalDateTime.now(), "lorem ipsum", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/MomMomMilk/djsumnqp3dettvvzjxq4", 1, 1));
        productList.add(new Product(1, "Abbort milk", 10000, LocalDateTime.now(), LocalDateTime.now(), "lorem ipsum", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/MomMomMilk/djsumnqp3dettvvzjxq4", 1, 1));
        productList.add(new Product(1, "Abbort milk", 10000, LocalDateTime.now(), LocalDateTime.now(), "lorem ipsum", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/MomMomMilk/djsumnqp3dettvvzjxq4", 1, 1));
        productList.add(new Product(1, "Abbort milk", 10000, LocalDateTime.now(), LocalDateTime.now(), "lorem ipsum", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/MomMomMilk/djsumnqp3dettvvzjxq4", 1, 1));
        productList.add(new Product(1, "Abbort milk", 10000, LocalDateTime.now(), LocalDateTime.now(), "lorem ipsum", "https://res.cloudinary.com/dlr9litcn/image/upload/f_auto,q_auto/v1/MomMomMilk/djsumnqp3dettvvzjxq4", 1, 1));
        productAdapter.notifyDataSetChanged();
        return root;
    }
}