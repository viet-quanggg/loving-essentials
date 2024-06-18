package com.example.loving_essentials.UI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Services.IService.IProductService;
import com.example.loving_essentials.Domain.Services.Service.ProductService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.ProductAdapter;
import com.example.loving_essentials.UI.Adapter.ProductCardListAdapter;
import com.example.loving_essentials.UI.Fragments.HomeFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {
    IProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.productlist_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        productService = ProductService.getProductService();

        RecyclerView recyclerView = findViewById(R.id.rv_productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ProductDTO> products = new ArrayList<>();
        ProductCardListAdapter adapter = new ProductCardListAdapter(products, ProductListActivity.this);
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

    }
}
