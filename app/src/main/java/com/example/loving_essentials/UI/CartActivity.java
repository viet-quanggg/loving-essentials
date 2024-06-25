package com.example.loving_essentials.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loving_essentials.Domain.Entity.Cart;
import com.example.loving_essentials.Domain.Entity.Category;
import com.example.loving_essentials.Domain.Entity.ProductDTO;
import com.example.loving_essentials.Domain.Entity.ProductDetail;
import com.example.loving_essentials.Domain.Services.IService.ICartService;
import com.example.loving_essentials.Domain.Services.Service.CartService;
import com.example.loving_essentials.R;
import com.example.loving_essentials.UI.Adapter.CartAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangedListener {
    private List<Cart> carts;
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView txtPrice;
    private Button btnCon, btnOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        txtPrice = findViewById(R.id.totalPrice);
        btnCon = findViewById(R.id.btnContinueShopping);
        btnOrder = findViewById(R.id.btnCheckout);

        loadCartData();

        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProductListActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadCartData() {
        ICartService cartService = CartService.getICartService();
        Call<Cart[]> call = cartService.getAllCarts();

        call.enqueue(new Callback<Cart[]>() {
            @Override
            public void onResponse(Call<Cart[]> call, Response<Cart[]> response) {
                if (response.isSuccessful()) {
                    Cart[] cartsResult = response.body();
                    carts = new ArrayList<>();
                    carts.clear();
                    carts.addAll(Arrays.asList(cartsResult));
                    Double totalPrice = (double) 0;
                    List<Integer> quantity = new ArrayList<>();
                    Map<ProductDTO, Integer> productQuantities = new HashMap<>();
                    for (Cart cart : carts) {
                        for (Map.Entry<Integer, ProductDTO> entry : cart.Products.entrySet()) {
                            productQuantities.put(entry.getValue(), entry.getKey()); // key is quantity, value is product
                        }
                        totalPrice += cart.getPrice();
                    }

                    txtPrice.setText("Total: $" + totalPrice);
                    cartAdapter = new CartAdapter(productQuantities,CartActivity.this,CartActivity.this);
                    recyclerViewCart.setAdapter(cartAdapter);
                } else {
                    Log.e("ProductCardListAdapter", "API call failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Cart[]> call, Throwable t) {
                // Handle failure
            }
        });
    }
    @Override
    public void onQuantityChanged() {
        loadCartData();
    }
}
