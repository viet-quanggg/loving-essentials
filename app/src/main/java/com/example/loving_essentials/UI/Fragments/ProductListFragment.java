package com.example.loving_essentials.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.loving_essentials.Domain.Entity.Brand;
import com.example.loving_essentials.Domain.Entity.Category;
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
import com.example.loving_essentials.UI.Adapter.ProductCardListAdapter;
import com.example.loving_essentials.UI.ProductListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {
    private DrawerLayout drawerLayout;
    IProductService productService;
    ICategoryService categoryService;
    IBrandService brandService;
    List<ProductDTO> products;
    List<Brand> brands;
    List<Category> categories;
    ProductCardListAdapter adapter;
    ArrayAdapter categoryAdapter, brandAdapter;
    AutoCompleteTextView category, brand;
    int selectedCategoryId = 0;
    int selectedBrandId = 0;
    Button btnFilter;
    EditText edtSearchName;
    String searchName;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_productlist, container, false);

        int id = 0;

        Bundle args = getArguments();
        if (args!= null) {
            id = args.getInt("categoryId");
        }

        productService = ProductService.getProductService();
        categoryService = CategoryService.getCategoryService();
        brandService = BrandService.getBrandService();

        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, categories);
        category = root.findViewById(R.id.selectedCategory);
        category.setAdapter(categoryAdapter);
        Call<Category[]> callCategory = categoryService.getCategories();
        callCategory.enqueue(new Callback<Category[]>() {
            @Override
            public void onResponse(Call<Category[]> call, Response<Category[]> response) {
                Category[] categoryResult = response.body();
                if (categoryResult == null) {
                    return;
                }

                Category category = new Category(0, "Any Category");
                categories.addAll(Arrays.asList(categoryResult));
                categories.add(0, category);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Category[]> call, Throwable t) {
            }
        });
        category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = (Category) parent.getItemAtPosition(position);
                selectedCategoryId = selectedCategory.getId();
            }
        });


        brands = new ArrayList<>();
        brandAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, brands);
        brand = root.findViewById(R.id.selectedBrand);
        brand.setAdapter(brandAdapter);
        Call<Brand[]> callBrand = brandService.getBrands();
        callBrand.enqueue(new Callback<Brand[]>() {
            @Override
            public void onResponse(Call<Brand[]> call, Response<Brand[]> response) {
                Brand[] brandResult = response.body();
                if (brandResult == null) {
                    return;
                }
                Brand brand = new Brand(0, "Any Brand");
                brands.addAll(Arrays.asList(brandResult));
                brands.add(0, brand);
                brandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Brand[]> call, Throwable t) {
            }
        });
        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Brand selectedBrand = (Brand) parent.getItemAtPosition(position);
                selectedBrandId = selectedBrand.getId();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.rv_productList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        products = new ArrayList<>();
        adapter = new ProductCardListAdapter(products, getContext());
        Call<ProductDTO[]> callProduct = productService.getProducts();
        callProduct.enqueue(new Callback<ProductDTO[]>() {
            @Override
            public void onResponse(Call<ProductDTO[]> call, Response<ProductDTO[]> response) {
                ProductDTO[] productResult = response.body();
                if (productResult == null) {
                    return;
                }

                products.addAll(Arrays.asList(productResult));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductDTO[]> call, Throwable t) {

            }
        });
        recyclerView.setAdapter(adapter);


        edtSearchName = (EditText) root.findViewById(R.id.edtSearchName);
        btnFilter = (Button) root.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSearchName.getText() != null) {
                    searchName = edtSearchName.getText().toString();
                } else {
                    searchName = "";
                }
                Toast.makeText(getContext(), "Filtering", Toast.LENGTH_SHORT).show();
                Call<ProductDTO[]> callFilteredProduct = productService.getFilteredProducts(searchName, selectedCategoryId, selectedBrandId);
                callFilteredProduct.enqueue(new Callback<ProductDTO[]>() {
                    @Override
                    public void onResponse(Call<ProductDTO[]> call, Response<ProductDTO[]> response) {
                        ProductDTO[] productResult = response.body();
                        if (productResult == null) {
                            return;
                        }

                        products.clear();
                        products.addAll(Arrays.asList(productResult));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ProductDTO[]> call, Throwable t) {
                        Toast.makeText(getContext(), "Error in filter", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (id != 0) {
            InitialCategorySearch(id);
        }

        return root;
    }

    private void InitialCategorySearch(int id) {
        Toast.makeText(getContext(), "Filtering", Toast.LENGTH_SHORT).show();
        Call<ProductDTO[]> callFilteredProduct = productService.getFilteredProducts("", id, 0);
        callFilteredProduct.enqueue(new Callback<ProductDTO[]>() {
            @Override
            public void onResponse(Call<ProductDTO[]> call, Response<ProductDTO[]> response) {
                ProductDTO[] productResult = response.body();
                if (productResult == null) {
                    return;
                }

                products.clear();
                products.addAll(Arrays.asList(productResult));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductDTO[]> call, Throwable t) {
                Toast.makeText(getContext(), "Error in filter", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
